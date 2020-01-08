package com.nn.queue;

import org.apache.activemq.command.ActiveMQQueue;
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

        /**
         *  如果不指定目的地 则 默认监听 默认的目的地
         */

//        jmsTemplate.setDefaultDestination(new ActiveMQQueue("myQueue")); // 设置默认的 目的地
//        TextMessage textMessage  = (TextMessage) jmsTemplate.receive();
//        System.out.println("接收到的消息为："+textMessage.getText()+"  "+textMessage.getStringProperty("flag"));

        Object object = jmsTemplate.receiveAndConvert();
        System.out.println("接收到的消息为：" + object);
    }
}
