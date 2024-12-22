package com.example.boatrental.rabbitmq.senders;

import com.example.boatrental.rabbitmq.RabbitMQConfiguration;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserMessageSender {

    private final RabbitTemplate rabbitTemplate;

    public UserMessageSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendUserMessage(String message) {
        rabbitTemplate.convertAndSend(RabbitMQConfiguration.exchangeName, "userStatus.key", message);
        System.out.println("Отправлено в " + RabbitMQConfiguration.userQueueName + ": " + message);
    }

}
