package com.laioin.java.frame.dowload.cons;

/**
 * Created with IDEA
 * User qinxiancheng
 * Date on 2016/8/11
 * Time on 13:47
 * <p/>
 * 常量
 */
public interface HttpHeadKeys {

    /**
     * 返回数据的长度
     */
    String CONTENT_LENGTH = "Content-Length";
    /**
     * 请求头：数据范围
     * 表示头500个字节：bytes=0-499
     * 表示第二个500字节：bytes=500-999
     * 表示最后500个字节：bytes=-500
     * 表示500字节以后的范围：bytes=500-
     * 第一个和最后一个字节：bytes=0-0,-1
     * 同时指定几个范围：bytes=500-600,601-999
     */
    String RANGE = "range";

    /**
     * range 范围：某一个数字以后所有数据
     */
    String RANGE_VAL_AFTER = "bytes=%s-";

}
