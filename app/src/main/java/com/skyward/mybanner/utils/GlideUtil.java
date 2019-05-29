package com.skyward.mybanner.utils;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.skyward.mybanner.App;

/**
 * @author skyward
 * date: 2019/5/29
 * desc:
 */
public class GlideUtil {

    public static void showImage(Object imagePath, ImageView imageView){
        Glide.with(App.getContext).load(imagePath).into(imageView);
    }

    public static void showImage(Object imagePath, ImageView imageView, int radius){
        Glide.with(App.getContext).load(imagePath).apply(RoundedCornersUtils.roundingRadius(radius)).into(imageView);
    }


    public static void showImage(Object imagePath, ImageView imageView,int radius ,RoundedCornersTransformation.CornerType cornerType){
        Glide.with(App.getContext).load(imagePath).apply(RoundedCornersUtils.roundingRadius(radius,cornerType)).into(imageView);
    }
}
