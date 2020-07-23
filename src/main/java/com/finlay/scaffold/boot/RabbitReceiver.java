package com.finlay.scaffold.boot;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

/**
 * @author: Finlay
 * @description:
 * @date: 2020-07-15 8:38 下午
 */
@Component
public class RabbitReceiver {

    private static final String EXCHANGE_NAME = "boot.exchange";
//    private static final String ROUTING_KEY = "springboot.#";
    private static final String ROUTING_KEY = "springboot.hello";
    private static final String QUEUE_NAME = "boot.queue";

    //直接通过@RabbitListener，完成QUEUE，EXCHANGE 的【声明、绑定】
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = QUEUE_NAME,
                    durable = "true"),
            exchange = @Exchange(value = EXCHANGE_NAME,
                    type = ExchangeTypes.TOPIC,
                    durable = "false"),
            key = ROUTING_KEY
    ))
    @RabbitHandler
    public void rec(String msg, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) {
        try {
            channel.basicQos(1);
            System.out.println("receiver: " + msg);
            channel.basicAck(tag, false);
        } catch (Exception e) {
            //不建议再放回队列，可以采用ConfirmCallback 机制进行处理
            throw new RuntimeException(e);
        }
    }
}
