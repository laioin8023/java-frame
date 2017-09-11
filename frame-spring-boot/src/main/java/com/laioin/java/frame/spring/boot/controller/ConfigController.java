package com.laioin.java.frame.spring.boot.controller;

import com.laioin.java.frame.spring.boot.config.ConfigData;
import com.laioin.java.frame.spring.boot.domain.SmsDomain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created with IDEA
 * User qinxiancheng
 * Date on 2017/9/4
 * Time on 17:40
 */
@Controller
@RequestMapping("/app/config")
public class ConfigController {

    @Value("${m.test.val}")
    private String testVal;
    @Autowired
    private ConfigData configData;

    /**
     * 获取 application 配置，文件里的信息
     *
     * @return
     */
    @RequestMapping("/test")
    @ResponseBody
    public String getTestVal() {
        return testVal;
    }

    /**
     * 获取自己 配置文件里的值
     *
     * @return
     */
    @RequestMapping("/data")
    @ResponseBody
    public SmsDomain getConfigData() {
        SmsDomain smsDomain = new SmsDomain();
        smsDomain.setKey(configData.getKey());
        smsDomain.setPhoneCount(configData.getPhoneCount());
        return smsDomain;
    }


}
