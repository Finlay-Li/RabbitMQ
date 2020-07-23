package com.finlay.scaffold.dlx;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: Finlay
 * @description:
 *
 * 由于RabbitMQ本身不支持延时队列（延时消息），所以要通过其他方式来实现。总的来说有三种：
 *
 * 1. 先存储到数据库，用定时任务扫描，登记时刻+延时时间，就是需要投递的时刻
 * 2. 利用RabbitMQ的死信队列（Dead Letter Queue）实现
 * 3. 利用rabbitmq-delayed-message-exchange插件
 * 需要注意：如果队列没有指定DLX或者无法被路由到一个DLQ，则队列中过期的消息会被直接丢弃。因此，我们可以利用消息TTL的特性，实现消息的延时投递。
 *
 * 使用死信队列实现延时消息的缺点：
 * 1. 如果统一用队列来设置消息的TTL，当梯度非常多的情况下，比如1分钟，2分钟，5分钟，10分钟，20分钟，30分钟……需要创建很多交换机和队列来路由消息。
 * 2. 如果单独设置消息的TTL，则可能会造成队列中的消息阻塞——前一条消息没有出队（没有被消费），后面的消息无法投递。
 * 3. 可能存在一定的时间误差
 *
 * @date: 2020-07-17 4:59 下午
 */
@Configuration
public class DlxConfig {

    private static final String EXCHANGE_NAME = "test.exchange";
    private static final String QUEUE_NAME = "test.queue";
    //绑定key
    private static final String ROUTING_KEY = "dlx.*";

    private static final String DEAD_LETTER_EXCHANGE = "dlx.exchange";

    @Bean
    public Queue testQueue() {
        Map<String, Object> args = new HashMap<>(2);
//       x-dead-letter-exchange    声明  死信交换机
        args.put("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE);
//       x-dead-letter-routing-key    声明 死信路由键,可以不声明
//        args.put("x-dead-letter-routing-key", DEAD_LETTER_ROUTING_KEY); dlx绑定的是 #
        return QueueBuilder.durable(QUEUE_NAME).withArguments(args).build();
    }

    @Bean
    public Exchange testExchange() {
        return ExchangeBuilder.topicExchange(EXCHANGE_NAME).durable(true).build();
    }

    @Bean
    public Binding testBinding() {
        return new Binding(QUEUE_NAME, Binding.DestinationType.QUEUE, EXCHANGE_NAME, ROUTING_KEY, null);
    }

    /*参数要求具体类型的Exchange，如TopicExchange testExchange 那么会导致无法再声明同类型的Exchange（单例）
    @Bean
    public Binding testBinding(Queue testQueue, TopicExchange testExchange) {
        return BindingBuilder.bind(testQueue).to(testExchange).with(ROUTING_KEY);
    }*/
}
