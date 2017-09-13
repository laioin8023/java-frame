package com.laioin.java.frame.spring.boot.reflex.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IDEA
 * User qinxiancheng
 * Date on 2017/4/7
 * Time on 16:42
 */
public class RequestUtil {

    /**
     * 获取request 里的参数
     *
     * @param request
     * @return
     */
    public static Map<String, Object> getParameters(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        Map<String, String[]> requestData = request.getParameterMap();
        for (String key : requestData.keySet()) {
            String item[] = requestData.get(key);
            if (item.length == 1) {
                parameters.put(key, item[0]);
            } else if (item.length > 1) {
                parameters.put(key, item);
            }
        }
        parameters.put(StaticKeys.U_REQUEST, request);
        parameters.put(StaticKeys.U_RESPONSE, response);
        parameters.put(StaticKeys.U_SESSION, request.getSession());
        return parameters;
    }
}
