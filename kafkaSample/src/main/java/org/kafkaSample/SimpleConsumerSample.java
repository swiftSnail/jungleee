/**
 * @(#)SimpleConsumerSample.java, 2015年10月23日. Copyright 2015 ss, Inc. All
 *                                rights reserved. ss
 *                                PROPRIETARY/CONFIDENTIAL. Use is subject to
 *                                license terms.
 */
package org.kafkaSample;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kafka.api.FetchRequest;
import kafka.api.FetchRequestBuilder;
import kafka.api.PartitionOffsetRequestInfo;
import kafka.common.ErrorMapping;
import kafka.common.TopicAndPartition;
import kafka.javaapi.FetchResponse;
import kafka.javaapi.OffsetRequest;
import kafka.javaapi.OffsetResponse;
import kafka.javaapi.PartitionMetadata;
import kafka.javaapi.TopicMetadata;
import kafka.javaapi.TopicMetadataRequest;
import kafka.javaapi.TopicMetadataResponse;
import kafka.javaapi.consumer.SimpleConsumer;
import kafka.message.MessageAndOffset;
import lombok.extern.apachecommons.CommonsLog;

import com.google.common.collect.Lists;

/**
 * @author yaoxm
 */
@CommonsLog
public class SimpleConsumerSample {
    private static final int PORT = 9092;

    private List<String> replicaBrokers = Lists.newArrayList();

    private static final String TOPIC = "java";

    private static final int PARTITIONID = 0;

    private static final int MAXREADS = 1000000;

    public static void main(String[] args) throws Exception {
        SimpleConsumerSample sample = new SimpleConsumerSample();
        sample.run();
    }

    private void run() throws Exception {
        PartitionMetadata metadata = findLeader(Arrays.asList("61.135.217.81"),
                PORT, TOPIC, PARTITIONID);
        if (null == metadata) {
            log.error("Can't find metadata for Topic and Partition. Exiting");
            return;
        }
        if (null == metadata.leader()) {
            log.equals("Can't find Leader for Topic and Partition. Exiting");
            return;
        }

        String leaderBroker = metadata.leader().host();
        String clientName = "Client_" + TOPIC + "_" + PARTITIONID;

        SimpleConsumer consumer = new SimpleConsumer(leaderBroker, PORT,
                100000, 64 * 1024, clientName);
        long readOffset = getLastOffset(consumer, TOPIC, PARTITIONID,
                kafka.api.OffsetRequest.EarliestTime(), clientName);

        int numErrors = 0;
        int maxReads = MAXREADS;
        while (maxReads > 0) {
            if (consumer == null) {
                consumer = new SimpleConsumer(leaderBroker, PORT, 100000,
                        64 * 1024, clientName);
            }
            FetchRequest req = new FetchRequestBuilder().clientId(clientName)
                    .addFetch(TOPIC, PARTITIONID, readOffset, 100000).build();
            FetchResponse fetchResponse = consumer.fetch(req);

            if (fetchResponse.hasError()) {
                numErrors++;
                // Something went wrong!
                short code = fetchResponse.errorCode(TOPIC, PARTITIONID);
                log.error("Error fetching data from the Broker:"
                        + leaderBroker + " Reason: " + code);
                if (numErrors > 5)
                    break;
                if (code == ErrorMapping.OffsetOutOfRangeCode()) {
                    // We asked for an invalid offset. For simple case ask for
                    // the last element to reset
                    readOffset = getLastOffset(consumer, TOPIC, PARTITIONID,
                            kafka.api.OffsetRequest.LatestTime(), clientName);
                    continue;
                }
                consumer.close();
                consumer = null;
                leaderBroker = findNewLeader(leaderBroker, TOPIC, PARTITIONID,
                        PORT);
                continue;
            }
            numErrors = 0;

            long numRead = 0;
            for (MessageAndOffset messageAndOffset: fetchResponse.messageSet(
                    TOPIC, PARTITIONID)) {
                long currentOffset = messageAndOffset.offset();
                if (currentOffset < readOffset) {
                    System.out.println("Found an old offset: " + currentOffset
                            + " Expecting: " + readOffset);
                    continue;
                }
                readOffset = messageAndOffset.nextOffset();
                ByteBuffer payload = messageAndOffset.message().payload();

                byte[] bytes = new byte[payload.limit()];
                payload.get(bytes);
                System.out.println(String.valueOf(messageAndOffset.offset())
                        + ": " + new String(bytes, "UTF-8"));
                numRead++;
                maxReads--;
            }

            if (numRead == 0) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {}
            }
        }
        if (consumer != null) {
            consumer.close();
        }
    }

    public static long getLastOffset(SimpleConsumer consumer, String topic,
            int partitionId, long whichTime, String clientName) {
        TopicAndPartition topicAndPartition = new TopicAndPartition(topic,
                partitionId);
        Map<TopicAndPartition, PartitionOffsetRequestInfo> requestInfo = new HashMap<TopicAndPartition, PartitionOffsetRequestInfo>();
        requestInfo.put(topicAndPartition, new PartitionOffsetRequestInfo(
                whichTime, 1));
        OffsetRequest request = new OffsetRequest(requestInfo,
                kafka.api.OffsetRequest.CurrentVersion(), clientName);
        OffsetResponse response = consumer.getOffsetsBefore(request);

        if (response.hasError()) {
            log.error("Error fetching data Offset Data the Broker. Reason: "
                    + response.errorCode(topic, partitionId));
            return 0;
        }
        long[] offsets = response.offsets(topic, partitionId);
        return offsets[0];
    }

    private PartitionMetadata findLeader(List<String> brokers, int port,
            String topic, int partitionId) {
        PartitionMetadata retMetadata = null;

        loop: for (String broker: brokers) {
            SimpleConsumer consumer = null;
            try {
                consumer = new SimpleConsumer(broker, port, 100000, 64 * 1024,
                        "leaderLookup");
                List<String> topics = Collections.singletonList(topic);
                TopicMetadataRequest req = new TopicMetadataRequest(topics);
                TopicMetadataResponse resp = consumer.send(req);

                List<TopicMetadata> metaData = resp.topicsMetadata();
                for (TopicMetadata topicMetadata: metaData) {
                    for (PartitionMetadata partitionMetadata: topicMetadata
                            .partitionsMetadata()) {
                        if (partitionMetadata.partitionId() == partitionId) {
                            retMetadata = partitionMetadata;
                            break loop;
                        }
                    }
                }
            } catch (Exception e) {
                log.error("Error communicating with Broker [" + broker
                        + "] to find Leader for [" + topic + ", " + partitionId
                        + "] Reason: ", e);
            } finally {
                if (null != consumer) {
                    consumer.close();
                }
            }
        }

        return retMetadata;
    }

    private String findNewLeader(String oldLeader, String topic,
            int partitionId, int port) throws Exception {
        for (int i = 0; i < 3; i++) {
            boolean gotoSleep = false;
            PartitionMetadata metadata = findLeader(replicaBrokers, port,
                    topic, partitionId);
            if (null == metadata) {
                gotoSleep = true;
            } else if (null == metadata.leader()) {
                gotoSleep = true;
            } else if (oldLeader.equalsIgnoreCase(metadata.leader().host())
                    && i == 0) {
                gotoSleep = true;
            } else {
                return metadata.leader().host();
            }
            if (gotoSleep) {
                try {
                    Thread.sleep(10000);
                } catch (Exception e) {
                    log.error("", e);
                }
            }
        }
        log.error("Unable to find new leader after Broker failure. Exiting");
        throw new Exception(
                "Unable to find new leader after Broker failure. Exiting");
    }
}
