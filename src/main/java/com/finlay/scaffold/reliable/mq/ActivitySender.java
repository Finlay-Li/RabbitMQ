package com.finlay.scaffold.reliable.mq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finlay.scaffold.reliable.ActivityConstant;
import com.finlay.scaffold.reliable.model.Activity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author: Finlay
 * @description:
 * @date: 2020-07-23 10:41 上午
 */
@Slf4j
@Service
public class ActivitySender {


    @Autowired
    private RabbitTemplate rabbitTemplate;

    /***
     * @description: step1 + step2
     * @Param activity: 业务数据
     * @return: void
     **/
    public void send(Activity activity) {
        ObjectMapper mapper = new ObjectMapper();
        byte[] bytes;
        try {
            bytes = mapper.writeValueAsBytes(activity);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
        MessageProperties msgProperties = new MessageProperties();
        //持久化消息
        msgProperties.setDelay(2);
        Message step1Message = new Message(bytes, msgProperties);
        /*step1*/
        //Activity对象数据不复杂，因此没有采用json传输
        rabbitTemplate.convertAndSend(ActivityConstant.NORMAL_EXCHANGE_NAME, ActivityConstant.NORMAL_SEND_ROUTING_KEY, step1Message);

        /*step2*/
        msgProperties.setHeader("x-delay", 100000);//1分钟延迟
        Message step2Message = new Message(bytes, msgProperties);
        //Activity对象数据不复杂，因此没有采用json传输，json传输具体可参考dlx的send案例
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        log.info("延迟开始时间：" + sf.format(new Date()));
        rabbitTemplate.convertAndSend(ActivityConstant.DELAYED_EXCHANGE_NAME, ActivityConstant.DELAYED_SEND_ROUTING_KEY, step2Message);
    }
}
