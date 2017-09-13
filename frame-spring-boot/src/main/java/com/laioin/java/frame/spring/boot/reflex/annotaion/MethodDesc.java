package com.laioin.java.frame.spring.boot.reflex.annotaion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created with IDEA
 * User qinxiancheng
 * Date on 2017/4/7
 * Time on 11:34
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MethodDesc {


    enum ResultType {
        JSON, NONE
    }

    /**
     * 表示 当前方法，需要哪些参数
     * 比如{"userId","userName"}
     * 使用：@MethodDesc(keys={"userId","userName"})
     *
     * @return
     */
    String[] keys() default {};

    /**
     * 方法的返回类型，默认是按原类型返回
     * 使用：@MethodDesc(returnType=ResultType.JSON)
     *
     * @return
     */
    ResultType returnType() default ResultType.NONE;
}
