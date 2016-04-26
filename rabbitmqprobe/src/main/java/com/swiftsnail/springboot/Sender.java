package com.swiftsnail.springboot;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by yaoxm on 2016/4/4 0004.
 */
@Component
public class Sender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Scheduled(fixedDelay = 1000L)
    public void sendMsg() {
//        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
//        rabbitTemplate.convertAndSend(AppConfig.EXCHANGE, AppConfig.ROUTINGKEY, "hello", correlationId);
        this.rabbitTemplate.convertAndSend("foo", "send hello " + new Date().toString());
    }

}
