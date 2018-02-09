package com.jay.easykeyboard.keyboard;

/**
 * Created by huangjie on 2018/2/3.
 * 类名：
 * 说明：
 */

public class KeyModel {
    private Integer code;
    private String label;

    public KeyModel(Integer code, String lable) {
        this.code = code;
        this.label = lable;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getLable() {
        return label;
    }

    public void setLabel(String lable) {
        this.label = lable;
    }
}
