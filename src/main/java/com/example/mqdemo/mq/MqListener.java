package com.example.mqdemo.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * MQ listener that consumes messages from the queue and logs them.
 * 
 * This component is only active when the 'listener' Spring profile is enabled.
 */
@Component
//@Profile("listener")
public class MqListener {

    private static final Logger log = LoggerFactory.getLogger(MqListener.class);

    @Value("${app.mq.queue-name}")
    private String queueName;

    @JmsListener(destination = "${app.mq.queue-name}")
    @Transactional
    public void onMessage(String body) {
        log.warn("Received message from MQ queue '{}': {}", queueName, body);
    }
}
