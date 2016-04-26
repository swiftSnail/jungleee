package com.swiftsnail.helloworld;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;
import com.swiftsnail.RabbitClient;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by yaoxm on 2016/4/3 0003.
 */
public class Consumer {
    public static void main(String[] args) throws IOException, InterruptedException, TimeoutException {
        Channel channel = RabbitClient.getInstance().getChannel();

        channel.queueDeclare(Sender.QUEUE_NAME, true, false, false, null);

        channel.basicQos(1);

        QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(Sender.QUEUE_NAME, true, consumer);

        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String msg = new String(delivery.getBody());
            System.out.println("receive msg: " + msg);
        }
    }
}
