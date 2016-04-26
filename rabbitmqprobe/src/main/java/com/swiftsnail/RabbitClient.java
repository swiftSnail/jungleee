package com.swiftsnail;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by yaoxm on 2016/4/3 0003.
 */
public class RabbitClient {
    private static RabbitClient ourInstance = new RabbitClient();

    public static RabbitClient getInstance() {
        return ourInstance;
    }

    private RabbitClient() {
    }

    private Connection connection;
    private Channel channel;

    public Channel getChannel() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("sss");
        factory.setUsername("yao");
        factory.setPassword("111111");

        connection = factory.newConnection();
        channel = connection.createChannel();
        return channel;
    }

    public void close() throws IOException, TimeoutException {
        if (null != channel)
            channel.close();
        if (null != connection)
            connection.close();
    }
}
