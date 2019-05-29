package com.skyward.banner.utils;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * @author skyward
 * date: 2019/5/28
 * desc:
 */
public class BannerTransformer implements ViewPager.PageTransformer {


    private static final float SCALE=0.09f;



    @Override
    public void transformPage(@NonNull View page, float position) {
        if (position < -1) {
            /* [-Infinity,-1)*/
            /*页面已经在屏幕左侧且不可视*/
            page.setScaleX((1 + position * SCALE));
            page.setScaleY((1 + position * SCALE));
        } else if (position <= 0) {
            /* [-1,0]*/
            /*页面从左侧进入或者向左侧滑出的状态*/
            page.setScaleX((1 + position * SCALE));
            page.setScaleY((1 + position * SCALE));
        } else if (position <= 1) {
            /* (0,1]*/
            /*页面从右侧进入或者向右侧滑出的状态*/
            page.setScaleX((1-  position * SCALE));
            page.setScaleY((1 - position * SCALE));
        } else if (position > 1) {
            /*页面已经在屏幕右侧且不可视*/
            page.setScaleX((1-  position * SCALE));
            page.setScaleY((1 - position * SCALE));
        }
    }
}
