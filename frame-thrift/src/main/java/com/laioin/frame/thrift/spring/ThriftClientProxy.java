package com.laioin.frame.thrift.spring;

import com.laioin.frame.thrift.spring.pool.IThriftPoolManager;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;

/**
 * Created with IDEA
 * User qinxiancheng
 * Date on 2017/8/4
 * Time on 17:36
 * -- thrift 客户端代理
 */
public class ThriftClientProxy {

    private static final Logger LGR = LoggerFactory.getLogger(ThriftClientProxy.class);

    private IThriftPoolManager thriftPoolManager;  // 连接池管理对象

    public void setThriftPoolManager(IThriftPoolManager thriftPoolManager) {
        this.thriftPoolManager = thriftPoolManager;
    }

    /**
     * 获取 Thrift 服务 客户端
     *
     * @param serviceName 服务名称
     *                    必须跟，server 里注册的服务名称对应
     * @param clz         service class 对象
     * @return
     */
    public ServiceClient getServiceClient(String serviceName, Class clz) {
        try {
            TProtocol protocol = thriftPoolManager.getConnection(); // 获取 连接
            // 如果是多个服务注册一个端口， 使用 TMultiplexedProtocol 区分服务
            TMultiplexedProtocol serviceProtocol = new TMultiplexedProtocol(protocol, serviceName);
            Class clientClz = Class.forName(clz.getName() + "$Client");
            Constructor clientCon = clientClz.getConstructor(TProtocol.class);  // 服务客户端 构造函数
            // param1 = 创建服务客户端对象，param2 = thrift 连接对象
            ServiceClient client = new ServiceClient(clientCon.newInstance(serviceProtocol), protocol);
            return client;
        } catch (Exception e) {
            LGR.error("获取 thrift 服务 [{}]. 时错误.", serviceName, e);
        }
        return null;
    }

    /**
     * 归还，service client
     * 调用 client  执行完方法后，必须要归还到 连接池里
     *
     * @param client
     */
    public void returnServiceCilent(ServiceClient client) {
        if (null != client) {
            // 退还给 连接池
            thriftPoolManager.returnConnection(client.thriftProtocol);
        }
    }

    /**
     * 服务的客户端类
     */
    public static class ServiceClient {
        public Object serviceObject; // 服务类
        public TProtocol thriftProtocol; // thrift 连接对象

        public ServiceClient(Object service, TProtocol conn) {
            this.serviceObject = service;
            this.thriftProtocol = conn;
        }
    }
}
