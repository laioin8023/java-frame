package com.laioin.frame.thrift.spring;

import com.laioin.frame.thrift.spring.constant.ConstantKeys;
import com.laioin.frame.thrift.spring.init.service.TInitService;
import com.laioin.frame.thrift.spring.pool.IThriftPoolManager;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

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
    private static Map<String, String> SERVICES = new HashMap<>();

    public void setThriftPoolManager(IThriftPoolManager thriftPoolManager) {
        this.thriftPoolManager = thriftPoolManager;
        SERVICES = this.getServices(); // 初始化，服务
    }

    /**
     * 获取， init service  客户端
     *
     * @return
     */
    public ServiceClient getInitServiceClient() {
        return this.getServiceClient(ConstantKeys.T_INIT_SERVICE_NAME, TInitService.class);
    }

    /**
     * 获取，服务类，跟名称的对应关系
     *
     * @return
     */
    public Map<String, String> getServices() {
        ServiceClient serviceClient = this.getInitServiceClient();
        try {
            if (null != serviceClient) {
                TInitService.Client client = (TInitService.Client) serviceClient.serviceObject;
                Map<String, String> data = client.getServices();
                LGR.info("客户端获取到服务对应关系：{} .", data);
                return data;
            }
        } catch (Exception e) {
            LGR.error("", e);
        } finally {
            // 退回到连接池
            this.returnServiceClient(serviceClient);
        }
        return null;
    }

    /**
     * 获取 Thrift 服务 客户端
     *
     * @param serviceName 服务名称
     *                    必须跟，server 里注册的服务名称对应
     * @param clz         service class 对象
     * @return
     */
    public <T> ServiceClient<T> getServiceClient(String serviceName, Class clz) {
        try {
            TProtocol protocol = thriftPoolManager.getConnection(); // 获取 连接
            // 如果是多个服务注册一个端口， 使用 TMultiplexedProtocol 区分服务
            TMultiplexedProtocol serviceProtocol = new TMultiplexedProtocol(protocol, serviceName);
            Class clientClz = Class.forName(clz.getName() + ConstantKeys.T_IN_CLASS_CLIENT);
            Constructor clientCon = clientClz.getConstructor(TProtocol.class);  // 服务客户端 构造函数
            // param1 = 创建服务客户端对象，param2 = thrift 连接对象
            ServiceClient<T> client = new ServiceClient(clientCon.newInstance(serviceProtocol), protocol);
            return client;
        } catch (Exception e) {
            LGR.error("获取 thrift 服务 [{}]. 时错误.", serviceName, e);
        }
        return null;
    }

    /**
     * 获取 Thrift 服务 客户端
     *
     * @param clz service class 对象
     * @return
     */
    public ServiceClient getServiceClient(Class clz) {
        String serviceClassName = clz.getName();
        if (null != SERVICES && SERVICES.containsKey(serviceClassName)) { // 是否有这个服务
            String serviceName = SERVICES.get(serviceClassName);
            // 获取服务
            return this.getServiceClient(serviceName, clz);
        }
        return null;
    }

    /**
     * 归还，service client
     * 调用 client  执行完方法后，必须要归还到 连接池里
     *
     * @param client
     */
    public void returnServiceClient(ServiceClient client) {
        if (null != client) {
            // 退还给 连接池
            thriftPoolManager.returnConnection(client.thriftProtocol);
        }
    }

    /**
     * 服务的客户端类
     */
    public static class ServiceClient<T> {
        public T serviceObject; // 服务类
        public TProtocol thriftProtocol; // thrift 连接对象

        public ServiceClient(T service, TProtocol conn) {
            this.serviceObject = service;
            this.thriftProtocol = conn;
        }
    }
}
