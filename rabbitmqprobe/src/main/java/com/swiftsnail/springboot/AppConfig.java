package com.swiftsnail.springboot;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by yaoxm on 2016/4/4 0004.
 */

@Configuration
public class AppConfig {

    public static final String EXCHANGE = "spring-boot-exchange";
    public static final String ROUTINGKEY = "spring-boot-routingkey";

    @Bean
    public ConnectionFactory connectionFactory(){
        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setHost("sss");
        factory.setUsername("yao");
        factory.setPassword("111111");
//        factory.setVirtualHost("/");
        //true 才能进行消息的回调。
        factory.setPublisherConfirms(true);
        factory.setPublisherReturns(true);
        return factory;
    }

    @Bean
//    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RabbitTemplate rabbitTemplate(){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());

        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                System.out.println(correlationData);
                if (ack) {
                    System.out.println("send success!");
                } else {
                    System.err.println("send failed!");
                }
            }
        });

        return rabbitTemplate;
    }
}
