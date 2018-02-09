package com.example.keybord.keyborddemo;

import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.jay.easykeyboard.SystemKeyBoardEditText;
import com.jay.easykeyboard.function.SystemOnKeyboardActionListener;

/**
 * Created by huangjie on 2018/2/6.
 * 类名：
 * 说明：
 */

public class SystemKeyboardEidtTextActivity extends AppCompatActivity implements View.OnFocusChangeListener{

    private SystemKeyBoardEditText skb_top;
    private SystemKeyBoardEditText skb_bottom;
    private SystemOnKeyboardActionListener listener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_systemkeyboardedittext);
        skb_top = findViewById(R.id.skb_top);
        skb_bottom = findViewById(R.id.skb_bottom);
        listener = new SystemOnKeyboardActionListener(skb_top,skb_top.getKeyboardWindow()){
            @Override
            public void onKey(int primaryCode, int[] keyCodes) {
                super.onKey(primaryCode, keyCodes);
                if (primaryCode== Keyboard.KEYCODE_DONE){
                    showShortToast("点击了完成按钮");
                }
            }
        };
        skb_top.setOnKeyboardActionListener(listener);
        skb_bottom.setOnKeyboardActionListener(listener);
        skb_top.setOnFocusChangeListener(this);
        skb_bottom.setOnFocusChangeListener(this);
    }


    private void showShortToast(String str){
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus){
            if (v instanceof SystemKeyBoardEditText){
                listener.setEditText((EditText) v);
                listener.setPopupWindow(((SystemKeyBoardEditText) v).getKeyboardWindow());
            }
        }
    }
}
