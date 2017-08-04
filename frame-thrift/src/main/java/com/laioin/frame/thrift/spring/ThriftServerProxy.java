package com.laioin.frame.thrift.spring;

import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.TProcessor;
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

import java.lang.reflect.Constructor;
import java.util.Map;

/**
 * Created with IDEA
 * User qinxiancheng
 * Date on 2017/8/4
 * Time on 11:48
 * -- thrift 服务端代理 , 用来配置服务
 * 和启动服务
 */
public class ThriftServerProxy extends Thread {

    private static final Logger LGR = LoggerFactory.getLogger(ThriftServerProxy.class);

    /**
     * 第一层：key 是服务器名称，用于一个端口适用多个服务，value 是 服务
     * 第二层： key 是服务接口， value 是 服务实现 类
     */
    private Map<String, ThriftServiceBean> ifaceService;  // 服务这里支持多个服务共享一个 端口
    private Integer port; // 端口

    public ThriftServerProxy() {
    }

    public ThriftServerProxy(Map<String, ThriftServiceBean> ifaceService, Integer port) {
        this.ifaceService = ifaceService;
        this.port = port;
    }

    public void setIfaceService(Map<String, ThriftServiceBean> ifaceService) {
        this.ifaceService = ifaceService;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    @Override
    public void run() {
        this.setName("thrift-server:" + this.port);
        this.startServer(); // 启动服务
    }

    /**
     * 启动服务
     */
    private void startServer() {
        try {
            if (null == port || 0 == port) {
                this.port = 9099;
                throw new ThriftException("端口为空，默认用9099.", new NullPointerException());
            }
            //传输通道 - 非阻塞方式
            TNonblockingServerTransport serverTransport = new TNonblockingServerSocket(port);
            //异步IO，需要使用TFramedTransport，它将分块缓存读取。
            TTransportFactory transportFactory = new TFramedTransport.Factory();
            //使用高密度二进制协议
            TProtocolFactory proFactory = new TCompactProtocol.Factory();
            //设置处理器 多个 handler
            TMultiplexedProcessor processor = new TMultiplexedProcessor();
            this.registerService(processor);  // 注册服务
            TServer server = new TThreadedSelectorServer(
                    new TThreadedSelectorServer.Args(serverTransport)
                            .protocolFactory(proFactory)
                            .transportFactory(transportFactory)
                            .processor(processor)
            );
            LGR.info("启动 thrift 服务。port = [{}].ifaceService=[{}].", this.port, this.ifaceService);
            server.serve(); // 启动服务
        } catch (Exception e) {
            LGR.error("thrift 服务启动时错误。port = [{}].ifaceService=[{}].", this.port, this.ifaceService, e);
        }
    }

    /**
     * 注册，服务
     *
     * @param processor
     */
    private void registerService(TMultiplexedProcessor processor) {
        if (null == ifaceService) {
            throw new ThriftException("服务接口信息为空", new NullPointerException());
        }
        try {
            for (String serviceKey : this.ifaceService.keySet()) {  // 循环 服务名称
                // key = 服务接口，value = 服务实现类
                ThriftServiceBean serviceBean = this.ifaceService.get(serviceKey);
                if (null == serviceBean) {
                    throw new ThriftException("服务配置为空，缺失服务接口和服务实现类.", new NullPointerException());
                }
                // 处理实现服务的类class
                Class serviceProcessorClz = Class.forName(serviceBean.getServiceIface() + "$Processor");
                Class serviceIface = Class.forName(serviceBean.getServiceIface() + "$Iface");// 服务接口
                //处理实现服务的类的构造函数
                Constructor serviceProcessorCon = serviceProcessorClz.getConstructor(serviceIface);

                Class serviceImplClz = Class.forName(serviceBean.getServiceImpl());// 服务实现类的class
                Object serviceImplObject = serviceImplClz.newInstance(); // 服务实现类

                // 处理实现服务的类
                TProcessor processorItem = (TProcessor) serviceProcessorCon.newInstance(serviceImplObject); // 实现类处理类
                // 注册服务，param1 -> 服务名称 ， param2 = 处理实现服务的类
                processor.registerProcessor(serviceBean.getServiceName(), processorItem);
            }
        } catch (Exception e) {
            LGR.error("注册 thrift 服务时错误。", e);
        }
    }
}
