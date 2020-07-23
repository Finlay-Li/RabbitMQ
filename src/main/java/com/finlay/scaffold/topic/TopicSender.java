package com.finlay.scaffold.topic;


import com.finlay.scaffold.model.Hehe;
import com.finlay.scaffold.model.Hei;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TopicSender {

    @Autowired
    private AmqpTemplate amqpTemplate;
    private String TOPIC_EX = "topic.ex";
    //生产者发送RoutingKey
    private String RoutingKey = "topic.hello.fast";
    private String msg = "交换机类型是：topic，模糊匹配RoutingKey";

    public void send() throws Exception {
        Hehe hehe = new Hehe();
        hehe.setI(1);
        Hei hei = new Hei();
        hei.setNum(BigDecimal.ONE);
        hehe.setO(hei);

        //SimpleMessageConverter only supports String, byte[] and Serializable payloads : 实体类有没有序列化
        // 默认的转换器是SimpleMessageConverter，它适用于String、Serializable实例和字节数组。
        //仅推荐JSONString、Serializable实例
        amqpTemplate.convertAndSend(TOPIC_EX, RoutingKey, hehe);
        System.out.println(msg);
    }
}