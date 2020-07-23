package com.finlay.scaffold.fanout;


import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class FanoutSender {

    @Autowired
    private AmqpTemplate amqpTemplate;

    public void send() throws Exception {

        //1、自定义消息属性
        MessageProperties properties = new MessageProperties();
        properties.setExpiration("2000");
        //在header 附加消息
        Map<String, Object> headers = properties.getHeaders();
        headers.put("describe", "信息描述");
        headers.put("type", "自定义信息类型");


        // 2、创建消息(消息可以随意，接收类型是Object)
        String msg = "Hello FanoutExchange ...";//body 正文
        Message message = new Message(msg.getBytes(), properties);

        // 3、发送消息，MessagePostProcessor 函数式接口, 至于convertAndSend具体的参数，可以进入到其实现类查看
        amqpTemplate.convertAndSend("fanout.ex", "", msg, msgPost -> (message));
        System.out.println("fanout ： 发送消息成功~~~~~~~~~~~~");
    }
}