package com.example.baiyuanwei.linewhirl.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.baiyuanwei.linewhirl.R;
import com.example.baiyuanwei.linewhirl.util.CustomInterpolator;

/**
 * Created by baiyuanwei on 17/4/18.
 * 增加的功能：
 * 1、线条的长度固定
 * 2、线条颜色渐变：
 * 实现思路：利用paint.setShader（），用LinearGradient来实现，每次绘制线条时，重新计算LinearGradient的起点和终点。
 */

public class WhirlSquareLineViewV5 extends View {
    private final static String TAG = WhirlSquareLineViewV5.class.getSimpleName();

    private final static int DEFAULT_DURATION = 10000;

    private final static int DEFAULT_PAINT_WIDTH = 5;
    private final static int DEFAULT_RADIUS = 60;
    private final static float DEFAULT_LINE_SIZE_RATE = 3 / 9f;
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

    private boolean isInit = false;

    public WhirlSquareLineViewV5(Context context) {
        this(context, null);
    }

    public WhirlSquareLineViewV5(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WhirlSquareLineViewV5(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        radius = DEFAULT_RADIUS;
        isInit = false;

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
        valueAnimator.setDuration(DEFAULT_DURATION);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);

        dstPath = new Path();
        dst2Path = new Path();

        startOffset = DEFAULT_START_OFFSET;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        Log.e(TAG, "onLayout()");

        super.onLayout(changed, left, top, right, bottom);

        if (!isInit) {
            isInit = true;
            initSize();
        }
    }

    private void initSize() {
        Log.e(TAG, "initSize()");

        width = getWidth();
        height = getHeight();

        startX = DEFAULT_PAINT_WIDTH;
        startY = DEFAULT_PAINT_WIDTH;

        rectangleRectF = new RectF(startX, startY, width - startX, height - startY);
        sourcePath = new Path();
        sourcePath.addRoundRect(rectangleRectF, radius, radius, Path.Direction.CW);


        pathMeasure = new PathMeasure();
        pathMeasure.setPath(sourcePath, true);
        length = pathMeasure.getLength();

        startOffset = length / 4f + width - 2 * radius;


        float[] originalTime = getOriginalTime();

        valueAnimator.setInterpolator(new CustomInterpolator(originalTime));

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


        float[] startAndStopArray = getStartAndStop();


        if (startAndStopArray[1] > length && startAndStopArray[0] < length) {
            float newStart = 0;
            float newStop = startAndStopArray[1] - length;

            float[] startPos = new float[2];
            float[] startTan = new float[2];
            pathMeasure.getPosTan(startAndStopArray[0], startPos, startTan);

            float[] stopPos = new float[2];
            float[] stopTan = new float[2];
            pathMeasure.getPosTan(newStop, stopPos, stopTan);

            setPaintShader(startPos, stopPos);

            pathMeasure.getSegment(startAndStopArray[0], length, dstPath, true);
            pathMeasure.getSegment(newStart, newStop, dst2Path, true);

            canvas.drawPath(dstPath, linePaint);
            canvas.drawPath(dst2Path, linePaint);

        } else if (startAndStopArray[1] > length && startAndStopArray[0] > length) {
            float newStart = startAndStopArray[0] - length;
            float newStop = startAndStopArray[1] - length;

            float[] startPos = new float[2];
            float[] startTan = new float[2];
            pathMeasure.getPosTan(newStart, startPos, startTan);

            float[] stopPos = new float[2];
            float[] stopTan = new float[2];
            pathMeasure.getPosTan(newStop, stopPos, stopTan);

            setPaintShader(startPos, stopPos);

            pathMeasure.getSegment(newStart, newStop, dstPath, true);
            canvas.drawPath(dstPath, linePaint);
        } else {

            float[] startPos = new float[2];
            float[] startTan = new float[2];
            pathMeasure.getPosTan(startAndStopArray[0], startPos, startTan);

            float[] stopPos = new float[2];
            float[] stopTan = new float[2];
            pathMeasure.getPosTan(startAndStopArray[1], stopPos, stopTan);

            setPaintShader(startPos, stopPos);


            pathMeasure.getSegment(startAndStopArray[0], startAndStopArray[1], dstPath, true);
            canvas.drawPath(dstPath, linePaint);
        }

        canvas.restore();
    }

    private float[] getStartAndStop() {

        float[] startAndStopArray = new float[2];
        startAndStopArray[0] = startOffset + animatedValue * length - DEFAULT_LINE_SIZE_RATE * length;
        startAndStopArray[1] = startOffset + animatedValue * length;
        return startAndStopArray;
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

    private float[] getOriginalTime() {
        float[] originalTime = new float[3];
        float temp = length / 4f;
        float firstLength = temp + (temp - (height - 2 * radius));
        originalTime[0] = firstLength / length;

        originalTime[1] = 0.5f;

        originalTime[2] = (length - (width - 2 * radius)) / length;

        return originalTime;
    }

    private void setPaintShader(float[] startPos, float[] stopPos) {
        Shader s = new LinearGradient(startPos[0], startPos[1], stopPos[0], stopPos[1], Color.RED, Color.YELLOW, Shader.TileMode.CLAMP);
        linePaint.setShader(s);
    }
}
