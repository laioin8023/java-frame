package com.laioin.java.frame.spring.boot.reflex.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.Map;

/**
 * Created with IDEA
 * User qinxiancheng
 * Date on 2017/4/7
 * Time on 11:59
 */
public class PassUtil {

    /**
     * 根据，参数列表，获取某一个参数值
     *
     * @param type       参数类型，比如int ，string
     * @param key        参数名称
     * @param parameters 参数列表
     * @return
     */
    public static Object getParamValue(Class type, String key, Map<String, Object> parameters) {
        Object val = null;
        Object data = parameters.get(key);
        if (data instanceof String) {
            if (int.class == type || Integer.class == type) {
                val = Integer.parseInt((String) data);
            } else if (byte.class == type || Byte.class == type) {
                val = Byte.parseByte((String) data);
            } else if (double.class == type || Double.class == type) {
                val = Double.parseDouble((String) data);
            } else if (BigDecimal.class == type) {
                val = new BigDecimal((String) data);
            } else if (boolean.class == type || Boolean.class == type) {
                String itemData = (String) data;
                itemData = itemData.toUpperCase();
                if ("true".equals(data) || "false".equals(data)) {
                    val = Boolean.parseBoolean(itemData);
                } else {
                    val = "0".equals(itemData) ? false : true;
                }
            } else if (long.class == type || Long.class == type) {
                val = Long.parseLong((String) data);
            } else if (short.class == type || Short.class == type) {
                val = Short.parseShort((String) data);
            } else if (float.class == type || Float.class == type) {
                val = Float.parseFloat((String) data);
            } else {
                val = data;
            }
        } else if (HttpServletRequest.class == type) {
            val = parameters.get(StaticKeys.U_REQUEST);
        } else if (HttpServletResponse.class == type) {
            val = parameters.get(StaticKeys.U_RESPONSE);
        } else if (HttpSession.class == type) {
            val = parameters.get(StaticKeys.U_SESSION);
        } else {
            val = data;
        }
        return val;
    }
}
