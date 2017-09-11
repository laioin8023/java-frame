package com.laioin.java.frame.spring.boot.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * Created with IDEA
 * User qinxiancheng
 * Date on 2017/9/11
 * Time on 17:25
 */
public class BaseController {

    @Value("${server.context-path}")
    private String contextPath;

    /**
     * 获取 model and view
     *
     * @param page 页面
     * @param data 需要传递的数据
     * @return
     */
    protected ModelAndView getModelAndView(String page, Map<String, Object> data) {
        ModelAndView mv = new ModelAndView(page);
        mv.addObject("contextPath", contextPath);
        if (null != data) {
            // 添加，数据
            for (String key : data.keySet()) {
                mv.addObject(key, data.get(key));
            }
        }
        return mv;
    }
}
