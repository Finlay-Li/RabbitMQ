package api.test;

import com.finlay.scaffold.boot.RabbitSender;
import com.finlay.scaffold.delayed.DelayedSender;
import com.finlay.scaffold.dlx.TestSender;
import com.finlay.scaffold.fanout.FanoutSender;
import com.finlay.scaffold.reliable.model.Activity;
import com.finlay.scaffold.reliable.mq.ActivitySender;
import com.finlay.scaffold.topic.TopicSender;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

public class Test1 extends ApplicationTest {

    @Autowired
    private FanoutSender fanoutSender;

    @Autowired
    private TopicSender topicSender;

    @Autowired
    private RabbitSender rabbitSender;

    @Autowired
    private TestSender testSender;

    @Autowired
    private DelayedSender delayedSender;

    @Autowired
    private ActivitySender activitySender;

    @Test
    public void localStoreByUserId() throws Exception {
        fanoutSender.send();
        topicSender.send();
    }

    @Test
    public void rabbitSender() {
        rabbitSender.sendString();
    }

    @Test
    public void testSender() {
        testSender.send();
    }

    @Test
    public void delayedSender() {
        delayedSender.send();
    }

    /*Callback Service 主动发起RPC通信给上游 (模拟)*/
    @Test
    public void activitySender() {
        Activity activity = new Activity();
        activity.setId("1286569799274471424");//写死的，实际从RPC通知时传递
        activity.setPay(BigDecimal.TEN);
        activitySender.send(activity);
    }
}
