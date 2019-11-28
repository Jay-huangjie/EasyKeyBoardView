package com.jay.easykeyboard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.jay.easykeyboard.action.KeyBoardActionListener;
import com.jay.easykeyboard.action.OnEditFocusChangeListener;
import com.jay.easykeyboard.impl.FormatTextWatcher;
import com.jay.easykeyboard.impl.SystemOnKeyboardActionListener;
import com.jay.easykeyboard.util.Util;
import com.jay.easykeyboard.widget.KeyBoardEditText;

/**
 * Created by huangjie on 2018/2/4.
 * 类名：
 * 说明：附带键盘弹出的EditText
 */

public class SystemKeyBoardEditText extends KeyBoardEditText {
    private boolean enable = true;    //是否启用自定义键盘
    private boolean focusEnable = true; //默认获取焦点
    private boolean outSideEnable = false; //点击外部区域是否隐藏键盘
    private SystemKeyboard mSystemKeyboard;
    private FormatTextWatcher mTextWatcher;
    private SystemOnKeyboardActionListener mActionListener;
    private OnEditFocusChangeListener mFocusChangeListener;
    private int focusMark;

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
        initListener();
    }

    private void initListener() {
        setOnTouchListener(new OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!isShowing()) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        if (focusEnable) {
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
                focusMark++;
                if (STATUE == READY) {
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (STATUE == STAR) {
                                if (focusMark == 1 && !hasFocus && outSideEnable) {
                                    dismissKeyboardWindow();
                                }
                                STATUE = READY;
                                focusMark = 0;
                            }
                        }
                    }, 200);
                    STATUE = STAR;
                }
                if (mFocusChangeListener != null) {
                    mFocusChangeListener.OnFocusChangeListener(v, hasFocus);
                }
            }
        });
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SystemKeyBoardEditText);
        outSideEnable = a.getBoolean(R.styleable.SystemKeyBoardEditText_outSideCancel, false);
        int xmlLayoutResId = a.getResourceId(R.styleable.SystemKeyBoardEditText_xmlLayoutResId, 0);
        mSystemKeyboard = new SystemKeyboard(context);
        mSystemKeyboard.setXmlLayoutResId(xmlLayoutResId);
        if (a.hasValue(R.styleable.SystemKeyBoardEditText_keyDrawable)) {
            Drawable keyDrawable = a.getDrawable(R.styleable.SystemKeyBoardEditText_keyDrawable);
            mSystemKeyboard.setKeyDrawable(keyDrawable);
        }
        initPopWindow(mSystemKeyboard);
        mActionListener = new SystemOnKeyboardActionListener();
        mActionListener.setEditText(this);
        mActionListener.setPopupWindow(getKeyboardWindow());
        mSystemKeyboard.getKeyboardView().setOnKeyboardActionListener(mActionListener);
        boolean isSpace = a.getBoolean(R.styleable.SystemKeyBoardEditText_space, false);
        if (isSpace) {
            mTextWatcher = new FormatTextWatcher(this);
            addTextChangedListener(mTextWatcher);
        }
        boolean randomKeys = a.getBoolean(R.styleable.SystemKeyboard_isRandom, false);
        if (randomKeys) {
            mSystemKeyboard.setRandomKeys(true);
        }
        setCursorVisible(true);
        a.recycle();
    }


    public SystemKeyboard getSystemKeyboard() {
        return mSystemKeyboard;
    }

    /**
     * 是否加入4位空格功能
     *
     * @param isSpace true
     */
    public void setSpaceEnable(boolean isSpace) {
        if (isSpace) {
            if (mTextWatcher == null) {
                mTextWatcher = new FormatTextWatcher(this);
            }
            addTextChangedListener(mTextWatcher);
        } else {
            if (null != mTextWatcher) {
                removeTextChangedListener(mTextWatcher);
            }
        }
    }

    /**
     * 重写onKeyDown 当键盘弹出按回退键关闭
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
                && getKeyboardWindow().isShowing()) {
            dismissKeyboardWindow();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 设置键盘输入监听
     *
     * @param listener listence
     */
    public void setOnKeyboardActionListener(KeyBoardActionListener listener) {
        this.mActionListener.setKeyActionListener(listener);
    }

    /**
     * 设置焦点
     *
     * @param focusEnable focuable
     */
    public void setFocusEnable(boolean focusEnable) {
        this.focusEnable = focusEnable;
        setFocusable(focusEnable);
        setFocusableInTouchMode(focusEnable);
        setCursorVisible(focusEnable);
    }


    /**
     * 焦点监听
     *
     * @param focusChangeListener listence
     */
    public void setFocusChangeListener(OnEditFocusChangeListener focusChangeListener) {
        this.mFocusChangeListener = focusChangeListener;
    }

    /**
     * 设置键盘背景 可以用此背景设置线条粗细
     *
     * @param drawable drawable
     */
    public void setKeyViewDrawable(Drawable drawable) {
        if (mSystemKeyboard != null) mSystemKeyboard.setKeyDrawable(drawable);
    }

    public void setRandomKeys(boolean isRandomKeys) {
        if (mSystemKeyboard != null) mSystemKeyboard.setRandomKeys(isRandomKeys);
    }

    /**
     * 绑定EditText
     *
     * @param editText editText
     */
    public void setEditText(EditText editText) {
        mActionListener.setEditText(editText);
        if (editText instanceof SystemKeyBoardEditText) {
            mActionListener.setPopupWindow(((SystemKeyBoardEditText) editText).getKeyboardWindow());
        }
    }

}
