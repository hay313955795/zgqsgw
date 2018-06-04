package com.backup.zgqsgw.Vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class ObjectRestResponse implements Serializable{


    private String message;
    private boolean rel;
    private Object data;
    private int status =200;
    private int errorCode;


    public ObjectRestResponse() {

    }

    public ObjectRestResponse(String message, boolean rel, int errorCode) {
        this.message = message;
        this.rel = rel;
        this.errorCode = errorCode;
    }

    public ObjectRestResponse rel(boolean rel){
        this.rel = rel;
        return this;
    }

    public ObjectRestResponse data(Object data){
        this.data = data;
        return this;
    }

    public ObjectRestResponse message(String message){
        this.message = message;
        return this;
    }

}
