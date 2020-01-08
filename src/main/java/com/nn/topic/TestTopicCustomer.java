package com.nn.topic;

import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.core.JmsTemplate;

public class TestTopicCustomer {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext-topic.xml");
        JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);

        Object o = jmsTemplate.receiveAndConvert();
        System.out.println("接收到 topic 的消息为："+o);
    }
}
