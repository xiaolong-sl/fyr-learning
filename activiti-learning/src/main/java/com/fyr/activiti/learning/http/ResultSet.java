package com.fyr.activiti.learning.http;

import java.io.Serializable;

public class ResultSet<T> implements Serializable {
    private int retCode;
    private String retMessage;
    private T data;

    public ResultSet(int retCode, String retMessage, T data) {
        this.retCode = retCode;
        this.retMessage = retMessage;
        this.data = data;
    }

    public int getRetCode() {
        return retCode;
    }

    public void setRetCode(int retCode) {
        this.retCode = retCode;
    }

    public String getRetMessage() {
        return retMessage;
    }

    public void setRetMessage(String retMessage) {
        this.retMessage = retMessage;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
