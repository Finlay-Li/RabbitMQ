package api.test;

import com.finlay.scaffold.boot.RabbitSender;
import com.finlay.scaffold.delayed.DelayedSender;
import com.finlay.scaffold.dlx.TestSender;
import com.finlay.scaffold.fanout.FanoutSender;
import com.finlay.scaffold.topic.TopicSender;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
}
