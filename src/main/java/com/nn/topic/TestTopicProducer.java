package com.nn.topic;

import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

public class TestTopicProducer {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext-topic.xml");
        JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);
        jmsTemplate.send(new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {

                TextMessage topic_message = session.createTextMessage("topic message");
                topic_message.setStringProperty("flat","topic");
                return topic_message;
            }
        });
        System.out.println("topic 消息发送成功。。。。。");

    }
}
