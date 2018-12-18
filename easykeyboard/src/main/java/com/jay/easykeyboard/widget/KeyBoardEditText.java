package com.jay.easykeyboard.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.PopupWindow;

import com.jay.easykeyboard.R;
import com.jay.easykeyboard.util.Util;

/**
 * Created by huangjie on 2018/2/5.
 * 类名：能弹出自定义键盘的EditText抽象类
 */
public abstract class KeyBoardEditText extends AppCompatEditText {
    private Activity activity;
    private int realHeight; //界面实际高度
    private PopupWindow mKeyboardWindow;
    private View mDecorView;
    private View mContentView;
    private int scrolldis;


    public KeyBoardEditText(Context context) {
        this(context, null);
    }

    public KeyBoardEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KeyBoardEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        if (context instanceof Activity) activity = (Activity) context;
        realHeight = Util.getContentHeight(context);
        this.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        if (this.getText() != null) {
            this.setSelection(this.getText().length());
        }

    }

    /**
     * 初始化popindow
     * @param contentView layout
     */
    protected void initPopWindow(View contentView) {
        mKeyboardWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mKeyboardWindow.setAnimationStyle(R.style.AnimationFade);
        mKeyboardWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mKeyboardWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                // TODO Auto-generated method stub
                if (scrolldis > 0) {
                    int temp = scrolldis;
                    scrolldis = 0;
                    if (null != mContentView) {
                        mContentView.scrollBy(0, -temp);
                    }
                }
            }
        });
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (null != activity) {
            Window mWindow = activity.getWindow();
            mDecorView = mWindow.getDecorView();
            mContentView = mDecorView.findViewById(Window.ID_ANDROID_CONTENT);
        }
        Util.disableShowSoftInput(this);
    }


    /**
     * 打开键盘
     */
    protected void showKeyboardWindow() {
        if (null != mKeyboardWindow) {
            if (!mKeyboardWindow.isShowing()) {
                mKeyboardWindow.showAtLocation(this.mDecorView, Gravity.BOTTOM, 0, 0);
                if (null != mDecorView && null != mContentView) {
                    final View popContentView = mKeyboardWindow.getContentView();
                    popContentView.post(new Runnable() {
                        @Override
                        public void run() {
                            int[] pos = new int[2];
                            getLocationOnScreen(pos);
                            float height = popContentView.getMeasuredHeight();
                            Rect outRect = new Rect();
                            mDecorView.getWindowVisibleDisplayFrame(outRect);
                            int screen = realHeight;
                            scrolldis = (int) ((pos[1] + getMeasuredHeight() - outRect.top) - (screen - height));
                            if (scrolldis > 0) {
                                mContentView.scrollBy(0, scrolldis);
                            }
                        }
                    });
                }
            }
        }
    }


    /**
     * 关闭键盘
     */
    protected void dismissKeyboardWindow() {
        if (null != mKeyboardWindow) {
            if (mKeyboardWindow.isShowing()) {
                mKeyboardWindow.dismiss();
            }
        }
    }

    /**
     * 判断键盘是否打开
     * @return isShowing
     */
    protected boolean isShowing() {
        boolean isShowing = false;
        if (null != mKeyboardWindow) {
            isShowing = mKeyboardWindow.isShowing();
        }
        return isShowing;
    }

    /**
     * 获取popwindow对象
     * @return mKeyboardWindow
     */
    public PopupWindow getKeyboardWindow() {
        return mKeyboardWindow;
    }


    /**
     * 屏蔽EditText长按复制功能,启用后粘贴功能也会失效
     */
    public void removeCopyAndPaste() {
        this.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }
        });
        setLongClickable(false);
    }
}
