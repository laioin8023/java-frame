package test.thrift.spring;

import com.laioin.frame.thrift.base.model.TResultModel;
import com.laioin.frame.thrift.base.model.TUserInfoRequestParam;
import com.laioin.frame.thrift.base.service.TUserService;
import com.laioin.frame.thrift.spring.ThriftClientProxy;
import org.apache.thrift.TException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created with IDEA
 * User qinxiancheng
 * Date on 2017/8/4
 * Time on 17:51
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:config/thrift-client.xml")
public class TestThriftClient {

    private static final Logger LGR = LoggerFactory.getLogger(TestThriftClient.class);

    @Autowired
    private ThriftClientProxy thriftClientProxy;

    @Test
    public void getUserInfo() {

        // ThriftClientProxy.ServiceClient serviceClient = thriftClientProxy.getServiceClient("TUserService", TUserService.class);
        //ThriftClientProxy.ServiceClient serviceClient = thriftClientProxy.getServiceClient(TUserService.class);

        ThriftClientProxy.ServiceClient<TUserService.Client> serviceClient = thriftClientProxy.getServiceClient(TUserService.class);
        try {
            if (null != serviceClient) {
                TUserService.Client userServiceClient = serviceClient.serviceObject;
                // 获取用户信息
                LGR.info("调用 TUserService.getUserInfo() .....");
                TUserInfoRequestParam param = new TUserInfoRequestParam();
                param.setPhone("1316178171665").setUserId(1).setUserName("傻逼啊");
                TResultModel resultModel = userServiceClient.getUserInfo(param);
                LGR.info("调用 TUserService.getUserInfo() 返回：code=[{}],message=[{}],data=[{}]",
                        resultModel.getCode(), resultModel.getMessage(), resultModel.getData());
            }
        } catch (TException e) {
            e.printStackTrace();
        } finally {
            // 归还service client
            thriftClientProxy.returnServiceClient(serviceClient);
        }
    }
}
