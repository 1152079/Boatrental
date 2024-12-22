package com.example.boatrental.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {

    public static final String userQueueName = "userQueue";
    public static final String bookingStatusQueueName = "bookingStatusQueue";
    public static final String boatStatusQueueName = "boatStatusQueue";
    public static final String exchangeName = "testExchange";

    @Bean
    Queue userQueue() {
        return new Queue(userQueueName, false);
    }
    @Bean
    Queue bookingQueue() {
        return new Queue(bookingStatusQueueName, false);
    }
    @Bean
    Queue boatStatusQueue() {
        return new Queue(boatStatusQueueName, false);
    }
    @Bean
    TopicExchange exchange() {
        return new TopicExchange(exchangeName, false, false);
    }

    @Bean
    Binding bindingUser(Queue userQueue, TopicExchange exchange) {
        return BindingBuilder.bind(userQueue).to(exchange).with("userStatus.key");
    }
    @Bean
    Binding bindingBooking(Queue bookingQueue, TopicExchange exchange) {
        return BindingBuilder.bind(bookingQueue).to(exchange).with("bookingStatus.key");
    }
    @Bean
    Binding bindingBoatStatus(Queue boatStatusQueue, TopicExchange exchange) {
        return BindingBuilder.bind(boatStatusQueue).to(exchange).with("boatStatus.key");
    }
}
