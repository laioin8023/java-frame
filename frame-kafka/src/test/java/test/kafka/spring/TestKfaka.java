package test.kafka.spring;

import com.laioin.java.frame.kafka.spring.service.KafkaSendMessage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created with IDEA
 * User qinxiancheng
 * Date on 2017/8/1
 * Time on 11:17
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:config/spring-kafka.xml")
public class TestKfaka {

    @Autowired
    private KafkaSendMessage sendMessage;

    @Test
    public void sendMessage() {
        sendMessage.sendMessage("--adfafd");
    }
}
