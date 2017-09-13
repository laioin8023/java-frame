package com.laioin.java.frame.download.down;

import com.laioin.java.frame.download.cons.HttpHeadKeys;
import com.laioin.java.frame.download.cons.Keys;
import com.laioin.java.frame.download.event.IProgressEvent;
import com.laioin.java.frame.download.model.FileBean;
import com.laioin.java.frame.download.util.Utils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IDEA
 * User qinxiancheng
 * Date on 2016/8/11
 * Time on 11:35
 */
public class DownloadManage {

    private static Logger LGR = LoggerFactory.getLogger(DownloadManage.class);

    private long downSize = 0; // 文件当前下载了多少字节b
    private long fileSize = 0; // 文件一共有多少字节
    private String formatFileSize; // 文件一共有多少 如： 10.11 G
    private String formatDownSize; // 文件下载了多少 如： 4.11 M
    private String fileName; // 文件名
    private String fileExtName; // 文件扩展名，后缀
    private boolean isNotFirst = false; // 是否是第一次，下载
    private FileBean fileBean; // 文件信息
    private RandomAccessFile saveFile; // 保存的文件
    private boolean isBreakDown = false; // 中断下载
    private long downSpeed; // 下载速度  每秒
    private String downRatio; // 下载百分比
    private long downStartDate; // 每一秒，下载开始时间
    private long downStartByte = 0; // 每一秒下载开始的字节数
    private int resetCount = 10;  // 出现异常时，重试多少次，一次  1 秒
    private int errCount = 0; // 连续几次异常
    private long downDate; // 还剩余多少时间下完


    private IProgressEvent progressEvent; // 进度事件
    private Map<String, String> headInfoMap = new ConcurrentHashMap<String, String>(); // http 返回信息

    public DownloadManage(IProgressEvent progressEvent) {
        this.progressEvent = progressEvent;
    }

    /**
     * 下载 文件
     *
     * @param url      远程文件url
     * @param savePath 保存到本地的路径
     */
    public void download(String url, String savePath) {
        HttpClient client = HttpClients.createDefault();
        HttpGet get = new HttpGet(url);
        if (isNotFirst) {  // 如果不是第一次下载
            get.setHeader(HttpHeadKeys.RANGE,
                    String.format(HttpHeadKeys.RANGE_VAL_AFTER, downSize));  //1517087056
            LGR.info("下载重试：" + String.format(HttpHeadKeys.RANGE_VAL_AFTER, downSize));
        }
        try {
            HttpResponse response = client.execute(get);
            this.checkAndInit(response, url, savePath); // 检查数据，和初始化
            InputStream is = response.getEntity().getContent(); // 文件输入流
            int readBufferSize = 2048;  // 一次读取的大小
            byte[] buffer = new byte[readBufferSize];
            int readSize;  // 当前读取到的，字节数
            downStartDate = System.currentTimeMillis();

            while ((readSize = is.read(buffer, 0, readBufferSize)) > 0) {
                saveFile.write(buffer, 0, readSize);  // 写入文件
                this.downSize += readSize; // 已经，加下载了多少数据
                long writeDate = System.currentTimeMillis();
                if ((writeDate - downStartDate) > Keys.DATE_A_SECOND) {
                    this.calcDownInfo(writeDate); // 每秒计算一次下载信息
                }
            }
            if (downSize == fileSize) { // 如果下载完成，刷新一次
                this.calcDownInfo(System.currentTimeMillis());
                LGR.info("文件[{}].下载完成，保持至[{}].", url, savePath);
            }
            is.close(); // 关闭输出流
            errCount = 0; // 如果下载了一次，
        } catch (Exception e) {
            // 下载过程中遇到异常，继续下载，直到下载完成
            if (!isBreakDown) {  // 如果不中断下载，则继续下载
                this.reDownload(url, savePath);
            }
            LGR.error("", e);
        }
    }


    /**
     * 重新下载
     *
     * @param url      url
     * @param savePath 保存地址
     */
    private void reDownload(String url, String savePath) {
        try {
            if (errCount == resetCount) {
                LGR.info("下载失败...");
                return;  // 如果超出重试返回，则完成下载
            }
            LGR.info("重新连接下载，请稍后。。。");
            errCount++;
            Thread.sleep(Keys.DATE_A_SECOND);
            this.download(url, savePath);
        } catch (Exception threadExp) {
            LGR.error("", threadExp);
        }
    }


    /**
     * 计算下载信息
     *
     * @param writeDate 每次写的时间
     */
    private void calcDownInfo(long writeDate) {
        // 一秒之内下载了多少 k
        this.downSpeed = downSize - downStartByte;
        // 每一秒计算，一次
        this.downRatio = Utils.formatRatio(downSize, fileSize); // 下载百分比
        //计算还有多少秒，下载完成。
        this.downDate = (fileSize - downSize) / downSpeed;
        this.formatDownSize = Utils.formatByte(downSize);
        this.progressEvent.changeProgressEvent(this.getFileBean());
        downStartDate = writeDate; // 改成，当前时间
        downStartByte = downSize; // 当前已下载多少
    }

    /**
     * 初始化，和，检查数据
     *
     * @param response http 返回
     * @param url      文件url
     * @param savePath 保存地址
     * @throws Exception
     */
    private void checkAndInit(HttpResponse response, String url, String savePath) throws Exception {
        if (!isNotFirst) { //如果是第一次下载，加载 头信息
            fileBean = new FileBean();
            this.setHeadInfo(response);
            this.setFileNameAndExtName(url);  // 文件 名称
            if (StringUtils.isEmpty(savePath)) {
                isBreakDown = true; // 中断下载
                Exception exception = new FileNotFoundException(String.format("文件地址 [%s] 找不到", savePath));
                LGR.error("", exception);
                throw exception;
            }
            saveFile = new RandomAccessFile(savePath, "rw"); // 读写
            isNotFirst = true; // 表示 第一次 下载
        } else {
            saveFile.seek(downSize);  // 移动文件指针到，上次下载完成的地方
        }
    }

    /**
     * 把返回头信息，写入 内存里
     *
     * @param response
     */
    private void setHeadInfo(HttpResponse response) {
        Header[] headers = response.getAllHeaders();
        for (Header item : headers) {
            LGR.info(item.toString());
            headInfoMap.put(item.getName(), item.getValue());
        }
        if (headInfoMap.get(HttpHeadKeys.CONTENT_LENGTH) != null) {
            fileSize = Long.parseLong(headInfoMap.get(HttpHeadKeys.CONTENT_LENGTH));
            formatFileSize = Utils.formatByte(fileSize);
        }
    }

    /**
     * 获取文件信息
     *
     * @return
     */
    public FileBean getFileBean() {
        fileBean.setDownSize(downSize);
        fileBean.setFileExtName(fileExtName);
        fileBean.setFileName(fileName);
        fileBean.setFileSize(fileSize);
        fileBean.setFormatDownSize(formatDownSize);
        fileBean.setFormatFileSize(formatFileSize);
        fileBean.setDownSpeed(downSpeed);
        fileBean.setDownRatio(downRatio);
        fileBean.setDownDate(downDate);
        return fileBean;
    }

    /**
     * 根据，下载地址，获取文件的，名称 和 扩展名
     *
     * @param url
     */
    private void setFileNameAndExtName(String url) {
        int idxPos = url.lastIndexOf("/");
        if (idxPos == -1) {
            idxPos = url.lastIndexOf("\\");
        }
        fileName = url.substring(idxPos + 1);
        if (!StringUtils.isEmpty(fileName)) {
            if (fileName.indexOf(".") != -1) {
                fileExtName = fileName.substring(fileName.indexOf("."));
            }
        }
    }

    public static void main(String[] args) {
        DownloadManage m = new DownloadManage(new IProgressEvent() {
            public void changeProgressEvent(FileBean bean) {
                String fs = "文件大小：{%s} , 已下载: {%s} , 下载百分比：{%s} , 下载速度：{%s} ， 剩余下载时间：{%s}";
                LGR.info(String.format(fs, bean.getFormatFileSize(), bean.getFormatDownSize(), bean.getDownRatio(),
                        bean.getFormatDownSpeed(), bean.getFormatDownDate()));
            }
        });
        m.download("http://192.168.1.222//song/DiskA/100237111/out.ts", "D:\\tmp\\t.s");
        //m.download("http://192.168.1.222//song/DiskA/100237111/output.ts", "D:\\tmp\\t.s");
        //m.download("http://192.168.1.222/imgs/vodiso/VirtualBox-5.0.10-104061-Win.exe", "D:\\tmp\\VirtualBox-5.0.10-104061-Win.exe");
        LGR.info(m.getFileBean().toString());
    }
}
