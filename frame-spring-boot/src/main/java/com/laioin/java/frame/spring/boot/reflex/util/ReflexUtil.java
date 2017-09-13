package com.laioin.java.frame.spring.boot.reflex.util;


import com.laioin.java.frame.spring.boot.reflex.annotaion.MethodDesc;
import com.laioin.java.frame.spring.boot.reflex.content.SpringContent;
import com.laioin.java.frame.spring.boot.reflex.dto.ResultModel;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.DirectFieldAccessor;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created with IDEA
 * User qinxiancheng
 * Date on 2017/4/7
 * Time on 10:57
 */
public class ReflexUtil {

    /**
     * 根据类对象，获取 某一个方法对象
     *
     * @param clazz      类对象
     * @param methodName 方法名称
     * @return
     */
    public static Method getMethod(Class<?> clazz, String methodName) {
        for (Method item : clazz.getDeclaredMethods()) {
            if (item.getName().equals(methodName)) {
                return item;
            }
        }
        clazz = clazz.getSuperclass();  // 跳过
        if (null == clazz || clazz == Object.class || clazz.isInterface()) {
            return null;
        }
        return ReflexUtil.getMethod(clazz, methodName);
    }

    /**
     * 根据 object 里获取 class
     *
     * @param obj object
     * @return
     */
    public static Class<?> getClassBySpring(Object obj) {
        Class<?> targetClazz = null;
        if (AopUtils.isJdkDynamicProxy(obj)) {
            AdvisedSupport advised = (AdvisedSupport) new DirectFieldAccessor(obj).getPropertyValue("advised");
            targetClazz = advised.getTargetClass();
        } else {
            targetClazz = AopUtils.getTargetClass(obj);
        }
        return targetClazz;
    }

    /**
     * 获取，类对象
     *
     * @param className
     * @return
     */
    public static Class<?> loadClass(String className) {
        Class<?> clazz = null;
        try {
            clazz = Class.forName(className);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clazz;
    }

    /**
     * 获取参数的列表，
     *
     * @param method     方法对象
     * @param methodDesc 方法描述信息
     * @param parameters 传入的值
     * @return
     */
    public static Object getMethodParameters(Method method, MethodDesc methodDesc, Map<String, Object> parameters) {
        Class<?> methodParamsType[] = method.getParameterTypes(); // 方法中，所有参数的类型
        String methodParamKeys[] = methodDesc.keys();  // 方法中，所有的参数名称
        int paramLen = methodParamsType.length;
        if (null == methodParamKeys || paramLen != methodParamKeys.length) {
            return ResultModel.buildError(ResultModel.Type.PARAMETERS_ERROR);
        }
        Object params[] = new Object[paramLen];  // 存放，方法的参数
        for (int i = 0; i < paramLen; i++) {
            Object val = PassUtil.getParamValue(methodParamsType[i], methodParamKeys[i], parameters);
            params[i] = val;
        }
        return params;
    }

    /**
     * 通过反射，根据，serviceName，和 方法名称，执行并且返回结果
     *
     * @param serviceName 服务名称
     * @param methodName  方法名称
     * @param parameters  参数列表
     * @return
     */
    public static ResultModel invoke(String serviceName, String methodName, Map<String, Object> parameters) {
        ResultModel res = null;
        try {
            // 在 spring 里获取 类对象
            Object obj = SpringContent.getBean(serviceName);
            if (null == obj) {
                return ResultModel.buildError(ResultModel.Type.NO_OBJECT);
            }
            Class<?> clazz = ReflexUtil.getClassBySpring(obj); // 获取类对象;
            Method method = ReflexUtil.getMethod(clazz, methodName); // 获取方法
            if (null == method) {
                return ResultModel.buildError(ResultModel.Type.NO_METHOD);
            }

            MethodDesc methodDesc = method.getAnnotation(MethodDesc.class); // 获取方法上的注解
            Object methodParamsInfo = ReflexUtil.getMethodParameters(method, methodDesc, parameters);  // 获取方法参数
            if (methodParamsInfo instanceof ResultModel) {
                return (ResultModel) methodParamsInfo;  //  获取方法参数时出现的错误
            }
            Object[] methodParams = (Object[]) methodParamsInfo;
            Object data = method.invoke(obj, methodParams);  // 执行方法并返回结果
            res = ResultModel.buildSuccess(data);
        } catch (Exception e) {
            e.printStackTrace();
            res = ResultModel.buildError(ResultModel.Type.APP_ERROR);
        }
        return res;
    }
}
