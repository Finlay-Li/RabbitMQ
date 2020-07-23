package com.finlay.scaffold.fanout;


import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

//@Component
public class FanoutReceiver {

    @RabbitListener(queues = "fanout.que")
    @RabbitHandler
    public void rec(String msg, Channel channel, @Headers Map<String, Object> headers) throws IOException {
        Long tag = null;
        try {
            channel.basicQos(1);
            //body
            System.out.println("receiver: " + msg);
            //header
            Object headerMsg1 = headers.get("describe");
            Object headerMsg2 = headers.get("type");
            System.out.println("receiver header: " + headerMsg1);
            System.out.println("receiver header: " + headerMsg2);

            tag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
            channel.basicAck(tag, false);
        } catch (Exception e) {
            // TODO 消费失败,那么我们可以进行容错处理,比如转移当前消息进入其它队列
//                channel.basicNack 与 channel.basicReject 的区别在于basicNack可以拒绝多条消息
//                而basicReject一次只能拒绝一条消息

//                tag:消息标识
//                false:是否批量.true:将一次性拒绝所有小于deliveryTag的消息
//                true:重新排队
            channel.basicNack(tag, false, true);//重排并不是放在最后
        }
    }
}