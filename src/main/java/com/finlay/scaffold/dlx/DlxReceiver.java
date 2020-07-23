package com.finlay.scaffold.dlx;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author: Finlay
 * @description:
 * @date: 2020-07-17 5:46 下午
 */
@Component
public class DlxReceiver {
    private static final String DLX_QUEUE = "dlx.queue";
    private static final String DLX_EXCHANGE = "dlx.exchange";
    private static final String DLX_ROUTING_KEY = "#";

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = DLX_QUEUE,
                    durable = "true"),
            exchange = @Exchange(value = DLX_EXCHANGE,
                    type = ExchangeTypes.TOPIC),
            key = DLX_ROUTING_KEY
    ))
    @RabbitHandler
    public void rec(Message msg, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        System.out.println("dlx receiver: " + msg);
        channel.basicAck(tag, false);
    }
}
