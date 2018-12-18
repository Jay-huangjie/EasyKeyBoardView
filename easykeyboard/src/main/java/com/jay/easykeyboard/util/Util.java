package com.jay.easykeyboard.util;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by huangjie on 2018/2/3.
 * 类名：
 * 说明：
 */

public class Util {
    /**
     * 密度转换为像素值
     *
     * @param dp
     * @return
     */
    public static int dpToPx(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    //判断是否是字母
    public static boolean isLetter(String str) {
        String wordStr = "abcdefghijklmnopqrstuvwxyz";
        return wordStr.contains(str.toLowerCase());
    }


    public static boolean isNumeric(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }


    /**
     * 禁止Edittext弹出软件盘，光标依然正常显示,并且能正常选取光标
     */
    public static void disableShowSoftInput(EditText editText) {
        Class<EditText> cls = EditText.class;
        Method method;
        try {
            method = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
            method.setAccessible(true);
            method.invoke(editText, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * 显示键盘
     * */
    public static void showKeyboard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                view.requestFocus();
                imm.showSoftInput(view, 0);
            }
        }
    }

    /**
     * 隐藏软键盘
     */
    public static void hideKeyboard(Context context) {
        View view = ((Activity) context).getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }


    /**
     * 获取实际内容高度
     *
     * @param context
     * @return
     */
    public static int getContentHeight(Context context) {
        int screenh_nonavbar = 0;
        DisplayMetrics dMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            Display display = windowManager.getDefaultDisplay();
            display.getMetrics(dMetrics);
            screenh_nonavbar = dMetrics.heightPixels;

            int ver = Build.VERSION.SDK_INT;

            // 新版本的android 系统有导航栏，造成无法正确获取高度
            if (ver == 13) {
                try {
                    Method mt = display.getClass().getMethod("getRealHeight");
                    screenh_nonavbar = (Integer) mt.invoke(display);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (ver > 13) {
                try {
                    Method mt = display.getClass().getMethod("getRawHeight");
                    screenh_nonavbar = (Integer) mt.invoke(display);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return screenh_nonavbar - getStatusBarHeight(context);
    }

    /**
     * 电量栏高度
     *
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0,
                sbar = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        return sbar;
    }
}
