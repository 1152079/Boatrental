package com.example.boatrental.rabbitmq.senders;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import com.example.boatrental.rabbitmq.RabbitMQConfiguration;
import org.springframework.stereotype.Service;

@Service
public class BookingMessageSender {

    private final RabbitTemplate rabbitTemplate;

    public BookingMessageSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendBookingMessage(String message) {
        rabbitTemplate.convertAndSend(RabbitMQConfiguration.exchangeName, "bookingStatus.key", message);
        System.out.println("Отправлено в bookingStatusQueue: " + message);
    }
}
