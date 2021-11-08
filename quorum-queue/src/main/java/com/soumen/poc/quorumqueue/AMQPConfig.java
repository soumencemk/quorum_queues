package com.soumen.poc.quorumqueue;

import com.rabbitmq.client.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * @author Soumen Karmakar
 * @Date 08/11/2021
 */
@Configuration
@Log4j2
@RequiredArgsConstructor
public class AMQPConfig {
    private final Environment environment;

    @Bean("rabbitConnectionFactory")
    public CachingConnectionFactory connectionFactory() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(environment.getProperty("rabbitmq.hosts"));
        connectionFactory.setPort(Integer.parseInt(environment.getProperty("rabbitmq.port")));
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(connectionFactory);
        cachingConnectionFactory.setAddresses(environment.getProperty("rabbitmq.hosts"));
        log.info("######### ConnectionFactory initialised ########### ");
        return cachingConnectionFactory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(CachingConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }

    @Bean
    public SimpleMessageListenerContainer quorumQueueContainer(QuorumQueueConsumer queueConsumer) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory());
        container.setDefaultRequeueRejected(false);
        container.addQueueNames(environment.getProperty("quorum.queue.name"));
        container.setConcurrentConsumers(Integer.parseInt(environment.getProperty("concurrent.consumers")));
        container.setMessageListener(queueConsumer);
        return container;
    }


}
