/**
 * @(#)ConsumerSample.java, 2015年10月23日. Copyright 2015 ss, Inc. All rights
 *                          reserved. ss PROPRIETARY/CONFIDENTIAL. Use is
 *                          subject to license terms.
 */
package org.kafkaSample;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.sun.swing.internal.plaf.basic.resources.basic;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

/**
 * @author yaoxm
 */
public class ConsumerSample {

    /**
     * 
     */
    private static final String TOPIC = "java";

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("auto.offset.reset", "smallest");// largest smallest
        props.put("zookeeper.connect", "61.135.217.81:2188");
        props.put("group.id", "groupA");
        props.put("zookeeper.session.timeout.ms", "400");
        props.put("zookeeper.sync.time.ms", "200");
        props.put("auto.commit.interval.ms", "1000");

        ConsumerConnector consumer = Consumer
                .createJavaConsumerConnector(new ConsumerConfig(props));

        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(TOPIC, 1);
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer
                .createMessageStreams(topicCountMap);
        List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(TOPIC);
        
        KafkaStream<byte[], byte[]> stream = streams.get(0);
        ConsumerIterator<byte[], byte[]> it = stream.iterator();
        while(it.hasNext()){
            System.out.println(new String(it.next().message()));
        }
        
        if(null != consumer){
            consumer.shutdown();
        }
    }
}
