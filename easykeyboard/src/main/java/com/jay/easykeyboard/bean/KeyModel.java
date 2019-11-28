package com.jay.easykeyboard.bean;

/**
 * Created by hj on 2018/12/18.
 */
public class KeyModel {

    public KeyModel(int code,String label){
        this.code = code;
        this.label = label;
    }

    private String label;
    private int code;

    public String getLabel() {
        return label;
    }

    public int getCode() {
        return code;
    }
}
