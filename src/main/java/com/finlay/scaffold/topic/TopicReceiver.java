package com.finlay.scaffold.topic;


import cn.hutool.core.util.ObjectUtil;
import com.finlay.scaffold.model.Hehe;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;

import java.io.IOException;

//@Component
public class TopicReceiver {

    /*msg : 消费的消息
     *channel : 当前操作通道
     *@Header : 可以获取到所有的头部信息
     * */
    @RabbitListener(queues = "topic.que")
    @RabbitHandler
    public void rec(Message msg, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) {
        try {
            channel.basicQos(1);
            Hehe hehe = (Hehe) ObjectUtil.deserialize(msg.getBody());
            System.out.println("receiver: " + hehe.toString());
            channel.basicAck(tag, false);
        } catch (Exception e) {
            try {
                // TODO 消费失败,那么我们可以进行容错处理,比如转移当前消息进入其它队列
//                channel.basicNack 与 channel.basicReject 的区别在于basicNack可以拒绝多条消息
//                而basicReject一次只能拒绝一条消息

//                tag:消息标识
//                false:是否批量.true:将一次性拒绝所有小于deliveryTag的消息
//                true:重新排队

                channel.basicNack(tag, false, true);//重排并不是放在最后
            } catch (IOException e1) {
                /*catch异常后，手动发送到指定队列，然后使用channel给rabbitmq确认消息已消费
                给Queue绑定死信队列，使用nack（requque为false）确认消息消费失败
                {
                    未处理成功消息重新入队时如何将消息排到队尾？:不妨将nack设置为false，消费失败时返回ack，重新将这个消息publish到MQ中，这样就可以先消费别的消息
                }*/

                throw new RuntimeException(e1);
            }
        }

    }
}