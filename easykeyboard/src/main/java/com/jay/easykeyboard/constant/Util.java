package com.jay.easykeyboard.constant;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
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

    //判断是否是数字(包含正负数校验)
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("-?[0-9]+.?[0-9]+");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }


    /**
     * 隐藏软键盘
     * @param activity
     */
    public static void hideKeyboard(Activity activity){
        View view = activity.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 关闭软键盘
     * @param context
     * @param editText
     */
    public static void closeKeyboard(Context context, EditText editText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    /**
     * 打开软键盘
     * @param activity
     * @param editText
     */
    public static void openKeyboard(Activity activity, EditText editText) {
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    /*
    * 关闭软键盘显示游标
    * */
    public static void showCursor(Activity activity, EditText editText){
        if (android.os.Build.VERSION.SDK_INT <= 10) {
            editText.setInputType(InputType.TYPE_NULL);
        } else {
            activity.getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            try {
                Class<EditText> cls = EditText.class;
                Method setShowSoftInputOnFocus;
                setShowSoftInputOnFocus = cls.getMethod(
                        "setShowSoftInputOnFocus", boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(editText, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取实际内容高度
     * @param context
     * @return
     */
    public static int getContentHeight(Context context){
        int screenh_nonavbar = 0;
        DisplayMetrics dMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager!=null) {
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
