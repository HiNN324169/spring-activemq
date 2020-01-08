## spring 整合 ActiveMQ
`版本说明`
```$xslt
<activemq.version>5.15.9</activemq.version>
<slf4j.version>1.7.25</slf4j.version>
<lombok.version>1.18.8</lombok.version>
<jackson.vsersion>2.10.0</jackson.vsersion>

<!-- spring 版本-->
<spring.version>4.3.7.RELEASE</spring.version>
```

#### 1、引入依赖
```$xslt
<dependencies>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-jms</artifactId>
            <version>${spring.version}</version>
        </dependency>

        <!-- activemq 依赖-->
        <!-- https://mvnrepository.com/artifact/org.apache.activemq/activemq-all -->
        <dependency>
            <groupId>org.apache.activemq</groupId>
            <artifactId>activemq-all</artifactId>
            <version>${activemq.version}</version>
        </dependency>

        <dependency>
            <groupId>org.apache.activemq</groupId>
            <artifactId>activemq-pool</artifactId>
            <version>${activemq.version}</version>
        </dependency>

        <!-- 日志 依赖 -->
        <!-- https://mvnrepository.com/artifact/org.slf4j/slf4j-api -->
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>${slf4j.version}</version>
        </dependency>
        <!-- 日志 依赖 -->

        <!-- https://mvnrepository.com/artifact/org.projectlombok/lombok -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>${lombok.version}</version>
            <scope>provided</scope>
        </dependency>

        <!-- https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-databind -->
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
            <version>${jackson.vsersion}</version>
        </dependency>
    </dependencies>
```


### 加餐
###### 本次 使用的是内嵌Broker
```$xslt
public class MyActiveMQBroker {

    public static void main(String[] args) throws Exception {
        BrokerService brokerService = new BrokerService();
        brokerService.setUseJmx(true);
        brokerService.addConnector("tcp://127.0.0.1:61616");
        brokerService.start();
        System.out.println("内嵌Tomcat 启动成功....");
    }
}
```

### queue 队列阻塞式

##### 1、创建 applicationContext-queue.xml 配置文件

- 声明连接工厂：org.apache.activemq.pool.PooledConnectionFactory
    - 注入ActiveMQConnectionFactory
    - 设置最大连接 ：maxConnections
    
- 声明目的地：队列：org.apache.activemq.command.ActiveMQQueue
    - 使用有参构造函数注入 队列的名字
```$xslt
<constructor-arg index="0" value="spring-activemq-queue"/>
```

- 声明 操作mq 对象的JMSTemplate：org.springframework.jms.core.JmsTemplate
    - 注入连接工厂：connectionFactory
    - 设置消息发送的 默认目的地：defaultDestination
    - 设置消息转发器：org.springframework.jms.support.converter.SimpleMessageConverter
    

##### 2、创建 生产者：com.nn.queue.TestProducer：非注解方式

- 加载 applicationContext-queue.xml 配置文件，创建JMSTemplate对象
```$xslt
ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext-queue.xml");
JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);
```
- 发送消息
```$xslt
// 1、发送到默认的目的地
jmsTemplate.send(final MessageCreator messageCreator)

2、发送到自定义目的地
jmsTemplate.send(final Destination destination, final MessageCreator messageCreator)

3、发送到默认目的地 并设置名称
jmsTemplate.send(final String destinationName, final MessageCreator messageCreator)
```
```$xslt
jmsTemplate.send(new MessageCreator() {
    public Message createMessage(Session session) throws JMSException {
        TextMessage textMessage = session.createTextMessage("spring 和 activemq-queue 整合");
        textMessage.setStringProperty("flag","加急");
        return textMessage;
    }
});
```


##### 3、创建 消费者：com.nn.queue.TestCustomer ：非注解方式

- 加载 applicationContext-queue.xml 配置文件，创建JMSTemplate对象
```$xslt
ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext-queue.xml");
JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);
```
注意：如果不指定目的地 则 默认监听 默认的目的地
```$xslt
 // 指定 目的地
jmsTemplate.setDefaultDestination(new ActiveMQQueue("myQueue"));
```

- 接收消息
```$xslt
堵塞式：方式一
TextMessage textMessage  = (TextMessage) jmsTemplate.receive();
System.out.println("接收到的消息为："+textMessage.getText()+"  "+textMessage.getStringProperty("flag"));

堵塞式：方式二
Object object = jmsTemplate.receiveAndConvert();
```


### Topic 主题 阻塞式

##### 1、创建 applicationContext-topic.xml 配置文件

- 声明连接工厂：org.apache.activemq.pool.PooledConnectionFactory
    - 注入ActiveMQConnectionFactory
    - 设置最大连接 ：maxConnections
    
- 声明目的地：队列：org.apache.activemq.command.ActiveMQTopic
    - 使用有参构造函数注入 队列的名字
```$xslt
<constructor-arg index="0" value="spring-activemq-queue"/>
```

- 声明 操作mq 对象的JMSTemplate：org.springframework.jms.core.JmsTemplate
    - 注入连接工厂：connectionFactory
    - 设置消息发送的 默认目的地：defaultDestination
    - 设置消息转发器：org.springframework.jms.support.converter.SimpleMessageConverter
    

##### 2、创建 生产者：com.nn.topic.TestTopicProducer：非注解方式

- 加载 applicationContext-queue.xml 配置文件，创建JMSTemplate对象
```$xslt
ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext-queue.xml");
JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);
```
- 发送消息
```$xslt
// 1、发送到默认的目的地
jmsTemplate.send(final MessageCreator messageCreator)

2、发送到自定义目的地
jmsTemplate.send(final Destination destination, final MessageCreator messageCreator)

3、发送到默认目的地 并设置名称
jmsTemplate.send(final String destinationName, final MessageCreator messageCreator)
```

```$xslt
jmsTemplate.send(new MessageCreator() {
public Message createMessage(Session session) throws JMSException {

    TextMessage topic_message = session.createTextMessage("topic message");
    topic_message.setStringProperty("flat","topic");
    return topic_message;
    }
});
```



##### 3、创建 消费者：com.nn.topic.TestTopicCustomer ：非注解方式

- 加载 applicationContext-queue.xml 配置文件，创建JMSTemplate对象
```$xslt
ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext-topic.xml");
JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);
```
注意：如果不指定目的地 则 默认监听 默认的目的地
```$xslt
 // 指定 目的地
jmsTemplate.setDefaultDestination(new ActiveMQQueue("myTopic"));
```

- 接收消息
```$xslt
堵塞式：方式一
TextMessage textMessage  = (TextMessage) jmsTemplate.receive();
System.out.println("接收到的消息为："+textMessage.getText()+"  "+textMessage.getStringProperty("flag"));

堵塞式：方式二
Object object = jmsTemplate.receiveAndConvert();
```



##### **三个点必须注意**
- 如何设置默认目的地
- 如何监听新的目的地
- 如何给非默认目的地 发送消息



