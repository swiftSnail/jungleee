package com.swiftsnail.helloworld;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import com.swiftsnail.RabbitClient;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by yaoxm on 2016/4/3 0003.
 */
public class Sender {

    public static final String QUEUE_NAME = "task_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitClient.getInstance().getChannel();

        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        String message = "hello world";
        channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
        System.out.println("send msg: " + message);

        RabbitClient.getInstance().close();
    }
}
