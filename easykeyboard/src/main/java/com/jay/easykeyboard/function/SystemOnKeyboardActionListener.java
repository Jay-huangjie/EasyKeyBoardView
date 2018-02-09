package com.jay.easykeyboard.function;

import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.Editable;
import android.widget.EditText;
import android.widget.PopupWindow;

/**
 * Created by huangjie on 2018/2/3.
 * 类名：
 * 说明：实现该类可单独实现需要的方法,内部实现内容添加
 */

public class SystemOnKeyboardActionListener implements KeyboardView.OnKeyboardActionListener {

    private EditText editText;
    private PopupWindow popupWindow;

    public SystemOnKeyboardActionListener(){};

    public SystemOnKeyboardActionListener(EditText editText) {
        this.editText = editText;
    }

    public SystemOnKeyboardActionListener(EditText editText, PopupWindow popupWindow) {
        this.editText = editText;
        this.popupWindow = popupWindow;
    }

    public void setEditText(EditText editText) {
        this.editText = editText;
    }

    public void setPopupWindow(PopupWindow popupWindow) {
        this.popupWindow = popupWindow;
    }

    @Override
    public void onPress(int primaryCode) {

    }

    @Override
    public void onRelease(int primaryCode) {

    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        if (null != editText) {
            Editable editable = editText.getText();
            int start = editText.getSelectionStart();
            if (primaryCode == Keyboard.KEYCODE_DONE) {// 隐藏键盘
                if (null != popupWindow && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
            } else if (primaryCode == Keyboard.KEYCODE_DELETE) {// 回退
                if (editable != null && editable.length() > 0) {
                    if (start > 0) {
                        editable.delete(start - 1, start);
                    }
                }
            } else if (primaryCode == 152) {
                editable.clear();
            } else {
                editable.insert(start, Character.toString((char) primaryCode));
            }
        }
    }

    @Override
    public void onText(CharSequence text) {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }
}
