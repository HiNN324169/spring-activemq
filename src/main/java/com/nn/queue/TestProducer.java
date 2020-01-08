package com.nn.queue;

import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

/**
 *  队列  生产者
 */
public class TestProducer {
    public static void main(String[] args) {

        // 读取spring-queue 配置文件
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext-queue.xml");
        JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);
//        1、发送到默认的目的地
//        jmsTemplate.send(final MessageCreator messageCreator)
        jmsTemplate.send(new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                TextMessage textMessage = session.createTextMessage("spring 和 activemq-queue 整合");
                textMessage.setStringProperty("flag","加急");
                return textMessage;
            }
        });

//        2、发送到自定义目的地
//        jmsTemplate.send(final Destination destination, final MessageCreator messageCreator)

//        3、发送到默认目的地 并设置名称
//        jmsTemplate.send(final String destinationName, final MessageCreator messageCreator)

        System.out.println("消息发送成功。。。。。。");
    }

}
