package com.swiftsnail.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;
import com.swiftsnail.RabbitClient;

/**
 * Created by yaoxm on 2016/4/3 0003.
 */
public class ReceiveLogTopic {
    public static void main(String[] args) throws Exception {
        Channel channel = RabbitClient.getInstance().getChannel();

        channel.exchangeDeclare(EmitLogTopic.EXCHANGE_NAME, "topic");
        String queueName = "queue_topic_logs1";
        channel.queueDeclare(queueName, false, false, false, null);
        String routingKeyOne = "logs.*.one";
//        String routingKeyOne = "*.error.two";
        channel.queueBind(queueName, EmitLogTopic.EXCHANGE_NAME, routingKeyOne);

        QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(queueName, true, consumer);
        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String msg = new String(delivery.getBody());
            System.out.println("receive msg: " + msg
                    + " from key: " + delivery.getEnvelope().getRoutingKey());
        }

    }
}
