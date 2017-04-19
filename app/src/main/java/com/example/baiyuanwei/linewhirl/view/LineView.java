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
import android.view.View;

/**
 * Created by baiyuanwei on 17/4/13.
 */

public class LineView extends View {

    private static final int DEFAULT_LINE_WIDTH = 5;
    private Paint paint;
    private Paint eraserPaint;
    private Paint movePaint;
    private Paint testPaint;

    Path lastPath;

    int lineLength = 100;
    int x1 = 0;
    int y1 = 160;
    int x2 = x1 + lineLength;
    int y2 = 160;
    private boolean isDrawBaseLine = true;

    public LineView(Context context) {
        this(context, null);
    }

    public LineView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(DEFAULT_LINE_WIDTH);

        eraserPaint = new Paint();
        eraserPaint.setAntiAlias(true);
        eraserPaint.setAlpha(0);
        eraserPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        eraserPaint.setDither(true);
        eraserPaint.setStrokeWidth(DEFAULT_LINE_WIDTH);
        eraserPaint.setStyle(Paint.Style.STROKE);
        eraserPaint.setStrokeJoin(Paint.Join.ROUND);

        movePaint = new Paint();
        movePaint.setAntiAlias(true);
        movePaint.setColor(Color.RED);
        movePaint.setStyle(Paint.Style.STROKE);
        movePaint.setStrokeWidth(DEFAULT_LINE_WIDTH);

        testPaint = new Paint();
        testPaint.setAntiAlias(true);
        testPaint.setColor(Color.RED);
        testPaint.setStyle(Paint.Style.STROKE);
        testPaint.setStrokeWidth(DEFAULT_LINE_WIDTH);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isDrawBaseLine) {
            drawBlackLine(canvas);
//            isDrawBaseLine = false;


        }
        drawWhirlLine(canvas);

        drawTestLine(canvas);
//        postInvalidateDelayed(10);

    }


    private void drawBlackLine(Canvas canvas) {
        int count = canvas.save();
        Path path = new Path();
        path.moveTo(0, 160);
        path.lineTo(400, 160);
        path.close();
        canvas.drawPath(path, paint);
        canvas.restoreToCount(count);
    }

    private void drawWhirlLine(Canvas canvas) {

        canvas.saveLayer(0, 0, 500, 200, paint, Canvas.HAS_ALPHA_LAYER_SAVE_FLAG);

        if (lastPath != null) {
            cleanCanvasLine(canvas, lastPath);
        }
        Path path = new Path();
        path.moveTo(x1, 160);
        path.lineTo(x2, 160);
        path.close();

        canvas.drawPath(path, movePaint);
        lastPath = path;

        x1 += 1;
        x2 = x1 + lineLength;

        canvas.restore();
    }

    private void cleanCanvasLine(Canvas canvas, Path path) {
        canvas.drawPath(path, eraserPaint);
    }


    private void drawTestLine(Canvas canvas) {
        canvas.save();
        int count = 0;
        while (count < 100) {
            canvas.drawPoint(10 + count, 10, testPaint);
            count++;
        }

    }

}
