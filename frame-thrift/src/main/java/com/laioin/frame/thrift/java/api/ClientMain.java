package com.laioin.frame.thrift.java.api;

import com.laioin.frame.thrift.base.model.TResultModel;
import com.laioin.frame.thrift.base.model.TUserInfoRequestParam;
import com.laioin.frame.thrift.base.service.TUploadFile;
import com.laioin.frame.thrift.base.service.TUserService;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IDEA
 * User qinxiancheng
 * Date on 2017/8/3
 * Time on 18:26
 */
public class ClientMain {

    private static TUserService.Client userServiceClient = null;
    private static TUploadFile.Client uplaodServiceClient = null;

    private static final Logger LGR = LoggerFactory.getLogger(ClientMain.class);

    public static void main(String[] args) {
        try {
            String ip = "localhost";
            int port = 9099;
            //设置传输通道，对于非阻塞服务，需要使用TFramedTransport，它将数据分块发送
            TTransport transport = new TFramedTransport(new TSocket(ip, port));
            transport.open();
            //使用高密度二进制协议
            TProtocol protocol = new TCompactProtocol(transport);
            // 获取多个 客户端
            TMultiplexedProtocol userProtocol = new TMultiplexedProtocol(protocol, "TUserService"); // 跟service 里对应
            userServiceClient = new TUserService.Client(userProtocol);
            TMultiplexedProtocol uploadProtocol = new TMultiplexedProtocol(protocol, "TUploadFile");
            uplaodServiceClient = new TUploadFile.Client(uploadProtocol);
            LGR.info("客户端创建成功。");

            ClientMain.getUserInfo(); // 获取用户信息

            transport.close(); // 关闭socket
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 获取用户信息
    private static void getUserInfo() {
        LGR.info("调用 TUserService.getUserInfo() .....");
        TUserInfoRequestParam param = new TUserInfoRequestParam();
        param.setPhone("1316178171665").setUserId(1).setUserName("傻逼啊");
        try {
            TResultModel resultModel = userServiceClient.getUserInfo(param);
            LGR.info("调用 TUserService.getUserInfo() 返回：code=[{}],message=[{}],data=[{}]",
                    resultModel.getCode(), resultModel.getMessage(), resultModel.getData());
        } catch (TException e) {
            e.printStackTrace();
        }
    }
}
