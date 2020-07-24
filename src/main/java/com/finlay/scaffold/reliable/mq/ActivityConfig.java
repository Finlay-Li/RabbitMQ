package com.finlay.scaffold.reliable.mq;

import com.finlay.scaffold.reliable.ActivityConstant;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: Finlay
 * @description:
 * @date: 2020-07-17 4:59 下午
 */
@Configuration
public class ActivityConfig {


    @Bean
    public Queue activityDelayedQueue() {
        return QueueBuilder.durable(ActivityConstant.DELAYED_QUEUE_NAME).build();
    }

    @Bean
    public Exchange activityDelayedExchange() {
        //声明交换机类型
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "topic");//注意交换机类型不要写错！
        //声明延时类型
        return new CustomExchange(ActivityConstant.DELAYED_EXCHANGE_NAME, "x-delayed-message", true, false, args);
    }

    @Bean
    public Binding activityDelayedBinding() {
        return new Binding(ActivityConstant.DELAYED_QUEUE_NAME, Binding.DestinationType.QUEUE, ActivityConstant.DELAYED_EXCHANGE_NAME, ActivityConstant.DELAYED_ROUTING_KEY, null);
    }

    @Bean
    public Queue confirmQueue() {
        return QueueBuilder.durable(ActivityConstant.CONFIRM_QUEUE_NAME).build();
    }

    @Bean
    public Exchange confirmExchange() {
        return ExchangeBuilder.topicExchange(ActivityConstant.CONFIRM_EXCHANGE_NAME).durable(true).build();
    }

    @Bean
    public Binding confirmBinding() {
        return new Binding(ActivityConstant.CONFIRM_QUEUE_NAME, Binding.DestinationType.QUEUE, ActivityConstant.CONFIRM_EXCHANGE_NAME, ActivityConstant.CONFIRM_ROUTING_KEY, null);
    }

}
