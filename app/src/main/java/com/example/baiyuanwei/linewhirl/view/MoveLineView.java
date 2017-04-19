package com.example.baiyuanwei.linewhirl.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by baiyuanwei on 17/4/14.
 */

public class MoveLineView extends View {

    private static final String TAG = MoveLineView.class.getSimpleName();

    private static final int DEFAULT_LINE_WIDTH = 10;


    private int[] x1;
    private int[] x2;

    private int x = 0;
    private Paint linePaint;
    private Paint clearPaint;
    private boolean isClearCanvas = false;

    private int width;
    private int height;
    private int startX;
    private int startY;
    private int radius;

    public MoveLineView(Context context) {
        this(context, null);
    }

    public MoveLineView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MoveLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setColor(Color.BLACK);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(DEFAULT_LINE_WIDTH);

        clearPaint = new Paint();
        clearPaint.setAntiAlias(true);
        clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));


    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        width = getWidth();
        height = getHeight();
        startX = width / 4;
        startY = height / 4;
        radius = width / 4;

        initYFunction();
    }

    private void initYFunction() {
        x1 = new int[2];
        x1[0] = 0;
        x1[1] = width / 2 - 50;

        x2 = new int[2];
        x2[0] = x1[1] + 1;
        x2[1] = x2[0] + 2 * 50 - 1;

    }

    private int getY1(int x) {
        int y1 = height / 2;
        return y1;
    }

    private int getY2(int x) {
        int r = 50;
        int a = width / 2;
        int b = height / 2;
        int y2 = (int) (b + Math.sqrt(r * r - (x - a) * (x - a)));
        return y2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.e(TAG, "onDraw");
        canvas.save();

        Path path = new Path();
        path.moveTo(startX, startY);
        path.lineTo(width / 2, startY);
        path.addArc(width / 2, startY - radius, width / 2 + 2 * radius, startY, -90, 90);

        canvas.drawPath(path, linePaint);

//        if (isClearCanvas) {
//            clearCanvas(canvas);
//        } else {
//            drawLine(canvas);
//        }


    }

    private void drawMoveLine(Canvas canvas) {

    }

    private void drawLine(Canvas canvas) {
        canvas.saveLayer(0, 0, width, height, linePaint, Canvas.HAS_ALPHA_LAYER_SAVE_FLAG);

        int y = 0;
        x = 0;

        while (x < x2[1]) {

            if (x >= x1[0] && x <= x1[1]) {
                y = getY1(x);
            } else {
                y = getY2(x);
            }

            Log.e(TAG, "x = " + x);
            Log.e(TAG, "y = " + y);
            canvas.drawPoint(x, y, linePaint);
            x = x + 1;
        }
        canvas.restore();
    }

    private void clearCanvas(Canvas canvas) {
        canvas.drawPaint(clearPaint);
    }

    public void clearView() {
        isClearCanvas = true;
        postInvalidate();
    }
}
