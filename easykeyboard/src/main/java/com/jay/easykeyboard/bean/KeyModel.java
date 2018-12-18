package com.jay.easykeyboard.bean;

/**
 * Created by hj on 2018/12/18.
 */
public class KeyModel {

    public KeyModel(int code,String lable){
        this.code = code;
        this.lable = lable;
    }

    private String lable;
    private int code;

    public String getLable() {
        return lable;
    }

    public int getCode() {
        return code;
    }
}
