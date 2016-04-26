package com.swiftsnail.publishsubcribe;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;
import com.swiftsnail.RabbitClient;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by yaoxm on 2016/4/3 0003.
 */
public class ReceiveLogOne {
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Channel channel = RabbitClient.getInstance().getChannel();

        channel.exchangeDeclare(EmitLog.EXCHANGE_NAME, "fanout");
        String queueName = "log-fb1";
        channel.queueDeclare(queueName, false, false, false, null);
        channel.queueBind(queueName, EmitLog.EXCHANGE_NAME, "");

        QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(queueName, true, consumer);
        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            System.out.println("received msg: " + new String(delivery.getBody()));
        }
    }
}
