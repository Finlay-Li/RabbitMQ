package com.finlay.scaffold.reliable.mq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finlay.scaffold.reliable.ActivityConstant;
import com.finlay.scaffold.reliable.model.Activity;
import com.finlay.scaffold.reliable.model.ActivityResult;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author: Finlay
 * @description:
 * @date: 2020-07-23 11:37 上午
 */
@Slf4j
@Component
public class ActivityReceiver {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private RedisTemplate redisTemplate;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = ActivityConstant.NORMAL_QUEUE_NAME,
                    durable = "true"),
            exchange = @Exchange(value = ActivityConstant.NORMAL_EXCHANGE_NAME,
                    type = ExchangeTypes.TOPIC,
                    durable = "false"),
            key = ActivityConstant.NORMAL_ROUTING_KEY
    ))
    @RabbitHandler
    public void rec(Message msg, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        //1对1 工作：在ack确认前，不接受其他消息
        channel.basicQos(1);
        Activity activity = mapper.readValue(msg.getBody(), Activity.class);

        if (activity == null) {
            //确认消费完毕(手动ack要记得)
            channel.basicAck(tag, false);
            throw new RuntimeException("Activity is null !");
        }
        /*step3 业务处理逻辑.........*/
        log.info(".........业务处理逻辑.........");

        /*step4 无论逻辑的处理是成功/失败，消费端处理完毕之后,再发送一条Confirm消息(不是ACK)到Broker */
        String activityId = activity.getId();

//        step5Result1(activityId, mapper);
        step5Result1(activityId, mapper);
        channel.basicAck(tag, false);
    }

    void step5Result1(String activityId, ObjectMapper mapper) throws JsonProcessingException {
        /*结果1 ：成功的*/
        ActivityResult result = new ActivityResult();
        result.setActivityId(activityId);
        result.setStatus(1);
        Integer status = result.getStatus();

        if (status == 1) {//伪代码，根据实际业务响应判断处理结果
            /*结果3 ：成功的，MQ发送失败*/
            redisTemplate.opsForSet().add(activityId, status);
        }
        String resultStr = mapper.writeValueAsString(result);
//        rabbitTemplate.convertAndSend(ActivityConstant.CONFIRM_EXCHANGE_NAME, ActivityConstant.CONFIRM_SEND_ROUTING_KEY, resultStr);//不考虑消息持久化

        rabbitTemplate.convertAndSend("？？", ActivityConstant.CONFIRM_SEND_ROUTING_KEY, resultStr);//业务成功，但消息发送是失败的
    }

    void step5Result2(String activityId, ObjectMapper mapper) throws JsonProcessingException {
        /*结果2 ：失败的*/
        ActivityResult result = new ActivityResult();
        result.setActivityId(activityId);
        result.setStatus(0);

        String resultStr = mapper.writeValueAsString(result);
        rabbitTemplate.convertAndSend(ActivityConstant.CONFIRM_EXCHANGE_NAME, ActivityConstant.CONFIRM_SEND_ROUTING_KEY, resultStr);//不考虑消息持久化
    }
}
