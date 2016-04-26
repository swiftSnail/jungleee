package com.swiftsnail.routing;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;
import com.swiftsnail.RabbitClient;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.stream.IntStream;

/**
 * Created by yaoxm on 2016/4/3 0003.
 */
public class ReceiveLogDirect {

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Channel channel = RabbitClient.getInstance().getChannel();

        channel.exchangeDeclare(EmitLogDirect.EXCHANGE_NAME, "direct");
        String queueName = "queue_log1";
        channel.queueDeclare(queueName, false, false, false, null);

        IntStream.range(0, 2).forEach(i -> {
            try {
                channel.queueBind(queueName,
                        EmitLogDirect.EXCHANGE_NAME,
                        EmitLogDirect.routingKeyList.get(i));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(queueName, true, consumer);

        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String msg = new String(delivery.getBody());
            System.out.println("receive msg: " + msg + " from key: " + delivery.getEnvelope().getRoutingKey());
        }
    }
}
