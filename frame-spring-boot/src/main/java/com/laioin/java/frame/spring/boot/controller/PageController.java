package com.laioin.java.frame.spring.boot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IDEA
 * User qinxiancheng
 * Date on 2017/9/11
 * Time on 16:13
 */
@Controller
public class PageController extends BaseController {

    @RequestMapping("/user/login")
    public ModelAndView login(String username) {
        Map<String, Object> data = new HashMap<>();
        data.put("username", username);
        ModelAndView mv = this.getModelAndView("success", data);
        return mv;
    }
}
