package com.jay.easykeyboard.impl;

import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.Editable;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.jay.easykeyboard.action.KeyBoardActionListener;

/**
 * Created by huangjie on 2018/2/3.
 * 类名：
 * 说明：键盘原生接口实现类，可继承重写
 */

public class SystemOnKeyboardActionListener implements KeyboardView.OnKeyboardActionListener {

    private EditText editText;
    private PopupWindow popupWindow;
    private KeyBoardActionListener mActionListener;

    public void setEditText(EditText editText) {
        this.editText = editText;
    }

    public void setPopupWindow(PopupWindow popupWindow) {
        this.popupWindow = popupWindow;
    }

    public void setKeyActionListener(KeyBoardActionListener listener){
        this.mActionListener = listener;
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
                mActionListener.onComplete();
            } else if (primaryCode == Keyboard.KEYCODE_DELETE) {// 回退
                if (editable != null && editable.length() > 0) {
                    if (start > 0) {
                        editable.delete(start - 1, start);
                    }
                }
                mActionListener.onClear();
            } else if (primaryCode == 152) {
                editable.clear();
                mActionListener.onClearAll();
            }else {
                editable.insert(start, Character.toString((char) primaryCode));
                mActionListener.onTextChange(editable);
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
