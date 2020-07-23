package com.finlay.scaffold.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: Finlay
 * @ClassName: MqQueueConfiguration
 * @Description:
 * @date: 2020-03-30 3:20 PM
 */
@Configuration
public class MqQueueConfiguration {

    @Bean
    public Queue topicQueue() {
        // 参数1 name ：队列名
        // 参数2 durable ：是否持久化
        // 参数3 exclusive ：仅创建者可以使用的私有队列，断开后自动删除
        // 参数4 autoDelete : 当所有消费客户端连接断开后，是否自动删除队列
        Queue queue = new Queue("topic.que", false, false, false);
        return queue;
    }

    @Bean
    public Queue fanoutQueue() {
        Queue queue = new Queue("fanout.que", false, false, false);
        return queue;
    }

    @Bean
    public Exchange fanoutExchange() {
        FanoutExchange exchange = new FanoutExchange("fanout.ex", false, false);
        return exchange;
    }

    @Bean
    public Exchange topicExchange() {
        // 参数1 name ：交换机名称
        // 参数2 durable ：是否持久化
        // 参数3 autoDelete ：当所有消费客户端连接断开后，是否自动删除队列
        TopicExchange exchange = new TopicExchange("topic.ex", false, false);
        return exchange;
    }


    @Bean
    public Binding fanoutBinding() {
        //绑定消费规则
        return new Binding("fanout.que", Binding.DestinationType.QUEUE, "fanout.ex", "", null);
    }

    @Bean
    public Binding topicBinding() {
        //绑定消费规则
        return new Binding("topic.que", Binding.DestinationType.QUEUE, "topic.ex", "topic.#", null);
    }

    @Bean
    public SimpleMessageListenerContainer simpleMessageListenerContainer(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        //1. 设置监听的队列
        container.setQueueNames("topic.que", "fanout.que");
        // 2. 设置消费者数量，默认就是1
        container.setConcurrentConsumers(1);
        container.setMaxConcurrentConsumers(5);
        // 4. 设置消息是否重回队列
        container.setDefaultRequeueRejected(false);

        // 5. 设置消息的签收模式
        container.setAcknowledgeMode(AcknowledgeMode.AUTO);

        // 6.1 声明Listener消费消息
        /*container.setMessageListener((ChannelAwareMessageListener) (message, channel) -> {
            String msg = new String(message.getBody());
            System.out.println("*******消费者*******：" + msg);
        });*/

        //6.2 声明监听适配器
        MessageListenerAdapter adapter = new MessageListenerAdapter();
        adapter.setDelegate(new ImageMessageDelegate());//ImageMessageDelegate自定义的消息转换器实现
        container.setMessageListener(adapter);
        return container;
    }
}