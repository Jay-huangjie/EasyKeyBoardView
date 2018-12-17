package com.jay.easykeyboard;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.jay.easykeyboard.action.IKeyBoardUI;
import com.jay.easykeyboard.action.KeyBoardActionListence;
import com.jay.easykeyboard.impl.SystemOnKeyboardActionListener;
import com.jay.easykeyboard.keyboard.MyKeyboardView;
import com.jay.easykeyboard.util.Util;

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
            int xmlLayoutResId = a.getResourceId(R.styleable.SystemKeyboard_xmlLayoutResId, 0);
            initKeyBoard(context, xmlLayoutResId);
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

    public void setOnKeyboardActionListener(KeyBoardActionListence listener) {
        actionListener.setKeyActionListence(listener);
    }

    public void setKeyboardUI(IKeyBoardUI ui) {
        if (null != ui) {
            keyboardView.setPaint(ui.setPaint(keyboardView.getPaint()));
        }
    }
}
