package com.finlay.scaffold.delayed;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author: Finlay
 * @description:
 * @date: 2020-07-17 5:46 下午
 */
@Slf4j
@Component
public class DelayedReceiver {
    private static final String QUEUE_NAME = "delayed.queue";

    @RabbitListener(queues = QUEUE_NAME)
    @RabbitHandler
    public void rec(Message msg, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        log.info("结束时间：" + sf.format(new Date()));
        //body
        System.out.println("receiver: " + msg.getBody());
        channel.basicAck(tag, false);
    }
}
