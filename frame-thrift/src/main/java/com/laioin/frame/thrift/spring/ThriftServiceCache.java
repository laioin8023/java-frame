package com.laioin.frame.thrift.spring;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IDEA
 * User qinxiancheng
 * Date on 2017/9/12
 * Time on 15:34
 */
public class ThriftServiceCache {

    // 保存 key = thrift 服务类全名，value = 注册服务时的名称
    private static final Map<String, String> SERVICES = new HashMap<>();

    /**
     * 添加，服务
     *
     * @param serviceClassName key = thrift 服务类全名
     * @param serviceName      value = 注册服务时的名称
     */
    public static void addService(String serviceClassName, String serviceName) {
        ThriftServiceCache.SERVICES.put(serviceClassName, serviceName);
    }

    /**
     * 获取所有服务
     *
     * @return
     */
    public static Map<String, String> getAllService() {
        return ThriftServiceCache.SERVICES;
    }
}
