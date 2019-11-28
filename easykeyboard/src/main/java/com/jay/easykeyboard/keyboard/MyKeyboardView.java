package com.jay.easykeyboard.keyboard;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard.Key;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;

import java.util.List;

//核心类，承担绘制工作
public class MyKeyboardView extends KeyboardView {

    private Drawable mKeyDrawable;
    private Rect rect;
    private Paint paint;

    public MyKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initResources(context);
    }

    public MyKeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initResources(context);
    }

    private void initResources(Context context) {
        Resources res = context.getResources();
        rect = new Rect();
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(50);
        paint.setColor(res.getColor(android.R.color.black));
    }

    public void setKeyDrawable(Drawable mKeyDrawable){
        this.mKeyDrawable = mKeyDrawable;
        invalidate();
    }

    public Paint getPaint(){
        return paint;
    }

    public void setPaint(Paint paint){
        this.paint = paint;
        if (paint==null){
            throw new NullPointerException("Paint is not null");
        }
        invalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        List<Key> keys = getKeyboard().getKeys();
        for (Key key : keys) {
            canvas.save();
            int offsetY = 0;
            if (key.y == 0) {
                offsetY = 1;
            }
            int drawY = key.y + offsetY;
            if (mKeyDrawable!=null) {
                rect.left = key.x;
                rect.top = drawY;
                rect.right = key.x + key.width;
                rect.bottom = key.y + key.height;
                canvas.clipRect(rect);
                int[] state = key.getCurrentDrawableState();
                //设置按压效果
                mKeyDrawable.setState(state);
                //设置边距
                mKeyDrawable.setBounds(rect);
                mKeyDrawable.draw(canvas);
            }
            if (key.label != null) {
                canvas.drawText(
                        key.label.toString(),
                        key.x + (key.width / 2),
                        drawY + (key.height + paint.getTextSize() - paint.descent()) / 2,
                        paint);
            } else if (key.icon != null) {
                int intrinsicWidth = key.icon.getIntrinsicWidth();
                int intrinsicHeight = key.icon.getIntrinsicHeight();

                final int drawableX = key.x + (key.width - intrinsicWidth) / 2;
                final int drawableY = drawY + (key.height - intrinsicHeight) / 2;

                key.icon.setBounds(
                        drawableX, drawableY, drawableX + intrinsicWidth,
                        drawableY + intrinsicHeight);

                key.icon.draw(canvas);
            }

            canvas.restore();
        }
    }

}
