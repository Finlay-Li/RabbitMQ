package com.finlay.scaffold.delayed;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author: Finlay
 * @description:
 * @date: 2020-07-17 5:28 下午
 */
@Slf4j
@Component
public class DelayedSender {

    private static final String EXCHANGE_NAME = "delayed.exchange";
    /*发送key*/
    private static final String SEND_ROUTING_KEY = "delayed";

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send() {
        MessageProperties properties = new MessageProperties();
        properties.setDelay(2); // 持久化消息
        properties.setHeader("x-delay", 30000);//30秒后投递
        String msg = "Test-Exchange--------------";

        Message message = new Message(msg.getBytes(), properties);

        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        log.info("发送时间：" + sf.format(new Date()));

        rabbitTemplate.convertAndSend(EXCHANGE_NAME, SEND_ROUTING_KEY, message);
    }
}
