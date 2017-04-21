package com.example.baiyuanwei.linewhirl.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.baiyuanwei.linewhirl.R;

/**
 * Created by baiyuanwei on 17/4/18.
 * 增加的功能：
 * 1、线条的起点可以从任意位置开始，
 */

public class WhirlSquareLineViewV2 extends View {

    private final static String TAG = WhirlSquareLineViewV2.class.getSimpleName();

    private final static int DEFAULT_PAINT_WIDTH = 5;
    private final static int DEFAULT_RADIUS = 60;
    private final static float DEFAULT_LINE_SIZE_RATE = 1 / 9f;
    private final static float DEFAULT_START_OFFSET = 0; // 起点偏移量

    private Paint linePaint;
    private Paint rectanglePaint;

    private int width;
    private int height;
    private int radius;
    private Canvas canvas;
    private PathMeasure pathMeasure;

    private float animatedValue;
    private ValueAnimator valueAnimator;
    private float length;

    private Path dstPath;
    private Path dst2Path;

    private int startX;
    private int startY;

    private Path sourcePath;
    private RectF rectangleRectF;

    private float startOffset;


    public WhirlSquareLineViewV2(Context context) {
        this(context, null);
    }

    public WhirlSquareLineViewV2(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WhirlSquareLineViewV2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        radius = DEFAULT_RADIUS;

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setColor(context.getResources().getColor(R.color.red));
        linePaint.setStrokeWidth(DEFAULT_PAINT_WIDTH);

        rectanglePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        rectanglePaint.setStyle(Paint.Style.STROKE);
        rectanglePaint.setColor(Color.BLACK);
        rectanglePaint.setStrokeWidth(DEFAULT_PAINT_WIDTH);

        valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                animatedValue = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.setDuration(5000);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);

        dstPath = new Path();
        dst2Path = new Path();

        startOffset = DEFAULT_START_OFFSET;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        width = getWidth();
        height = getHeight();

        startX = DEFAULT_PAINT_WIDTH;
        startY = DEFAULT_PAINT_WIDTH;

        initPath();
    }

    private void initPath() {

        rectangleRectF = new RectF(startX, startY, width - startX, height - startY);
        sourcePath = new Path();
        sourcePath.addRoundRect(rectangleRectF, radius, radius, Path.Direction.CW);


        pathMeasure = new PathMeasure();
        pathMeasure.setPath(sourcePath, true);
        length = pathMeasure.getLength();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        this.canvas = canvas;

        drawRectangle();

        drawPartRectangle();

    }

    private void drawRectangle() {
        canvas.save();
        canvas.drawPath(sourcePath, rectanglePaint);
        canvas.restore();
    }

    private void drawPartRectangle() {
        canvas.save();
        dstPath.reset();
        dstPath.moveTo(startX, startY);

        dst2Path.reset();
        dst2Path.moveTo(startX, startY);

        float stop = getStopD();
        float start = getStartD(stop);
        float[] startAndStopArray = getStartAndStop();

        Log.e(TAG, "line_change: stop = " + stop + ",start = " + start + ",length = " + length);

        if (startAndStopArray[1] > length && startAndStopArray[0] < length) {
            float newStart = 0;
            float newStop = startAndStopArray[1] - length;
            pathMeasure.getSegment(startAndStopArray[0], length, dstPath, true);
            pathMeasure.getSegment(newStart, newStop, dst2Path, true);
            canvas.drawPath(dstPath, linePaint);
            canvas.drawPath(dst2Path, linePaint);

        } else if (startAndStopArray[1] > length && startAndStopArray[0] > length) {
            float newStart = startAndStopArray[0] - length;
            float newStop = startAndStopArray[1] - length;
            pathMeasure.getSegment(newStart, newStop, dstPath, true);
            canvas.drawPath(dstPath, linePaint);
        } else {
            pathMeasure.getSegment(startAndStopArray[0], startAndStopArray[1], dstPath, true);
            canvas.drawPath(dstPath, linePaint);
        }

        canvas.restore();
    }

    private float getStopD() {
        float stop = startOffset;
        stop += length * animatedValue;

        return stop;

    }

    private float[] getStartAndStop() {

        float[] startAndStopArray = new float[2];

        if (animatedValue < DEFAULT_LINE_SIZE_RATE) {
            startAndStopArray[0] = startOffset;
            startAndStopArray[1] = startOffset + animatedValue * length;
        } else if (animatedValue > 1 - DEFAULT_LINE_SIZE_RATE) {
            startAndStopArray[0] = startOffset + animatedValue * length - (DEFAULT_LINE_SIZE_RATE * length - (animatedValue - (1 - DEFAULT_LINE_SIZE_RATE)) * length);
            startAndStopArray[1] = startOffset + animatedValue * length;
        } else {
            startAndStopArray[0] = startOffset + animatedValue * length - DEFAULT_LINE_SIZE_RATE * length;
            startAndStopArray[1] = startOffset + animatedValue * length;
        }

        return startAndStopArray;
    }

    private float getStartD(float stop) {
//        if (stop > length) {
//            stop = length;
//        }

        float start;

        if (animatedValue < DEFAULT_LINE_SIZE_RATE) {
            start = (stop - ((DEFAULT_LINE_SIZE_RATE - Math.abs(animatedValue - DEFAULT_LINE_SIZE_RATE)) * length));
        } else if (animatedValue > 1 - DEFAULT_LINE_SIZE_RATE) {
            start = ((1 - 2 * DEFAULT_LINE_SIZE_RATE) + (animatedValue - (1 - DEFAULT_LINE_SIZE_RATE)) * 2) * length;
        } else {
            start = stop - (length * DEFAULT_LINE_SIZE_RATE);
        }

        return start;
    }


    public void startAnim() {
        if (valueAnimator != null) {
            valueAnimator.start();
        }
    }

    public void cancelAnim() {
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
    }

    public void endAnim() {
        if (valueAnimator != null) {
            valueAnimator.end();
        }
    }

    public void setStartOffset(float offset) {
        startOffset = offset;
    }

}
