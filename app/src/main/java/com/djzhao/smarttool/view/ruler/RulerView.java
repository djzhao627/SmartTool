package com.djzhao.smarttool.view.ruler;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * 自定义尺子控件。
 * Created by djzhao on 18/05/02.
 */
public class RulerView extends View {

    private int mmx;
    private int mmy;
    private int pxheight;
    private int pxWidth;
    private int lineLength = 15;
    private Paint mPaint;

    public RulerView(Context context) {
        this(context, null);
    }

    public RulerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mmx = 1;
        mmy = 1;
        pxheight = 1;
        pxWidth = 1;
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(1);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        pxheight = h;
        pxWidth = w;

    }

    public RulerView setSize(int x, int y) {
        mmx = x;
        mmy = y;
        return this;
    }

    public RulerView setTextSize(float size_px) {
        mPaint.setTextSize(size_px);
        return this;
    }

    public RulerView setLineLength(int length) {
        lineLength = length;
        return this;
    }

    public void build() {
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawRuler(canvas);
    }

    private void drawRuler(Canvas canvas) {

        for (int i = 0; i < mmy; i++) {
            float lengthNow = i * pxheight / mmy;
            if (lengthNow < lineLength) {
                canvas.drawLine(0, lengthNow, lengthNow, lengthNow, mPaint);
                canvas.drawLine(lengthNow, 0, lengthNow, lengthNow, mPaint);
            } else {
                drawVertical(canvas, i);
                drawHorizontal(canvas, i);
            }

        }

    }

    private void drawVertical(Canvas canvas, int i) {
        int length = lineLength;
        if (i % 5 == 0) {
            length = lineLength * 15 / 10;
            if (i % 10 == 0) {
                length = lineLength * 2;
                canvas.drawText("" + i / 10, length + lineLength, i * pxheight / mmy + (lineLength / 2), mPaint);
            }
        }
        canvas.drawLine(0, i * pxheight / mmy, length, i * pxheight / mmy, mPaint);
    }

    private void drawHorizontal(Canvas canvas, int i) {

        int length = lineLength;
        if (i % 5 == 0) {
            length = lineLength * 15 / 10;
            if (i % 10 == 0) {
                length = lineLength * 2;
                canvas.drawText("" + i / 10, i * pxWidth / mmx - 3, length + (lineLength * 15 / 10), mPaint);
            }
        }
        canvas.drawLine(i * pxWidth / mmx, 0, i * pxWidth / mmx, length, mPaint);
    }
}
