package com.example.boatrental.rabbitmq.senders;

import com.example.boatrental.rabbitmq.RabbitMQConfiguration;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class BoatMessageSender {

    private final RabbitTemplate rabbitTemplate;

    public BoatMessageSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendBoatMessage(String message) {
        rabbitTemplate.convertAndSend(RabbitMQConfiguration.exchangeName, "boatStatus.key", message);
        System.out.println("Отправлено в boatStatusQueue: " + message);
    }

}
