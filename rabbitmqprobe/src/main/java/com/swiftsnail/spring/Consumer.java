package com.swiftsnail.spring;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

/**
 * Created by yaoxm on 2016/4/5 0005.
 */
public class Consumer implements MessageListener {
    @Override
    public void onMessage(Message message) {
        System.out.println("receive msg: " + new String(message.getBody()));
    }
}
