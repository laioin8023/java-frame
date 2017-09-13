package com.laioin.java.frame.spring.boot.reflex.content;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Created with IDEA
 * User qinxiancheng
 * Date on 2017/4/7
 * Time on 10:42
 */
@Component
public class SpringContent implements ApplicationContextAware {

    private static ApplicationContext context;

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContent.context = applicationContext;
    }

    public static Object getBean(String name) {
        Object obj = null;
        try {
            obj = SpringContent.context.getBean(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }
}
