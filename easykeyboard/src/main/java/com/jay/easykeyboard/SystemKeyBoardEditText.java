package com.jay.easykeyboard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.jay.easykeyboard.action.KeyBoardActionListence;
import com.jay.easykeyboard.action.OnEditFocusChangeListence;
import com.jay.easykeyboard.widget.KeyBoardEditText;
import com.jay.easykeyboard.util.Util;
import com.jay.easykeyboard.impl.FormatTextWatcher;
import com.jay.easykeyboard.impl.SystemOnKeyboardActionListener;

/**
 * Created by huangjie on 2018/2/4.
 * 类名：
 * 说明：附带键盘弹出的EditText
 */

public class SystemKeyBoardEditText extends KeyBoardEditText {
    private boolean enable = true;    //是否启用自定义键盘
    private boolean focuable = true; //默认获取焦点
    private boolean outSideable = false; //点击外部区域是否隐藏键盘
    private SystemKeyboard systemKeyboard;
    private FormatTextWatcher textWatcher;
    private SystemOnKeyboardActionListener listener;
    private OnEditFocusChangeListence focusChangeListence;
    private int FOCUSTAB;

    private static final int READY = 0X110;
    private static final int STAR = 0X111;
    private int STATUE;
    private Handler mHandler = new Handler();

    public SystemKeyBoardEditText(Context context) {
        this(context, null);
    }

    public SystemKeyBoardEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SystemKeyBoardEditText(final Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
        initListence();
    }

    private void initListence() {
        setOnTouchListener(new OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!isShowing()) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        if (focuable) {
                            requestFocus();
                            requestFocusFromTouch();
                            if (enable) {
                                Util.disableShowSoftInput(SystemKeyBoardEditText.this);
                                showKeyboardWindow();
                            }
                        } else {
                            dismissKeyboardWindow();
                            Util.disableShowSoftInput(SystemKeyBoardEditText.this);
                        }
                    }
                } else {
                    requestFocus();
                    requestFocusFromTouch();
                }
                return false;
            }
        });
        STATUE = READY;
        setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, final boolean hasFocus) {
                //根据焦点变化判断外部点击区域
                FOCUSTAB++;
                if (STATUE == READY) {
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (STATUE == STAR) {
                                if (FOCUSTAB == 1 && !hasFocus && outSideable) {
                                    dismissKeyboardWindow();
                                }
                                STATUE = READY;
                                FOCUSTAB = 0;
                            }
                        }
                    }, 200);
                    STATUE = STAR;
                }
                if (focusChangeListence != null) {
                    focusChangeListence.OnFocusChangeListener(v, hasFocus);
                }
            }
        });
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SystemKeyBoardEditText);
        boolean randomkeys = a.getBoolean(R.styleable.SystemKeyboard_isRandom, false);
        int xmlLayoutResId = a.getResourceId(R.styleable.SystemKeyBoardEditText_xmlLayoutResId, 0);
        boolean isSpace = a.getBoolean(R.styleable.SystemKeyBoardEditText_space, false);
        outSideable = a.getBoolean(R.styleable.SystemKeyBoardEditText_outSideCancel, false);
        systemKeyboard = new SystemKeyboard(context);
        systemKeyboard.setXmlLayoutResId(xmlLayoutResId);
        if (a.hasValue(R.styleable.SystemKeyBoardEditText_keyViewbg)) {
            Drawable keyViewbg = a.getDrawable(R.styleable.SystemKeyBoardEditText_keyViewbg);
            systemKeyboard.setKeybgDrawable(keyViewbg);
        }
        initPopWindow(systemKeyboard);
        listener = new SystemOnKeyboardActionListener();
        listener.setEditText(this);
        listener.setPopupWindow(getKeyboardWindow());
        systemKeyboard.getKeyboardView().setOnKeyboardActionListener(listener);
        if (isSpace) {
            textWatcher = new FormatTextWatcher(this);
            addTextChangedListener(textWatcher);
        }
        if (randomkeys) {
            systemKeyboard.setRandomkeys(true);
        }
        setCursorVisible(true);
        a.recycle();
    }


    public SystemKeyboard getSystemKeyboard() {
        return systemKeyboard;
    }

    /**
     * 是否加入4位空格功能
     *
     * @param isSpace true
     */
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


    /**
     * 设置键盘输入监听
     *
     * @param listener listence
     */
    public void setOnKeyboardActionListener(KeyBoardActionListence listener) {
        this.listener.setKeyActionListence(listener);
    }

    /**
     * 设置焦点
     *
     * @param focuable focuable
     */
    public void setFocuable(boolean focuable) {
        this.focuable = focuable;
        setFocusable(focuable);
        setFocusableInTouchMode(focuable);
        setCursorVisible(focuable);
    }


    /**
     * 焦点监听
     *
     * @param focusChangeListence listence
     */
    public void setFocusChangeListence(OnEditFocusChangeListence focusChangeListence) {
        this.focusChangeListence = focusChangeListence;
    }

    /**
     * 设置键盘背景 可以用此背景设置线条粗细
     *
     * @param drawable drawable
     */
    public void setKeyViewBgDrawable(Drawable drawable) {
        if (systemKeyboard != null) systemKeyboard.setKeybgDrawable(drawable);
    }

    public void setRandomkeys(boolean isRandomkeys) {
        if (systemKeyboard != null) systemKeyboard.setRandomkeys(isRandomkeys);
    }

    /**
     * 绑定EditText
     *
     * @param editText editText
     */
    public void setEditText(EditText editText) {
        listener.setEditText(editText);
        if (editText instanceof SystemKeyBoardEditText) {
            listener.setPopupWindow(((SystemKeyBoardEditText) editText).getKeyboardWindow());
        }
    }

}
