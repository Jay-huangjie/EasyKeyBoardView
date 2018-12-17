package com.jay.easykeyboard.impl;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Created by huangjie on 2018/2/5.
 * 类名：
 * 说明：4位空格实现类
 */

public class FormatTextWatcher implements TextWatcher {

    private final EditText editText;

    public FormatTextWatcher(EditText editText){
        this.editText = editText;
    }

    //间隔位数
    private int unit = 4;
    //间隔符
    private String tag = " ";
    private int beforeTextLength = 0;
    private int afterTextLength = 0;
    private int location = 0;//记录光标的位置
    private boolean isChanging = false;// 是否更换中

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        beforeTextLength = s.length();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        afterTextLength = s.length();
        if (isChanging)
            return;

        if (beforeTextLength < afterTextLength) {// 字符增加
            location = editText.getSelectionEnd();
            setFormatText(s.toString());
            if (location % (unit + 1) == 0) {
                editText.setSelection(getLocation(location + 1));
            } else {
                editText.setSelection(getLocation(location));
            }
        } else if (beforeTextLength > afterTextLength) {// 字符减少
            location = editText.getSelectionEnd();
            setFormatText(s.toString());
            if (location % (unit + 1) == 0) {
                editText.setSelection(getLocation(location - 1));
            } else {
                editText.setSelection(getLocation(location));
            }
        }
    }


    private int getLocation(int location) {
        if (location < 0)
            return 0;
        if (location > afterTextLength) {
            return afterTextLength;
        }
        return location;
    }

    private void setFormatText(String str) {
        isChanging = true;
        editText.setText(addTag(str));
        isChanging = false;
    }

    /**
     * 加上标识符
     *
     * @param str
     * @return
     */
    private String addTag(String str) {
        str = replaceTag(str);
        StringBuilder sb = new StringBuilder();
        int index = 0;
        int strLength = str.length();
        while (index < strLength) {
            int increment = index + unit;
            sb.append(str.subSequence(index, index = (increment > strLength) ? strLength : increment));
            if (increment < strLength) {
                sb.append(tag);
            }
        }
        return sb.toString();
    }

    /**
     * 替换标识符为空格
     *
     * @param str
     * @return
     */
    private String replaceTag(String str) {
        if (str.contains(tag)) {
            str = str.replace(tag, "");
        }
        return str;
    }
}
