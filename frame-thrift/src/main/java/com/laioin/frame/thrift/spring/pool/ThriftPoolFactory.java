package com.laioin.frame.thrift.spring.pool;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

/**
 * Created with IDEA
 * User qinxiancheng
 * Date on 2017/8/4
 * Time on 16:51
 * <p/>
 * -- 连接池，创建对象工厂
 */
public class ThriftPoolFactory extends GenericObjectPoolConfig implements PooledObjectFactory<TProtocol> {

    private Integer port;
    private String host;

    public ThriftPoolFactory() {
    }

    public ThriftPoolFactory(Integer port, String host) {
        this.host = host;
        this.port = port;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    /**
     * 创建 连接池里的对象，--> 这里是 thrift 的连接对象
     *
     * @return
     * @throws Exception
     */
    @Override
    public PooledObject<TProtocol> makeObject() throws Exception {
        //设置传输通道，对于非阻塞服务，需要使用TFramedTransport，它将数据分块发送
        TTransport transport = new TFramedTransport(new TSocket(host, port));
        transport.open();
        //使用高密度二进制协议
        TProtocol protocol = new TCompactProtocol(transport);
        return new DefaultPooledObject<>(protocol);
    }

    /**
     * 销毁连接
     *
     * @param p thrift 连接
     * @throws Exception
     */
    @Override
    public void destroyObject(PooledObject<TProtocol> p) throws Exception {
        TProtocol tprotocol = p.getObject();
        if (tprotocol.getTransport().isOpen()) {
            tprotocol.getTransport().close();
        }
    }

    /**
     * 判断连接是否可用，这里是 判断 Thrift 是否连接
     *
     * @param p thrift 连接
     * @return
     */
    @Override
    public boolean validateObject(PooledObject<TProtocol> p) {
        // 这里确保返回的是已打开的连接
        TProtocol tprotocol = p.getObject();
        return tprotocol.getTransport().isOpen();
    }

    @Override
    public void activateObject(PooledObject<TProtocol> p) throws Exception {

    }

    @Override
    public void passivateObject(PooledObject<TProtocol> p) throws Exception {

    }
}
