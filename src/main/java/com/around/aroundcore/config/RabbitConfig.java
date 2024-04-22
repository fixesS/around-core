package com.around.aroundcore.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    @Value("${spring.rabbitmq.host}")
    private String SPRING_RABBITMQ_HOST;

    @Value("${spring.rabbitmq.port}")
    private int SPRING_RABBITMQ_PORT;

    @Value("${spring.rabbitmq.username}")
    private String SPRING_RABBITMQ_USERNAME;

    @Value("${spring.rabbitmq.password}")
    private String SPRING_RABBITMQ_PASSWORD;
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(SPRING_RABBITMQ_HOST,SPRING_RABBITMQ_PORT);
        cachingConnectionFactory.setUsername(SPRING_RABBITMQ_USERNAME);
        cachingConnectionFactory.setPassword(SPRING_RABBITMQ_PASSWORD);
        return cachingConnectionFactory;
    }
    @Bean
    public AmqpAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory());
    }

}
