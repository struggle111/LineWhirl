package com.example.baiyuanwei.linewhirl.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by baiyuanwei on 17/4/14.
 */

public class SquareView extends View {

    private static final int DEFAULT_LINE_WIDTH = 5;
    private Paint paint;

    public SquareView(Context context) {
        this(context, null);
    }

    public SquareView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SquareView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(DEFAULT_LINE_WIDTH);
    }


}
