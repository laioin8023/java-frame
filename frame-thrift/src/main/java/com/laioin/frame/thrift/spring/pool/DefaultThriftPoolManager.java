package com.laioin.frame.thrift.spring.pool;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.thrift.protocol.TProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IDEA
 * User qinxiancheng
 * Date on 2017/8/4
 * Time on 17:19
 */
public class DefaultThriftPoolManager implements IThriftPoolManager {

    private static final Logger LGR = LoggerFactory.getLogger(DefaultThriftPoolManager.class);

    // 连接池
    private GenericObjectPool<TProtocol> objectPool;

    @Override
    public void setPoolFactory(ThriftPoolFactory factory) {
        try {
            // param1 = 连接池创建工厂，param2 = 连接池配置对象
            objectPool = new GenericObjectPool<>(factory, factory);
        } catch (Exception e) {
            LGR.error("创建 Thrift 连接池失败.", e);
        }
    }

    @Override
    public TProtocol getConnection() {
        try {
            return objectPool.borrowObject(); // 返回连接
        } catch (Exception e) {
            LGR.error("获取 thrift 连接错误.", e);
        }
        return null;
    }

    @Override
    public void returnConnection(TProtocol protocol) {
        try {
            objectPool.returnObject(protocol);
        } catch (Exception e) {
            LGR.error("退回 thrift 连接错误.", e);
        }
    }
}
