package cz.mailmuni.andris.jmsspring.sender;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.mailmuni.andris.jmsspring.config.JmsConfig;
import cz.mailmuni.andris.jmsspring.model.HelloMessage;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.util.UUID;

@Component
public class HelloSender {

    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;

    public HelloSender(JmsTemplate jmsTemplate, ObjectMapper objectMapper) {
        this.jmsTemplate = jmsTemplate;
        this.objectMapper = objectMapper;
    }

    @Scheduled(fixedRate = 2000)
    public void sendMessage() {
        HelloMessage message = HelloMessage.builder()
                .id(UUID.randomUUID())
                .message("hello")
                .build();

        jmsTemplate.convertAndSend(JmsConfig.MY_QUEUE, message);

    }

    @Scheduled(fixedRate = 2000)
    public void sendAndRecieveMessage() throws JMSException {

        HelloMessage message = HelloMessage.builder()
                .id(UUID.randomUUID())
                .message("hahahhaha")
                .build();

        Message receivedMsg = jmsTemplate.sendAndReceive(JmsConfig.MY_SEND_RECEIVE_QUEUE, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                try {
                    Message helloMessage = session.createTextMessage(objectMapper.writeValueAsString(message));
                    helloMessage.setStringProperty("_type", "cz.mailmuni.andris.jmsspring.model.HelloMessage");

                    System.out.println("..SENDING MESSAGE..");

                    return helloMessage;
                } catch (JsonProcessingException e) {
                    throw new JMSException("kaboom");
                }
            }
        });

        System.out.println("..RECEIVE REPLY..");
        System.out.println("..." + receivedMsg.getBody(String.class));

    }
}
