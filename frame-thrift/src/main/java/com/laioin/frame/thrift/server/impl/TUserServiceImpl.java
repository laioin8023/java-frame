package com.laioin.frame.thrift.server.impl;

import com.google.gson.Gson;
import com.laioin.frame.thrift.base.model.TRegisterUserRequestParam;
import com.laioin.frame.thrift.base.model.TResultModel;
import com.laioin.frame.thrift.base.model.TUserInfoRequestParam;
import com.laioin.frame.thrift.base.service.TUserService;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

/**
 * Created with IDEA
 * User qinxiancheng
 * Date on 2017/8/3
 * Time on 16:41
 * -- 实现，服务器端，service 方法
 */
public class TUserServiceImpl implements TUserService.Iface {

    private static final Gson gson = new Gson();
    private static final Logger LGR = LoggerFactory.getLogger(TUserServiceImpl.class);

    /**
     * 获取用户信息
     *
     * @param param
     * @return
     * @throws TException
     */
    @Override
    public TResultModel getUserInfo(TUserInfoRequestParam param) throws TException {
        Random random = new Random();
        Map<String, Object> userInfo = new TreeMap<>();
        userInfo.put("userName", param.getUserName());
        userInfo.put("id", random.nextInt(100000));
        userInfo.put("phone", param.getPhone());
        userInfo.put("age", random.nextInt(100));
        String data = gson.toJson(userInfo);
        TResultModel result = new TResultModel();
        result.setCode(0).setData(data).setMessage("数据获取成功");
        LGR.info("获取用户数据：[{}]", data);
        return result;
    }

    /**
     * 用户注册
     *
     * @param param
     * @return
     * @throws TException
     */
    @Override
    public TResultModel registerUser(TRegisterUserRequestParam param) throws TException {
        Random random = new Random();
        String formatStr = "id=%d,姓名=%s,手机号=%s,年龄=%d,邮箱=%s.";
        String data = String.format(formatStr, random.nextInt(100000), param.getUserName(), param.getPhone(), param.getAge(), param.getEmail());
        LGR.info("用户注册：[{}].", data);
        System.out.println("用户注册：" + data);
        TResultModel result = new TResultModel();
        result.setCode(0).setData(data).setMessage("用户注册成功。");
        return result;
    }
}
