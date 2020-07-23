package com.finlay.scaffold.config;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;

/**
 * @author: Finlay
 * @description:
 * @date: 2020-07-15 6:00 下午
 */
public class ImageMessageDelegate implements MessageConverter {
    @Override
    public Message toMessage(Object o, MessageProperties messageProperties) throws MessageConversionException {
        return null;
    }

    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        return null;
    }
}
