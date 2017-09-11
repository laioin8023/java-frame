package com.laioin.java.frame.spring.boot;

/**
 * Created with IDEA
 * User qinxiancheng
 * Date on 2017/4/7
 * Time on 17:16
 * --->  启动类必须放到根目录下面
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

/**
 * Created with IDEA
 * User qinxiancheng
 * Date on 2017/4/7
 * Time on 17:16
 * --->  启动类必须放到根目录下面
 */
@SpringBootApplication
public class Application {

    public static void main(String args[]){
        SpringApplication.run(Application.class, args);
    }
}