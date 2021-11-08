package com.soumen.poc.quorumqueue;

import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Component;

/**
 * @author Soumen Karmakar
 * @Date 08/11/2021
 */
@Component
@Log4j2
public class QuorumQueueConsumer implements MessageListener {

    @Override
    public void onMessage(Message message) {
        String s = new String(message.getBody());
        log.info("Consumer - {}", s);
    }
}
