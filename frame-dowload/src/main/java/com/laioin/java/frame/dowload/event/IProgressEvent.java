package com.laioin.java.frame.dowload.event;

import com.laioin.java.frame.dowload.model.FileBean;

/**
 * Created with IDEA
 * User qinxiancheng
 * Date on 2016/8/11
 * Time on 11:28
 *
 * 下载进度事件
 */
public interface IProgressEvent {

    /**
     * 下载进度，回调，没秒一次
     * @param fileInfo  返回的，文件信息 ，如下载速度等
     */
    void changeProgressEvent(FileBean fileInfo);
}
