package com.skyward.mybanner.utils;

import android.content.res.Resources;

import com.bumptech.glide.request.RequestOptions;

/**
 * @author skyward
 * date: 2018/11/29
 * desc:图片圆角处理
 */
public class RoundedCornersUtils {

    public static RequestOptions roundingRadius(int radius){
        //图片圆角处理
        RequestOptions requestOptions = new RequestOptions();
        return requestOptions.transform(new RoundedCornersTransformation(dp2px(radius),0, RoundedCornersTransformation.CornerType.ALL));

    }

    public static RequestOptions roundingRadius(int radius, RoundedCornersTransformation.CornerType cornerType){
        //图片任意圆角处理
        RequestOptions requestOptions = new RequestOptions();
        return requestOptions.transform(new RoundedCornersTransformation(dp2px(radius),0,cornerType));

    }

    public static int dp2px(final float dpValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
