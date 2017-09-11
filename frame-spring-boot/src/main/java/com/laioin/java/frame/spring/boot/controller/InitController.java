package com.laioin.java.frame.spring.boot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created with IDEA
 * User qinxiancheng
 * Date on 2017/9/11
 * Time on 17:29
 */
@Controller
public class InitController extends BaseController {

    /**
     * 首页
     *
     * @return
     */
    @RequestMapping("/")
    public ModelAndView toLogin() {
        ModelAndView mv = this.getModelAndView("login", null);
        return mv;
    }
}
