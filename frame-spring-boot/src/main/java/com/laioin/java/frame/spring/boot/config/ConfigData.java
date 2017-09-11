package com.laioin.java.frame.spring.boot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Created with IDEA
 * User qinxiancheng
 * Date on 2017/9/4
 * Time on 17:47
 * <p/>
 * <p/>
 * -- 标注，@Configuration 的类不能，序列化json 打印出去
 */
@Configuration
@PropertySource("classpath:config.properties")
public class ConfigData {

    @Value("${sms.key}")
    private String key;
    @Value("${sms.phone.count}")
    private Integer phoneCount;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getPhoneCount() {
        return phoneCount;
    }

    public void setPhoneCount(Integer phoneCount) {
        this.phoneCount = phoneCount;
    }

    @Override
    public String toString() {
        return "ConfigData{" +
                "key='" + key + '\'' +
                ", phoneCount=" + phoneCount +
                '}';
    }
}

