package com.laioin.frame.thrift.spring;

/**
 * Created with IDEA
 * User qinxiancheng
 * Date on 2017/8/4
 * Time on 14:38
 * -- 服务实体类，包装信息
 */
public class ThriftServiceBean {

    private String serviceName; // 服务名称
    private String serviceIface; // 服务接口
    private String serviceImpl; // 服务实现类

    public String getServiceIface() {
        return serviceIface;
    }

    public void setServiceIface(String serviceIface) {
        this.serviceIface = serviceIface;
    }

    public String getServiceImpl() {
        return serviceImpl;
    }

    public void setServiceImpl(String serviceImpl) {
        this.serviceImpl = serviceImpl;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    @Override
    public String toString() {
        return "{" +
                "serviceIface='" + serviceIface + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", serviceImpl='" + serviceImpl + '\'' +
                '}';
    }
}
