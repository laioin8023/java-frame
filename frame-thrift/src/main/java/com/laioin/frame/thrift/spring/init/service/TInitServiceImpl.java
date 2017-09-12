package com.laioin.frame.thrift.spring.init.service;

import com.laioin.frame.thrift.spring.ThriftServiceCache;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created with IDEA
 * User qinxiancheng
 * Date on 2017/9/12
 * Time on 15:31
 */
public class TInitServiceImpl implements TInitService.Iface {

    private static final Logger LGR = LoggerFactory.getLogger(TInitServiceImpl.class);

    /**
     * 获取，服务列表，key = 类全名，value = 服务名称
     *
     * @return
     * @throws TException
     */
    @Override
    public Map<String, String> getServices() throws TException {
        Map<String, String> data = ThriftServiceCache.getAllService();
        LGR.info("客户端获取服务列表：{} 。", data);
        return data;
    }
}
