package com.example.baiyuanwei.linewhirl.util;

import android.animation.TimeInterpolator;
import android.util.Log;

/**
 * Created by baiyuanwei on 17/4/21.
 * 差值器功能：
 * 1、分成四段：第一段：0～DEFAULT_FIRST_TIME，
 * 第二段：DEFAULT_FIRST_TIME ～ DEFAULT_FIRST_TIME ＋ DEFAULT_SECOND_TIME，
 * 第三段：DEFAULT_FIRST_TIME ＋ DEFAULT_SECOND_TIME ～ DEFAULT_FIRST_TIME ＋ DEFAULT_SECOND_TIME＋DEFAULT_THIRD_TIME
 * 第四段：DEFAULT_FIRST_TIME ＋ DEFAULT_SECOND_TIME＋DEFAULT_THIRD_TIME ～ 1；
 * <p>
 * 2、第一段：加速，第二段：匀速，第三段：减速，第四段：匀速；
 */

public class CustomInterpolator implements TimeInterpolator {

    private static final String TAG = CustomInterpolator.class.getSimpleName();

    /**
     * 各段的时间
     * DEFAULT_FIRST_TIME > DEFAULT_THIRD_TIME > DEFAULT_SECOND_TIME
     */
    private final static float DEFAULT_FIRST_TIME = 0.2f;
    private final static float DEFAULT_SECOND_TIME = 0.1f;
    private final static float DEFAULT_THIRD_TIME = 0.15f;

    private float[] originalTime;
    private float[] nowTime;

    public CustomInterpolator(float[] originalTime) {
        this.originalTime = originalTime;
        nowTime = new float[3];

        nowTime[0] = DEFAULT_FIRST_TIME;
        nowTime[1] = DEFAULT_FIRST_TIME + DEFAULT_SECOND_TIME;
        nowTime[2] = DEFAULT_FIRST_TIME + DEFAULT_SECOND_TIME + DEFAULT_THIRD_TIME;

        for (int i = 0; i < nowTime.length; i++) {
            Log.e(TAG, "i = " + i + ",nowTime = " + nowTime[i]);
        }

    }

    @Override
    public float getInterpolation(float input) {
        if (input < nowTime[0]) {
            return calculateFirstTimeValue(input);
        } else if (input < nowTime[1]) {
            return calculateSecondTimeValue(input);
        } else if (input < nowTime[2]) {
            return calculateThirdTimeValue(input);
        } else {
            return calculateForthTimeValue(input);
        }
    }

    /**
     * 第一段：y = ax^2;
     *
     * @param x
     * @return
     */
    private float calculateFirstTimeValue(float x) {
        return (originalTime[0] / (nowTime[0] * nowTime[0])) * x * x;
    }

    /**
     * 第二段：y ＝ kx+b
     *
     * @param x
     * @return
     */
    private float calculateSecondTimeValue(float x) {
        float k = (originalTime[1] - originalTime[0]) / (nowTime[1] - nowTime[0]);
        return k * x + originalTime[0] - nowTime[0] * k;
    }

    /**
     * 第三段：y = ax^2+bx+c 或者 x = ay^2+by+c
     *
     * @param x
     * @return
     */
    private float calculateThirdTimeValue(float x) {

        /**
         * y = ax^2+bx+c
         */
        float b = 10f;
        float a = (originalTime[2] - originalTime[1] - b * (nowTime[2] - nowTime[1])) /
                (nowTime[2] * nowTime[2] - nowTime[1] * nowTime[1]);

        float c = originalTime[1]
                - ((originalTime[2] - originalTime[1]) * nowTime[1] * nowTime[1]) / (nowTime[2] * nowTime[2] - nowTime[1] * nowTime[1])
                + b * (nowTime[2] - nowTime[1]) * nowTime[1] * nowTime[1] / (nowTime[2] * nowTime[2] - nowTime[1] * nowTime[1])
                - b * nowTime[1];
        Log.e(TAG, "third_time: a = " + a + ",b = " + b + ",c = " + c);
        return a * x * x + b * x + c;

        /**
         * x = ay^2+by+c
         */
//        float b = 19f;
//        float a = (nowTime[2] - nowTime[1] - b * (originalTime[2] - originalTime[1])) / (originalTime[2] * originalTime[2] - originalTime[1] * originalTime[1]);
//
//        float c = nowTime[2] - a * originalTime[2] * originalTime[2] - b * originalTime[2];
//
//        float c2 = c - x;
//
//        float y = (float) (-b + Math.sqrt(b * b - 4 * a * c2)) / (2 * a);
//
//        return y;
    }

    /**
     * 第四段：y = kx + b
     *
     * @param x
     * @return
     */
    private float calculateForthTimeValue(float x) {
        float k = (1f - originalTime[2]) / (1f - nowTime[2]);
        float b = 1f - k;
        return k * x + b;
    }

}
