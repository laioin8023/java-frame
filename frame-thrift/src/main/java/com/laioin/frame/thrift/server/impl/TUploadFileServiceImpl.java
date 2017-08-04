package com.laioin.frame.thrift.server.impl;

import com.google.gson.Gson;
import com.laioin.frame.thrift.base.service.TUploadFile;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.util.Random;

/**
 * Created with IDEA
 * User qinxiancheng
 * Date on 2017/8/3
 * Time on 17:09
 * --  实现服务器的文件上传
 */
public class TUploadFileServiceImpl implements TUploadFile.Iface {

    private static final Gson gson = new Gson();
    private static final Logger LGR = LoggerFactory.getLogger(TUploadFileServiceImpl.class);

    /**
     * 文件上传
     *
     * @param file
     * @return 文件名称
     * @throws TException
     */
    @Override
    public String uploadFile(ByteBuffer file) throws TException {
        Random r = new Random();
        FileOutputStream outputStream = null;
        try {
            if (null != file) {
                byte read[] = new byte[1024 * 4];
                String fileName = System.currentTimeMillis() + "" + r.nextInt(1000);
                File outFile = new File("d:/thrift-service/file");
                if (!outFile.getParentFile().exists()) {
                    outFile.getParentFile().mkdirs(); // 创建目录
                }
                if (!outFile.exists()) {
                    outFile.mkdirs();
                }
                outputStream = new FileOutputStream(new File(outFile.getPath() + "/" + fileName));
                while (file.hasRemaining()) { // 是否还有数据
                    file.get(read);
                    outputStream.write(read);
                }
                return fileName;
            }
        } catch (Exception e) {
            LGR.error("文件上传事错误。", e);
        } finally {
            if (null != outputStream) {
                try {
                    outputStream.close();
                    file.clear(); //清空缓存
                } catch (Exception e) {
                    LGR.error("", e);
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
