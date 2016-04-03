package com.baayso.bms.common.db.exception;

/**
 * 用于将检查型异常转化为运行时异常
 * 
 * @author ChenFangjie
 * 
 */
public class DBException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DBException() {
        super();
    }

    public DBException(String message) {
        super(message);
    }

    public DBException(Throwable cause) {
        super(cause);
    }

    public DBException(String message, Throwable cause) {
        super(message, cause);
    }

}
