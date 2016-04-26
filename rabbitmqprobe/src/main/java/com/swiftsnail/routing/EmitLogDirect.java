package com.swiftsnail.routing;

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
public class EmitLogDirect {

    public static final String EXCHANGE_NAME = "direct_logs";
    public static final List<String> routingKeyList = Arrays.asList("error", "warn", "all");

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitClient.getInstance().getChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "direct");

        routingKeyList.listIterator().forEachRemaining(key -> {
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
