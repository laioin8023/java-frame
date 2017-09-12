package com.laioin.frame.thrift.java.api;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.util.StatusPrinter;
import com.laioin.frame.thrift.base.service.TUploadFile;
import com.laioin.frame.thrift.base.service.TUserService;
import com.laioin.frame.thrift.server.impl.TUploadFileServiceImpl;
import com.laioin.frame.thrift.server.impl.TUserServiceImpl;
import com.laioin.frame.thrift.spring.constant.ConstantKeys;
import com.laioin.frame.thrift.spring.init.service.TInitService;
import com.laioin.frame.thrift.spring.init.service.TInitServiceImpl;
import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadedSelectorServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TNonblockingServerTransport;
import org.apache.thrift.transport.TTransportFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IDEA
 * User qinxiancheng
 * Date on 2017/8/3
 * Time on 18:16
 * -- 服务端
 */
public class ServerMain {

    private static final Logger LGR = LoggerFactory.getLogger(ServerMain.class);

    public static void main(String[] args) {
        try {
            ServerMain.initLogback(); // 初始日志
            int port = 9099;
            //传输通道 - 非阻塞方式
            TNonblockingServerTransport serverTransport = new TNonblockingServerSocket(port);
            //异步IO，需要使用TFramedTransport，它将分块缓存读取。
            TTransportFactory transportFactory = new TFramedTransport.Factory();
            //使用高密度二进制协议
            TProtocolFactory proFactory = new TCompactProtocol.Factory();
            //设置处理器 多个 handler
            TMultiplexedProcessor processor = new TMultiplexedProcessor();
            processor.registerProcessor("TUserService", new TUserService.Processor<TUserService.Iface>(new TUserServiceImpl()));
            processor.registerProcessor("TUploadFile", new TUploadFile.Processor<TUploadFile.Iface>(new TUploadFileServiceImpl()));
            processor.registerProcessor(ConstantKeys.T_INIT_SERVICE_NAME, new TInitService.Processor<TInitService.Iface>(new TInitServiceImpl()));
            //创建服务器
            /**
             * TThreadPoolServer         这个在多线程并发的时候，吞吐量高。每一个连接都是一个线程
             *  如果你能提前知道你的client数目并且也不介意多一点线程，TThreadPoolServer对你可能是个好选择
             * TThreadedSelectorServer  目前来说比较安全，的server
             * 允许你为网络I/O分配多个线程。它保持两个线程池，一个为了处理网络I/O, 一个处理请求。
             * 更高的吞吐量且更低的延迟
             */
            TServer server = new TThreadedSelectorServer(
                    new TThreadedSelectorServer.Args(serverTransport)
                            .protocolFactory(proFactory)
                            .transportFactory(transportFactory)
                            .processor(processor)
            );
            LGR.info("thrift server start success on port [{}].", port);
            server.serve(); // 启动服务

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化，logback
     *
     * @throws Exception
     */
    public static void initLogback() throws Exception {
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        JoranConfigurator configurator = new JoranConfigurator();
        configurator.setContext(lc);
        lc.reset();
        configurator.doConfigure(ServerMain.class.getResource("/logback.xml"));
        StatusPrinter.printInCaseOfErrorsOrWarnings(lc);
    }
}
