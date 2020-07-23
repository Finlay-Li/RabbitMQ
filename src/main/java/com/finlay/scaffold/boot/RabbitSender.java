package com.finlay.scaffold.boot;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: Finlay
 * @description:
 * @date: 2020-07-15 8:26 下午
 */
@Component
public class RabbitSender {
    private static final String EXCHANGE_NAME = "boot.exchange";
    private static final String ROUTING_KEY = "springboot.hello";
//    private static final String ROUTING_KEY = "springboot.hexllo"; ----------当routing_key 或 exchange不存在时：触发ReturnCallback

    @Autowired
    private RabbitTemplate rabbitTemplate;

    //confirm
    final RabbitTemplate.ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {

        @Override
        public void confirm(CorrelationData correlationData, boolean b, String s) {
            System.out.println("*************消息发送成功**************");
            System.out.println("ack签收结果------------>：" + b);
            System.out.println("若发生异常，异常信息------------>：" + s);
        }
    };

    //return
    final RabbitTemplate.ReturnCallback returnCallback = new RabbitTemplate.ReturnCallback() {

        @Override
        public void returnedMessage(Message message, int replyCode, String replyText,
                                    String exchange, String routingKey) {
            System.out.println("**************Return********************");
            System.out.println("replyCode：" + replyCode);
            System.out.println("replyText：" + replyText);
            System.out.println("exchange：" + exchange);
            System.out.println("routingKey：" + routingKey);
        }
    };

    public void sendString() {
        String msg = "rabbitmq--------->springboot--------->hello";
        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setReturnCallback(returnCallback);
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, msg);
    }
}
