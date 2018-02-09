package com.jay.easykeyboard;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.jay.easykeyboard.constant.KeyboardConfig;
import com.jay.easykeyboard.constant.Util;
import com.jay.easykeyboard.function.SystemOnKeyboardActionListener;
import com.jay.easykeyboard.keyboard.MyKeyboardView;

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

    public interface KeyUI {
        Paint paintConfig(Paint mPaint);
    }

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
        if (a.hasValue(R.styleable.SystemKeyboard_xmlLayoutResId)) {
            int xmlLayoutResId = a.getResourceId(R.styleable.SystemKeyboard_xmlLayoutResId, 0);
            initKeyBoard(context, xmlLayoutResId);
        }
        if (a.hasValue(R.styleable.SystemKeyboard_keyViewbg))
            keybgDrawable = a.getDrawable(R.styleable.SystemKeyboard_keyViewbg);
//        if (a.hasValue(R.styleable.SystemKeyboard_randomkeys)) {
//            boolean randomkeys = a.getBoolean(R.styleable.SystemKeyboard_randomkeys, false);
//            if (randomkeys) {
//                randomdigkey();
//            }
//        }
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
        setOnKeyboardActionListener(new SystemOnKeyboardActionListener());
        this.addView(keyboardView);
    }


//    private void randomdigkey() {
//        if (mKeyboard == null) {
//            Log(TAG + "==>randomdigkey方法keyboard为空");
//            return;
//        }
//        List<Keyboard.Key> keyList = mKeyboard.getKeys();
//        // 查找出0-9的数字键
//        List<Keyboard.Key> newkeyList = new ArrayList<Keyboard.Key>();
//        for (int i = 0, size = keyList.size(); i < size; i++) {
//            Keyboard.Key key = keyList.get(i);
//            CharSequence label = key.label;
//            if (label != null && Util.isNumeric(label.toString())) {
//                newkeyList.add(key);
//            }
//        }
//        int count = newkeyList.size();
//        List<KeyModel> resultList = new ArrayList<KeyModel>();
//        LinkedList<KeyModel> temp = new LinkedList<KeyModel>();
//        for (int i = 0; i < count; i++) {
//            temp.add(new KeyModel(48 + i, i + ""));
//        }
//        Random rand = new SecureRandom();
//        rand.setSeed(SystemClock.currentThreadTimeMillis());
//        for (int i = 0; i < count; i++) {
//            int num = rand.nextInt(count - i);
//            KeyModel model = temp.get(num);
//            resultList.add(new KeyModel(model.getCode(), model.getLable()));
//            temp.remove(num);
//        }
//        for (int i = 0, size = newkeyList.size(); i < size; i++) {
//            Keyboard.Key newKey = newkeyList.get(i);
//            KeyModel resultmodle = resultList.get(i);
//            newKey.label = resultmodle.getLable();
//            newKey.codes[0] = resultmodle.getCode();
//        }
//    }

    public MyKeyboardView getKeyboardView() {
        return keyboardView;
    }

    public Keyboard getKeyboard() {
        return mKeyboard;
    }

    public void UserNativeKeyboard(boolean isUser) {
        if (isUser) setVisibility(GONE);
        else setVisibility(VISIBLE);
    }

    public void UserNativeKeyboard(boolean isUser, Activity activity, EditText editText) {
        if (isUser) {
            setVisibility(GONE);
            Util.openKeyboard(activity, editText);
        } else {
            setVisibility(VISIBLE);
            Util.hideKeyboard(activity);
        }
    }

    public void setXmlLayoutResId(int xmlLayoutResId) {
        initKeyBoard(getContext(), xmlLayoutResId);
    }

//    public void isRandomkeys(boolean isRandomkeys) {
//        if (isRandomkeys) randomdigkey();
//    }

    public void setKeybgDrawable(Drawable keybgDrawable) {
        this.keybgDrawable = keybgDrawable;
        if (null != keyboardView)
            keyboardView.setKeybgDrawable(keybgDrawable);
    }

    public void setOnKeyboardActionListener(KeyboardView.OnKeyboardActionListener onKeyboardActionListener){
        if (onKeyboardActionListener!=null)
            keyboardView.setOnKeyboardActionListener(onKeyboardActionListener);
    }

    public void setKeyboardUI(KeyUI ui) {
        if (null != ui) {
            keyboardView.setPaint(ui.paintConfig(keyboardView.getPaint()));
        }
    }


    public void recycle() {
        keyboardView.setOnKeyboardActionListener(null);
        keyboardView = null;
        mKeyboard = null;
    }

    private void Log(String value) {
        if (KeyboardConfig.openLog) {
            Log.i(TAG, value);
        }
    }
}
