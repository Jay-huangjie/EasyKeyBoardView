package com.jay.easykeyboard;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import com.jay.easykeyboard.base.KeyBoardEditText;
import com.jay.easykeyboard.constant.Util;
import com.jay.easykeyboard.function.FormatTextWatcher;
import com.jay.easykeyboard.function.SystemOnKeyboardActionListener;

/**
 * Created by huangjie on 2018/2/4.
 * 类名：
 * 说明：附带键盘弹出的EditText
 */

public class SystemKeyBoardEditText extends KeyBoardEditText {
    private boolean enable = true;    //是否启用自定义键盘
    private boolean focuable = true; //默认获取焦点
    private boolean release = true; //主动回收
    private SystemKeyboard systemKeyboard;
    private FormatTextWatcher textWatcher;
    private SystemOnKeyboardActionListener listener;

    public SystemKeyBoardEditText(Context context) {
        this(context, null);
    }

    public SystemKeyBoardEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SystemKeyBoardEditText(final Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (focuable) {
                        requestFocus();
                        requestFocusFromTouch();
                        if (enable) {
                            Util.closeKeyboard(context, SystemKeyBoardEditText.this);
                                showKeyboardWindow();
                        }
                    } else {
                        dismissKeyboardWindow();
                        Util.closeKeyboard(context, SystemKeyBoardEditText.this);
                    }
                }
                return true;
            }
        });

        setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                  dismissKeyboardWindow();
                }
            }
        });
    }


    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SystemKeyBoardEditText);
//        boolean randomkeys = a.getBoolean(R.styleable.SystemKeyBoardEditText_randomkeys, false);
        int xmlLayoutResId = a.getResourceId(R.styleable.SystemKeyBoardEditText_xmlLayoutResId, 0);
        boolean isSpace = a.getBoolean(R.styleable.SystemKeyBoardEditText_space, false);
        systemKeyboard = new SystemKeyboard(context);
        systemKeyboard.setXmlLayoutResId(xmlLayoutResId);
//        systemKeyboard.isRandomkeys(randomkeys);
        if (a.hasValue(R.styleable.SystemKeyBoardEditText_keyViewbg)) {
            Drawable keyViewbg = a.getDrawable(R.styleable.SystemKeyBoardEditText_keyViewbg);
            systemKeyboard.setKeybgDrawable(keyViewbg);
        }
        initPopWindow(systemKeyboard);
        listener = new SystemOnKeyboardActionListener(this, getKeyboardWindow()) {
            @Override
            public void onKey(int primaryCode, int[] keyCodes) {
                super.onKey(primaryCode, keyCodes);
            }
        };
        systemKeyboard.getKeyboardView().setOnKeyboardActionListener(listener);
        if (isSpace) {
            textWatcher = new FormatTextWatcher(this);
            addTextChangedListener(textWatcher);
        }
        setCursorVisible(true);
        a.recycle();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (release) recycle();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (null != getKeyboardWindow()) {
                if (getKeyboardWindow().isShowing()) {
                    getKeyboardWindow().dismiss();
                    return true;
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    public void recycle() {
        super.recycle();
        systemKeyboard = null;
        if (null != textWatcher) {
            removeTextChangedListener(textWatcher);
        }
    }

    public void setActiveRelease(boolean release) {
        this.release = release;
    }

    public SystemKeyboard getSystemKeyboard() {
        return systemKeyboard;
    }

    public void setSpaceEnable(boolean isSpace) {
        if (isSpace) {
            if (textWatcher == null) {
                textWatcher = new FormatTextWatcher(this);
            }
            addTextChangedListener(textWatcher);
        } else {
            if (null != textWatcher) {
                removeTextChangedListener(textWatcher);
            }
        }
    }

//    public void setRandomkeysEnable(boolean randomkeys) {
//        systemKeyboard.isRandomkeys(randomkeys);
//    }

    public void setOnKeyboardActionListener(SystemOnKeyboardActionListener listener) {
        this.listener = listener;
        systemKeyboard.getKeyboardView().setOnKeyboardActionListener(listener);
    }

    /**
     * 设置焦点
     *
     * @param focuable
     */
    public void setFocuable(boolean focuable) {
        this.focuable = focuable;
        setFocusable(focuable);
        setFocusableInTouchMode(focuable);
        setCursorVisible(focuable);
    }

    public void setKeyViewBgDrawable(Drawable drawable) {
        if (systemKeyboard != null) systemKeyboard.setKeybgDrawable(drawable);
    }

}
