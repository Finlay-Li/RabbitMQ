package com.finlay.scaffold.reliable;

/**
 * @author: Finlay
 * @description:
 * @date: 2020-07-23 4:36 下午
 */
public interface ActivityConstant {
    String DELAYED_EXCHANGE_NAME = "pay.delayed.exchange";
    String DELAYED_QUEUE_NAME = "pay.delayed.queue";
    String DELAYED_ROUTING_KEY = "pay.delayed.#";
    
    String NORMAL_QUEUE_NAME = "pay.normal.queue";
    String NORMAL_EXCHANGE_NAME = "pay.normal.exchange";
    String NORMAL_ROUTING_KEY = "pay.#";

    String CONFIRM_QUEUE_NAME = "pay.confirm.queue";
    String CONFIRM_EXCHANGE_NAME = "pay.confirm.exchange";
    String CONFIRM_ROUTING_KEY = "pay.confirm.#";

    String NORMAL_SEND_ROUTING_KEY = "pay.normal.activity";
    String DELAYED_SEND_ROUTING_KEY = "pay.delayed.activity";
    String CONFIRM_SEND_ROUTING_KEY = "pay.confirm.activity";
}
