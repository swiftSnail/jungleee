package com.swiftsnail.spring;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by yaoxm on 2016/4/5 0005.
 */
@Configuration
@ComponentScan("com.swiftsnail.spring")
public class RabbitMQConfig {
    public final static String queueName = "simple.queue.name";

    @Bean
    public ConnectionFactory connectionFactory(){
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory("sss");
        connectionFactory.setUsername("yao");
        connectionFactory.setPassword("111111");
        return connectionFactory;
    }

    @Bean
    RabbitTemplate rabbitTemplate(){
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setRoutingKey(queueName);
        template.setMessageConverter(jsonMessageConverter());

        return template;
    }

    @Bean
    MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    Queue queue(){
        return new Queue(queueName, false);
    }

//    @Bean
//    TopicExchange exchange(){
//        return new TopicExchange("topic_exchange");
//    }
//
//    @Bean
//    Binding binding(Queue queue, TopicExchange exchange){
//        return BindingBuilder.bind(queue).to(exchange).with(queueName);
//    }

//    @Bean
//    DirectExchange exchange(){
//        return new DirectExchange("direct_exchange");
//    }
//
//    @Bean
//    Binding binding(Queue queue, DirectExchange exchange){
//        return BindingBuilder.bind(queue).to(exchange).with(queueName);
//    }

    @Bean
    FanoutExchange exchange(){
        return new FanoutExchange("fanout_exchange");
    }

    @Bean
    Binding binding(Queue queue, FanoutExchange exchange){
        return BindingBuilder.bind(queue).to(exchange);
    }

    @Bean
    SimpleMessageListenerContainer container(){
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();

        container.setConnectionFactory(connectionFactory());
        container.setQueues(queue());
        container.setMessageConverter(jsonMessageConverter());
        container.setMessageListener(new Consumer());
        container.setAcknowledgeMode(AcknowledgeMode.AUTO);

        return container;
    }

}
