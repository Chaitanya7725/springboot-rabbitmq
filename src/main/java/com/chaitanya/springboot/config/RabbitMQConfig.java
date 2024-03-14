package com.chaitanya.springboot.config;

import com.rabbitmq.client.AMQP;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {


    @Value("${rabbitmq.queue.name}")
    private String queue;

    @Value("${rabbitmq.queue.json.name}")
    private String jsonQueue;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.key.name}")
    private String routingKey;

    @Value("${rabbitmq.routing.json.key}")
    private String routingJsonKey;

    @Bean
    public Queue queue(){
        return new Queue(queue);
    }

    // Spring bean for queue(store json messages)
    @Bean
    public Queue jsonQueue(){
        return new Queue(jsonQueue);
    }

    //spring bean for routing exchange
    @Bean
    public TopicExchange exchange(){
        return new TopicExchange(exchange);
    }

    //Binding Queue and exchange using RK
    @Bean
    public Binding binding(){
        return BindingBuilder
                .bind(queue())
                .to(exchange())
                .with(routingKey);
    }

    //Binding jsonQueue and exchange using RK
    //Binding Queue and exchange using RK
    @Bean
    public Binding jsonBinding(){
        return BindingBuilder
                .bind(jsonQueue())
                .to(exchange())
                .with(routingJsonKey);
    }

    @Bean
    public MessageConverter converter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }


    // Following infrastructure beans are injected by spring when needed
    // ConnectionFactory
    // RabbitTemplate
    // RabbitAdmin

}
