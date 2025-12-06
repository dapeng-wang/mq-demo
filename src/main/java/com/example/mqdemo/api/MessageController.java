package com.example.mqdemo.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private static final Logger log = LoggerFactory.getLogger(MessageController.class);

    private final JmsTemplate jmsTemplate;
    private final String queueName;

    public MessageController(JmsTemplate jmsTemplate,
                             @Value("${app.mq.queue-name}") String queueName) {
        this.jmsTemplate = jmsTemplate;
        this.queueName = queueName;
    }

    /**
     * Accepts a plain text message and sends it to the configured MQ queue.
     */
    @PostMapping(consumes = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<Void> sendMessage(@RequestBody String body) {
        log.info("Sending message to MQ queue '{}': {}", queueName, body);
        jmsTemplate.convertAndSend(queueName, body);
        return ResponseEntity.accepted().build();
    }
}
