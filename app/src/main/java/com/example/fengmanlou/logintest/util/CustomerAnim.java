package com.example.fengmanlou.logintest.util;

import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by fengmanlou on 2015/4/9.
 */
public class CustomerAnim extends Animation{
    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        t.getMatrix().setTranslate((float) (Math.sin(interpolatedTime*20)*20), 0);

        super.applyTransformation(interpolatedTime, t);
    }
}
