package com.baayso.bms;

import java.io.Serializable;

/**
 * 返回给客户端的操作结果（转换成json格式后返回给客户端）。
 * 
 * @author ChenFangjie
 *
 * @since 1.0.0
 * 
 * @version 1.0.0
 * 
 */
public class OperationResult implements Serializable {

    private static final long serialVersionUID = -3683492506985584314L;

    private boolean status;
    private int statusCode;
    private Object message;
    private Object data;

    public OperationResult() {
    }

    public OperationResult(boolean status, int statusCode) {
        this.status = status;
        this.statusCode = statusCode;
    }

    public OperationResult(boolean status, int statusCode, Object data) {
        this.status = status;
        this.statusCode = statusCode;
        this.data = data;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OperationResult [status=");
        builder.append(status);
        builder.append(", statusCode=");
        builder.append(statusCode);
        builder.append(", message=");
        builder.append(message);
        builder.append(", data=");
        builder.append(data);
        builder.append("]");
        return builder.toString();
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
