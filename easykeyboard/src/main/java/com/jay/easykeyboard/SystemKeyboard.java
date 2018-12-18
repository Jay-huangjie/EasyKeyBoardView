package com.jay.easykeyboard;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.jay.easykeyboard.action.IKeyBoardUI;
import com.jay.easykeyboard.action.KeyBoardActionListence;
import com.jay.easykeyboard.bean.KeyModel;
import com.jay.easykeyboard.impl.SystemOnKeyboardActionListener;
import com.jay.easykeyboard.keyboard.MyKeyboardView;
import com.jay.easykeyboard.util.Util;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by huangjie on 2018/2/3.
 * 类名：
 * 说明：使用系统API实现的键盘
 */

public class SystemKeyboard extends FrameLayout {
    private static final String TAG = "SystemKeyboard";
    private MyKeyboardView keyboardView;
    private Drawable keybgDrawable;
    private Keyboard mKeyboard;
    private SystemOnKeyboardActionListener actionListener;
    private int xmlLayoutResId;

    public SystemKeyboard(Context context) {
        this(context, null);
    }

    public SystemKeyboard(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SystemKeyboard(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SystemKeyboard);
        if (a.hasValue(R.styleable.SystemKeyboard_keyViewbg)) {
            keybgDrawable = a.getDrawable(R.styleable.SystemKeyboard_keyViewbg);
        }
        if (a.hasValue(R.styleable.SystemKeyboard_xmlLayoutResId)) {
            xmlLayoutResId = a.getResourceId(R.styleable.SystemKeyboard_xmlLayoutResId, 0);
            initKeyBoard(context, xmlLayoutResId);
        }
        if (a.hasValue(R.styleable.SystemKeyboard_isRandom)){
            boolean isRandow = a.getBoolean(R.styleable.SystemKeyboard_isRandom,false);
            if (isRandow){
                randomkey();
            }
        }
        a.recycle();
    }

    private void initKeyBoard(Context context, int xmlLayoutResId) {
        mKeyboard = new Keyboard(context, xmlLayoutResId);
        keyboardView = (MyKeyboardView) LayoutInflater.from(context).inflate(R.layout.mykeyboardview, null);
        keyboardView.setKeyboard(mKeyboard);
        keyboardView.setEnabled(true);
        keyboardView.setPreviewEnabled(false);
        if (null != keybgDrawable) {
            keyboardView.setKeybgDrawable(keybgDrawable);
        }
        actionListener = new SystemOnKeyboardActionListener();
        keyboardView.setOnKeyboardActionListener(actionListener);
        this.removeAllViews();
        this.addView(keyboardView);
    }


    private void randomkey() {
        List<Keyboard.Key> keyList = mKeyboard.getKeys();
        List<Keyboard.Key> newkeyList = new ArrayList<Keyboard.Key>();
        for (int i = 0, size = keyList.size(); i < size; i++) {
            Keyboard.Key key = keyList.get(i);
            CharSequence label = key.label;
            if (label != null && Util.isNumeric(label.toString())) {
                newkeyList.add(key);
            }
        }
        int count = newkeyList.size();
        List<KeyModel> resultList = new ArrayList<KeyModel>();
        LinkedList<KeyModel> temp = new LinkedList<KeyModel>();
        for (int i = 0; i < count; i++) {
            temp.add(new KeyModel(48 + i, i + ""));
        }
        Random rand = new SecureRandom();
        rand.setSeed(SystemClock.currentThreadTimeMillis());
        for (int i = 0; i < count; i++) {
            int num = rand.nextInt(count - i);
            KeyModel model = temp.get(num);
            resultList.add(new KeyModel(model.getCode(), model.getLable()));
            temp.remove(num);
        }
        for (int i = 0, size = newkeyList.size(); i < size; i++) {
            Keyboard.Key newKey = newkeyList.get(i);
            KeyModel resultmodle = resultList.get(i);
            newKey.label = resultmodle.getLable();
            newKey.codes[0] = resultmodle.getCode();
        }
        keyboardView.setKeyboard(mKeyboard);
    }


    public MyKeyboardView getKeyboardView() {
        return keyboardView;
    }

    public Keyboard getKeyboard() {
        return mKeyboard;
    }


    /**
     * 设置键盘布局
     * @param xmlLayoutResId xml
     *  具体属性可参考：https://blog.csdn.net/ysmile1158157874/article/details/51497503
     */
    public void setXmlLayoutResId(int xmlLayoutResId) {
        initKeyBoard(getContext(), xmlLayoutResId);
    }

    /**
     * 设置随机数字键盘
     * @param isRandomkeys 是否随机,再次设置为false则恢复正常
     */
    public void setRandomkeys(boolean isRandomkeys) {
        if (isRandomkeys){
            randomkey();
        }else {
            mKeyboard = new Keyboard(getContext(), xmlLayoutResId);
            keyboardView.setKeyboard(mKeyboard);
        }
    }


    /**
     * 设置按压背景，线条粗细等
     * @param keybgDrawable d
     */
    public void setKeybgDrawable(Drawable keybgDrawable) {
        this.keybgDrawable = keybgDrawable;
        if (null != keyboardView)
            keyboardView.setKeybgDrawable(keybgDrawable);
    }


    /**
     * 建立与EditText的绑定关系，用于控制输入值
     *
     * @param editText 绑定EditText 默认显示自定义键盘
     */
    public void setEditText(@NonNull EditText editText) {
        setEditText(editText, false);
    }


    /**
     * 建立与EditText的绑定关系，用于控制输入值
     *
     * @param editText             需要绑定的EditText
     * @param isOpenNativeKeyBoard 是否打开原生键盘
     */
    public void setEditText(@NonNull EditText editText, boolean isOpenNativeKeyBoard) {
        actionListener.setEditText(editText);
        if (isOpenNativeKeyBoard) {
            Util.showKeyboard(editText);
            setVisibility(GONE);
        } else {
            setVisibility(VISIBLE);
            Util.disableShowSoftInput(editText);
            Util.hideKeyboard(editText.getContext());
        }
    }


    /**
     * 设置键盘输入监听
     * @param listener l
     */
    public void setOnKeyboardActionListener(KeyBoardActionListence listener) {
        actionListener.setKeyActionListence(listener);
    }

    /**
     * 设置ui监听
     * @param ui u
     */
    public void setKeyboardUI(IKeyBoardUI ui) {
        if (null != ui) {
            keyboardView.setPaint(ui.setPaint(keyboardView.getPaint()));
        }
    }
}
