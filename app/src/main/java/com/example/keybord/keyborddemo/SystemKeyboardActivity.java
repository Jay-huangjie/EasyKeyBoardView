package com.example.keybord.keyborddemo;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jay.easykeyboard.SystemKeyboard;
import com.jay.easykeyboard.action.IKeyBoardUIChange;
import com.jay.easykeyboard.action.KeyBoardActionListener;
import com.jay.easykeyboard.util.Util;

/**
 * Created by huangjie on 2018/2/6.
 * 类名：
 * 说明：此Activity实现了键盘固定的情况下的使用
 */

public class SystemKeyboardActivity extends AppCompatActivity implements View.OnFocusChangeListener {

    private static final String TAG = "SystemKeyboardActivity";
    private SystemKeyboard mKeyboard;
    private boolean isRandom =false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_systemkeyboard);
        mKeyboard = findViewById(R.id.systemkeyboard);
        Button btnChangeUi = findViewById(R.id.btn_change_ui);
        EditText edit1 = findViewById(R.id.edit);
        EditText edit2 = findViewById(R.id.edit2);
        mKeyboard.setEditText(edit1); //用于绑定EditText,如果切换了EditText，请务必设置此方法
        mKeyboard.setOnKeyboardActionListener(new KeyBoardActionListener() {
            @Override
            public void onComplete() {
                showShortToast("完成");
            }

            @Override
            public void onTextChange(Editable editable) {
                Log.i(TAG,"onTextChange:"+editable.toString());
            }

            @Override
            public void onClear() {
                showShortToast("onClear");
            }

            @Override
            public void onClearAll() {
                showShortToast("onClearAll");
            }
        });

        edit1.setOnFocusChangeListener(this);
        edit2.setOnFocusChangeListener(this);

        btnChangeUi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //改变ui
                mKeyboard.setKeyboardUI(new IKeyBoardUIChange() {
                    @Override
                    public Paint setPaint(Paint paint) {
                        paint.setColor(Color.BLUE);
                        paint.setTextSize(Util.dpToPx(getApplicationContext(),16));
                        return paint;
                    }
                });
            }
        });

        findViewById(R.id.btn_random).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRandom = !isRandom;
                mKeyboard.setRandomKeys(isRandom);
            }
        });
    }

    private void showShortToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            switch (v.getId()){
                case R.id.edit: //绑定EditText并显示自定义键盘
                    mKeyboard.setEditText((EditText) v);
                    break;
                case R.id.edit2: //绑定EditText并显示原生键盘
                    mKeyboard.setEditText((EditText) v,true);
                    break;
            }
        }
    }
}
