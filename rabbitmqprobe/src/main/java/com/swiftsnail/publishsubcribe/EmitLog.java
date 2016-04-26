package com.swiftsnail.publishsubcribe;

import com.rabbitmq.client.Channel;
import com.swiftsnail.RabbitClient;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by yaoxm on 2016/4/3 0003.
 */
public class EmitLog {

    public static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitClient.getInstance().getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        for (int i = 0; i <= 5; i++) {
            String msg = "log" + i;
            channel.basicPublish(EXCHANGE_NAME, "", null, msg.getBytes());
            System.out.println("send msg: " + msg);
        }

        RabbitClient.getInstance().close();
    }
}
