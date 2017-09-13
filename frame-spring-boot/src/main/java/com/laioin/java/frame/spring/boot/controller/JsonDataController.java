package com.laioin.java.frame.spring.boot.controller;

import com.laioin.java.frame.spring.boot.reflex.dto.ResultModel;
import com.laioin.java.frame.spring.boot.reflex.util.ReflexUtil;
import com.laioin.java.frame.spring.boot.reflex.util.RequestUtil;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created with IDEA
 * User qinxiancheng
 * Date on 2017/4/7
 * Time on 19:05
 */
@RestController
@RequestMapping("/json/data")
public class JsonDataController {


    /**
     * 根据 服务名称，和方法，获取json 数据
     *
     * @param serviceName 服务名称
     * @param methodName  方法名称
     * @param request
     * @return
     */
    @RequestMapping("/{serviceName}/{methodName}")
    public ResultModel getUserInfo(@PathVariable("serviceName") String serviceName,
                                   @PathVariable("methodName") String methodName,
                                   HttpServletRequest request,
                                   HttpServletResponse response) {
        // 获取参数列表
        Map<String, Object> parameters = RequestUtil.getParameters(request, response);
        ResultModel res = ReflexUtil.invoke(serviceName, methodName, parameters);  //执行服务返回结果
        return res;
    }
}
