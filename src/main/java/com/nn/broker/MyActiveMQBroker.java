package com.nn.broker;

import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.TransportConnector;

public class MyActiveMQBroker {

    public static void main(String[] args) throws Exception {
        BrokerService brokerService = new BrokerService();
        brokerService.setUseJmx(true);
        brokerService.addConnector("tcp://127.0.0.1:61616");
        brokerService.start();
        System.out.println("内嵌Tomcat 启动成功....");
    }
}
