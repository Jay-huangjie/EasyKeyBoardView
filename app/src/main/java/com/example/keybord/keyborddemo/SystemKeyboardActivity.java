package com.example.keybord.keyborddemo;

import android.graphics.Color;
import android.graphics.Paint;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jay.easykeyboard.SystemKeyboard;
import com.jay.easykeyboard.function.SystemOnKeyboardActionListener;

/**
 * Created by huangjie on 2018/2/6.
 * 类名：
 * 说明：
 */

public class SystemKeyboardActivity extends AppCompatActivity implements View.OnFocusChangeListener {

    private SystemKeyboard mKeyboard;
    private SystemOnKeyboardActionListener listener;
    private EditText edit1;
    private EditText edit2;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_systemkeyboard);
        mKeyboard = findViewById(R.id.systemkeyboard);
        Button btn_setkeyui = findViewById(R.id.btn_setkeyui);
        edit1 = findViewById(R.id.edit);
        edit2 = findViewById(R.id.edit2);
        listener = new SystemOnKeyboardActionListener() {
            @Override
            public void onKey(int primaryCode, int[] keyCodes) {
                //必须实现该方法,其余方法可选择实现,该方法的作用是将输入的值附到EditText上，同时进行键盘按键监听
                super.onKey(primaryCode, keyCodes);
                if (primaryCode == Keyboard.KEYCODE_DONE) {
                    showShortToast("点击了完成按钮");
                }
            }
        };
        listener.setEditText(edit1);
        mKeyboard.setOnKeyboardActionListener(listener);

        edit1.setOnFocusChangeListener(this);
        edit2.setOnFocusChangeListener(this);

        btn_setkeyui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mKeyboard.setKeyboardUI(new SystemKeyboard.KeyUI() {
                    @Override
                    public Paint paintConfig(Paint mPaint) {
                        mPaint.setColor(Color.BLUE);
                        mPaint.setTextSize(100);
                        return mPaint;
                    }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mKeyboard.recycle();
    }

    private void showShortToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            if (v instanceof EditText)
            listener.setEditText((EditText) v);
        }
    }
}
