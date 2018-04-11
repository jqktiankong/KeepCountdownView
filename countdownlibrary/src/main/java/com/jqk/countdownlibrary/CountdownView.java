package com.jqk.countdownlibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2018/4/11 0011.
 */

public class CountdownView extends View {

    private static final int TYPE_PROGRESS = 1;
    private static final int TYPE_FINISH = 2;
    private static final int TYPE_ERROR = 3;

    private int defalutColor;
    private int defaultSize = 50;
    private int defaultPaintSize = 10;
    private int myWidth, myHeight;
    private Paint myPaint;

    private int progressRate = 0;
    private int type;

    public CountdownView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.countdownview,
                0, 0);

        try {
            defalutColor = a.getColor(R.styleable.countdownview_defaultColor, getResources().getColor(R.color.defaultColor));
        } finally {
            a.recycle();
        }

        init();
    }

    public void init() {
        myPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        myPaint.setStyle(Paint.Style.STROKE);
        myPaint.setColor(defalutColor);
        myPaint.setStrokeWidth(defaultPaintSize);
        myPaint.setStrokeCap(Paint.Cap.ROUND);

        type = TYPE_PROGRESS;
    }

    public void setDefalutColor(int defalutColor) {
        this.defalutColor = defalutColor;
        invalidate();
        requestLayout();
    }

    public void error() {
        type = TYPE_ERROR;
        invalidate();
    }

    public void finish() {
        type = TYPE_FINISH;
        invalidate();
    }

    public void setProgressRate(int progressRate) {
        this.progressRate = progressRate;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        myWidth = defaultSize;
        myHeight = defaultSize;

//        UNSPECIFIED	父容器没有对当前View有任何限制，当前View可以任意取尺寸
//        EXACTLY	当前的尺寸就是当前View应该取的尺寸
//        AT_MOST	当前尺寸是当前View能取的最大尺寸

        switch (widthSpecMode) {
            case MeasureSpec.UNSPECIFIED:
                myWidth = defaultSize;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                myWidth = widthSpecSize;
                break;
        }
        switch (heightSpecMode) {
            case MeasureSpec.UNSPECIFIED:
                myHeight = defaultSize;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                myHeight = heightSpecSize;
                break;
        }

        setMeasuredDimension(myWidth, myHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        RectF oval = new RectF();
        oval.left = defaultPaintSize;
        oval.top = defaultPaintSize;
        oval.right = myWidth - defaultPaintSize;
        oval.bottom = myHeight - defaultPaintSize;

        float sweepAngle = progressRate * 3.6f >= 360 ? 360 : progressRate * 3.6f;

        switch (type) {
            case TYPE_PROGRESS:
                if (sweepAngle == 360) {
                    type = TYPE_FINISH;
                    invalidate();
                } else if (sweepAngle < 360) {
                    // 绘制下载标志
                    canvas.drawLine(myWidth / 8 * 3, myHeight / 4 * 3, myWidth / 8 * 5, myHeight / 4 * 3, myPaint);
                    canvas.drawLine(myWidth / 8 * 3, myHeight / 8 * 5, myWidth / 2, myHeight / 4 * 3, myPaint);
                    canvas.drawLine(myWidth / 2, myHeight / 4 * 3, myWidth / 8 * 5, myHeight / 8 * 5, myPaint);
                    canvas.drawLine(myWidth / 2, myHeight / 4, myWidth / 2, myHeight / 4 * 3, myPaint);

                    canvas.drawArc(oval, -90, sweepAngle, false, myPaint);
                }
                break;
            case TYPE_FINISH:
                // 绘制对号标志
                canvas.drawLine(myWidth / 4, myHeight / 4 * 2, myWidth / 2, myHeight / 8 * 5, myPaint);
                canvas.drawLine(myWidth / 2, myHeight / 8 * 5, myWidth / 8 * 6, myHeight / 4, myPaint);

                sweepAngle = 360;
                canvas.drawArc(oval, -90, sweepAngle, false, myPaint);
                break;
            case TYPE_ERROR:
                // 绘制X
                canvas.drawLine(myWidth / 4, myHeight / 4, myWidth / 4 * 3, myHeight / 4 * 3, myPaint);
                canvas.drawLine(myWidth / 4, myHeight / 4 * 3, myWidth / 4 * 3, myHeight / 4, myPaint);

                sweepAngle = 360;
                canvas.drawArc(oval, -90, sweepAngle, false, myPaint);
                break;

        }
    }
}
