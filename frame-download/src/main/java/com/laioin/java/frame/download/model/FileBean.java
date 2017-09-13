package com.laioin.java.frame.download.model;

import com.laioin.java.frame.download.util.Utils;

/**
 * Created with IDEA
 * User qinxiancheng
 * Date on 2016/8/11
 * Time on 14:58
 */
public class FileBean {
    private long downSize = 0; // 文件当前下载了多少字节b
    private long fileSize = 0; // 文件一共有多少字节
    private String formatFileSize; // 文件一共有多少 如： 10.11 G
    private String formatDownSize; // 文件下载了多少 如： 4.11 M
    private String fileName; // 文件名
    private String fileExtName; // 文件扩展名，后缀
    private String downRatio; // 下载百分比
    private long downSpeed; // 下载速度  每秒
    private long downDate; // 还剩余多少时间下完 秒

    /**
     * 获取，格式化后的，剩余下载时间
     *
     * @return
     */
    public String getFormatDownDate() {
        return Utils.formatDate(this.downDate);
    }

    /**
     * 获取，格式化后的，下载速度
     *
     * @return
     */
    public String getFormatDownSpeed() {
        return Utils.formatByte(this.downSpeed);
    }

    public long getDownDate() {
        return downDate;
    }

    public void setDownDate(long downDate) {
        this.downDate = downDate;
    }

    public long getDownSpeed() {
        return this.downSpeed;
    }

    public void setDownSpeed(long downSpeed) {
        this.downSpeed = downSpeed;
    }

    public String getDownRatio() {
        return downRatio;
    }

    public void setDownRatio(String downRatio) {
        this.downRatio = downRatio;
    }

    public long getDownSize() {
        return downSize;
    }

    public long getFileSize() {
        return fileSize;
    }

    public String getFormatFileSize() {
        return formatFileSize;
    }

    public String getFormatDownSize() {
        return formatDownSize;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileExtName() {
        return fileExtName;
    }

    public void setDownSize(long downSize) {
        this.downSize = downSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public void setFormatFileSize(String formatFileSize) {
        this.formatFileSize = formatFileSize;
    }

    public void setFormatDownSize(String formatDownSize) {
        this.formatDownSize = formatDownSize;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFileExtName(String fileExtName) {
        this.fileExtName = fileExtName;
    }

    @Override
    public String toString() {
        return "FileBean{" +
                "downSize=" + downSize +
                ", fileSize=" + fileSize +
                ", formatFileSize='" + formatFileSize + '\'' +
                ", formatDownSize='" + formatDownSize + '\'' +
                ", fileName='" + fileName + '\'' +
                ", fileExtName='" + fileExtName + '\'' +
                ", downRatio='" + downRatio + '\'' +
                ", downSpeed=" + downSpeed +
                ", downDate='" + downDate + '\'' +
                '}';
    }
}
