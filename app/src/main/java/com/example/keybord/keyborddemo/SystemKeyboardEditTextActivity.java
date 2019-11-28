package com.example.keybord.keyborddemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;

import com.jay.easykeyboard.SystemKeyBoardEditText;
import com.jay.easykeyboard.action.KeyBoardActionListener;

/**
 * Created by huangjie on 2018/2/6.
 * 类名：
 * 说明：附带EditText的popwindow形式弹出的键盘
 */

public class SystemKeyboardEditTextActivity extends AppCompatActivity implements KeyBoardActionListener {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_systemkeyboardedittext);
        SystemKeyBoardEditText skb_top = findViewById(R.id.skb_top);
        SystemKeyBoardEditText skb_bottom = findViewById(R.id.skb_bottom);
        skb_top.setOnKeyboardActionListener(this);
        skb_bottom.setOnKeyboardActionListener(this);
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onTextChange(Editable editable) {

    }

    @Override
    public void onClear() {

    }

    @Override
    public void onClearAll() {

    }
}
