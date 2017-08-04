package com.laioin.frame.thrift.spring;

/**
 * Created with IDEA
 * User qinxiancheng
 * Date on 2017/8/4
 * Time on 14:19
 */
public class ThriftException extends RuntimeException {


    public ThriftException() {
        super();
    }

    public ThriftException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public ThriftException(String message) {
        super(message);
    }

    public ThriftException(Throwable throwable) {
        super(throwable);
    }
}
