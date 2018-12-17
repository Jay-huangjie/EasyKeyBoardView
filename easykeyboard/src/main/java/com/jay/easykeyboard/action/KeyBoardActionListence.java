package com.jay.easykeyboard.action;

import android.text.Editable;

/**
 * Created by hj on 2018/12/17.
 * 说明：键盘输入监听
 */
public interface KeyBoardActionListence {
    void onComplete(); //完成点击

    void onTextChange(Editable editable); //文本改变

    void onClear(); //正在删除

    void onClearAll(); //全部清除
}
