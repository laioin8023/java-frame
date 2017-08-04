package test.thrift.spring;

import com.laioin.frame.thrift.spring.ThriftServerProxy;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created with IDEA
 * User qinxiancheng
 * Date on 2017/8/4
 * Time on 15:09
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:config/thrift-service.xml")
public class TestThriftServer {

    @Autowired
    private ThriftServerProxy thriftServerProxy;

    @Test  // 启动 thrift 服务
    public void startThriftServer() throws InterruptedException {
        thriftServerProxy.start();
        Thread.sleep(1000 * 60 * 60);
    }
}
