package com.finlay.scaffold.dlx;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: Finlay
 * @description:
 * @date: 2020-07-17 5:28 下午
 */
@Component
public class TestSender {

    private static final String EXCHANGE_NAME = "test.exchange";
    /*发送key*/
    private static final String SEND_ROUTING_KEY = "dlx.test";

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /*只发送，不消费，让其成为死信消息*/
    public void send() {
        MessageProperties properties = new MessageProperties();
        properties.setDelay(2); // 持久化消息
        properties.setExpiration("20000");//20秒过期
        String msg = "Test-Exchange--------------";

        Message message = new Message(msg.getBytes(), properties);
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, SEND_ROUTING_KEY, msg, msgPost -> (message));
    }
}
