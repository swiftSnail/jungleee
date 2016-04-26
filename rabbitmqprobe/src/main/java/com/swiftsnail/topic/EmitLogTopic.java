package com.swiftsnail.topic;

import com.rabbitmq.client.Channel;
import com.swiftsnail.RabbitClient;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.stream.IntStream;

/**
 * Created by yaoxm on 2016/4/3 0003.
 */
public class EmitLogTopic {
    public static final List<String> routingKeyList = Arrays
            .asList("logs.error.one", "logs.error.two", "logs.info");
    public static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitClient.getInstance().getChannel();
        channel.exchangeDeclare("topic_log", "topic");

        routingKeyList.forEach(key -> {
            IntStream.range(1, 5).forEach(i -> {
                try {
                    String msg = key + i;
                    channel.basicPublish(EXCHANGE_NAME,
                            key,
                            null,
                            msg.getBytes());
                    System.out.println("sent msg:" + msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

        });

        RabbitClient.getInstance().close();
    }
}