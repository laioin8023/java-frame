package com.laioin.java.frame.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

/**
 * Created with IDEA
 * User qinxiancheng
 * Date on 2017/7/31
 * Time on 17:46
 */
public class Util {
    private static Logger LGR = LoggerFactory.getLogger(Util.class);

    /**
     * 获取配置信息
     *
     * @return
     */
    public static Properties getConfByClassPath(String classPath) {
        Properties props = new Properties();
        try {  // 加载配置文件
            //ConfigUtils.loadProperties(props, IConstKeys.PROP_KAFKA_PATH);
            InputStream is = Util.class.getResourceAsStream(classPath);
            props.load(is); // 加载配置文件
        } catch (Exception e) {
            LGR.error("", e);
        }
        LGR.info("加载配置文件，[{}].", props);
        return props;
    }
}
