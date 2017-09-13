package com.laioin.java.frame.download.cons;

/**
 * Created with IDEA
 * User qinxiancheng
 * Date on 2016/8/11
 * Time on 14:12
 * <p/>
 * 数据格式，常量类
 */
public interface ByteKeys {
    /******************
     * 数据格式
     ***************/
    float NUM_B = 1024f;
    float DATA_KB = NUM_B;
    float DATA_M = DATA_KB * NUM_B;
    float DATA_G = DATA_M * NUM_B;
    float DATA_TB = DATA_G * NUM_B;
}
