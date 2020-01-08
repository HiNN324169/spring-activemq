package com.nn.queue;

import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

/**
 *  队列消费者
 */
public class TestCustomer {
    public static void main(String[] args) throws JMSException {

        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext-queue.xml");
        JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);

//        TextMessage textMessage  = (TextMessage) jmsTemplate.receive();
//        System.out.println("接收到的消息为："+textMessage.getText()+"  "+textMessage.getStringProperty("flag"));

        Object object = jmsTemplate.receiveAndConvert();
        System.out.println("接收到的消息为：" + object);
    }
}
