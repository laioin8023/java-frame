package com.laioin.java.frame.spring.boot.reflex.dto;

/**
 * Created with IDEA
 * User qinxiancheng
 * Date on 2017/4/7
 * Time on 12:02
 */
public class ResultModel {

    private int code;
    private String message;
    private Object data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public enum Type {

        NO_OBJECT(-1000, "没有找到服务对象。"),
        PARAMETERS_ERROR(-1001, "参数不匹配。"),
        NO_METHOD(-1002, "没有找到服务方法。"),
        APP_ERROR(-5000, "服务器内部错误，攻城狮正在修复。"),;

        public int code;
        public String message;

        Type(int code, String message) {
            this.code = code;
            this.message = message;
        }
    }

    /**
     * 构建错误信息
     *
     * @param type 错误枚举
     * @return
     */
    public static ResultModel buildError(ResultModel.Type type) {
        ResultModel res = new ResultModel();
        res.setCode(type.code);
        res.setMessage(type.message);
        return res;
    }

    /**
     * 构建成功信息
     *
     * @param data 返回的数据
     * @return
     */
    public static ResultModel buildSuccess(Object data) {
        ResultModel res = new ResultModel();
        res.setCode(0);
        res.setMessage("success");
        res.setData(data);
        return res;
    }
}
