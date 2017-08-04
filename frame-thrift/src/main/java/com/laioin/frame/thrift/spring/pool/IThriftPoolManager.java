package com.laioin.frame.thrift.spring.pool;

import org.apache.thrift.protocol.TProtocol;

/**
 * Created with IDEA
 * User qinxiancheng
 * Date on 2017/8/4
 * Time on 17:15
 * -- Thrift  连接池管理接口
 */
public interface IThriftPoolManager {

    /**
     * 设置生成 连接池对象的 工厂
     *
     * @param factory
     */
    void setPoolFactory(ThriftPoolFactory factory);

    /**
     * 获取连接池对象
     *
     * @return
     */
    TProtocol getConnection();

    /**
     * 用完连接后，把对象返回给连接池
     *
     * @param protocol
     */
    void returnConnection(TProtocol protocol);
}
