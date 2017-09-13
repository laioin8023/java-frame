package com.laioin.java.frame.spring.boot.service.impl;

import com.laioin.java.frame.spring.boot.reflex.annotaion.MethodDesc;
import com.laioin.java.frame.spring.boot.service.IUserService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IDEA
 * User qinxiancheng
 * Date on 2017/9/13
 * Time on 16:00
 */
@Service("userService")
public class UserServiceImpl implements IUserService {

    @Override
    @MethodDesc(keys = {"username"})
    public Object getUserInfo(String username) {
        Map<String, Object> user = new HashMap<>();
        user.put("username", username);
        user.put("age", 10);
        user.put("phone", "13161781665");
        return user;
    }
}
