package com.swiftsnail.spring;

import com.swiftsnail.spring.model.EventMsg;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by yaoxm on 2016/4/5 0005.
 */
public class App {
    public static void main(String[] args) throws InterruptedException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(RabbitMQConfig.class);
        context.refresh();

        RabbitTemplate rabbitTemplate = (RabbitTemplate) context.getBean("rabbitTemplate");

        for (int i = 0; i < 10; i++) {
            System.out.println("sending new message");
            rabbitTemplate.convertAndSend(new EventMsg(1, "one"));
//            rabbitTemplate.convertAndSend("aaaaaabbbbbb11111");
        }

//        context.close();
    }
}
