package com.laioin.java.frame.spring.boot.domain;

/**
 * Created with IDEA
 * User qinxiancheng
 * Date on 2017/9/5
 * Time on 11:16
 */
public class SmsDomain {

    private String key;
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
        return "SmsDomain{" +
                "key='" + key + '\'' +
                ", phoneCount=" + phoneCount +
                '}';
    }
}
