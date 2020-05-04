package cz.mailmuni.andris.jmsspring.listener;

import cz.mailmuni.andris.jmsspring.config.JmsConfig;
import cz.mailmuni.andris.jmsspring.model.HelloMessage;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import java.util.UUID;

@Component
public class HelloListener {

    private final JmsTemplate jmsTemplate;

    public HelloListener(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @JmsListener(destination = JmsConfig.MY_QUEUE)
    public void listen(@Payload HelloMessage helloMessage,
                       @Headers MessageHeaders headers, Message message) {

    }

    @JmsListener(destination = JmsConfig.MY_SEND_RECEIVE_QUEUE)
    public void listenForHello(@Payload HelloMessage helloMessage,
                       @Headers MessageHeaders headers, Message message) throws JMSException {
        System.out.println("--RECEIVED MESSAGE--");
        System.out.println("--" + helloMessage.getMessage());
        HelloMessage payloadMsg = HelloMessage.builder()
                .id(UUID.randomUUID())
                .message("confuuuuused")
                .build();

        jmsTemplate.convertAndSend(message.getJMSReplyTo(), payloadMsg);
        System.out.println("--SENDING MESSAGE");
    }

}
