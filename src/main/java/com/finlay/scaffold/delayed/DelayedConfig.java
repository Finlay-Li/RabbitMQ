package com.finlay.scaffold.delayed;

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
public class DelayedConfig {

    private static final String EXCHANGE_NAME = "delayed.exchange";
    private static final String QUEUE_NAME = "delayed.queue";
    private static final String ROUTING_KEY = "delayed";

    @Bean
    public Queue delayedQueue() {
        return QueueBuilder.durable(QUEUE_NAME).build();
    }

    @Bean
    public Exchange delayedExchange() {
        //声明交换机类型
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        //声明延时类型
        return new CustomExchange(EXCHANGE_NAME, "x-delayed-message", true, false, args);
    }

    @Bean
    public Binding delayedBinding() {
        return new Binding(QUEUE_NAME, Binding.DestinationType.QUEUE, EXCHANGE_NAME, ROUTING_KEY, null);
    }
}
