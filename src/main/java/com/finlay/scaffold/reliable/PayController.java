package com.finlay.scaffold.reliable;

import com.finlay.scaffold.reliable.model.Activity;
import com.finlay.scaffold.reliable.mq.ActivitySender;
import com.finlay.scaffold.reliable.support.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * @author: Finlay
 * @description: 保障消息可靠投递
 * @date: 2020-07-23 10:03 上午
 */
@RestController
@RequestMapping("/pay")
public class PayController {

    @Autowired
    private IdWorker idWorker;
    @Autowired
    private ActivitySender activitySender;


    @PostMapping("/normal")
    public String payTest() {
        Long id = idWorker.nextId();
        Activity activity = new Activity();
        activity.setId(id.toString());
        activity.setPay(BigDecimal.TEN);
        System.err.println("用户支付完成！------------ 活动业务开始！");
        activitySender.send(activity);
        return "支付成功！";
    }
}
