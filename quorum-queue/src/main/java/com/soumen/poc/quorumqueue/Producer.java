package com.soumen.poc.quorumqueue;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @author Soumen Karmakar
 * @Date 08/11/2021
 */
@Component
@RequiredArgsConstructor
public class Producer {

    private final Environment environment;

    private final RabbitTemplate rabbitTemplate;

    public void send(String message) {
        rabbitTemplate.send(environment.getProperty("quorum.exchange.name"),
                environment.getProperty("routing.key"),
                MessageBuilder.withBody(message.getBytes()).build());
    }
}
