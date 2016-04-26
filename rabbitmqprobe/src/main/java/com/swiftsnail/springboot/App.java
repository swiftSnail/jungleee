package com.swiftsnail.springboot;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Date;

/**
 * Created by yaoxm on 2016/4/4 0004.
 */
@SpringBootApplication
@RabbitListener(queues = "foo")
@EnableScheduling
public class App {

    @Bean
    public Sender sender() {
        return new Sender();
    }

    @Bean
    public Queue fooQueue() {
        return new Queue("foo");
    }

    @RabbitHandler
    public void proecss(String foo) {
        System.out.println("receive data: " + new Date() + ":" + foo);
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .sources(App.class)
                .showBanner(false)
                .headless(true)
                .run(args);
    }
}
