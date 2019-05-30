package com.skyward.mybanner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.skyward.banner.Banner;
import com.skyward.banner.utils.BannerType;
import com.skyward.mybanner.utils.GlideUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author skyward
 */
public class MainActivity extends AppCompatActivity {


    private Banner mBanner;
    private List<Integer> mImageList;
    private Banner mBanner2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageList = new ArrayList<>();
        mImageList.add(R.mipmap.b1);
        mImageList.add(R.mipmap.b2);
        mImageList.add(R.mipmap.b3);
//        mImageList.add(R.mipmap.b1);
//        mImageList.add(R.mipmap.b2);
//        mImageList.add(R.mipmap.banner4);

        mBanner = findViewById(R.id.banner);

        mBanner.setData(mImageList)
                .setAutoPlay(true)
                .setBannerType(BannerType.NORMAL)
                .setDefaultImageLoader(new Banner.ImageLoader() {
                    @Override
                    public void imageLoader(Context context, Object imagePath, ImageView imageView) {
                        GlideUtil.showImage(imagePath,imageView);
                    }
                })
                .setOnPageChangeListener(new Banner.OnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position, View view) {
                        Log.i("skyward","pos: "+position);
                    }
                })
                .setOnItemClickListener(new Banner.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position, View view) {
                        Toast.makeText(getApplicationContext(), "当前位置： " + position, Toast.LENGTH_SHORT).show();
                    }
                }).start();

        mBanner2 = findViewById(R.id.banner2);
        mBanner2.setData(mImageList)
                .setAutoPlay(true)
                .setBannerType(BannerType.OFFSET)
                .setCustomImageLoader(new Banner.CustomImageLoader() {
                    @Override
                    public View customImage(ViewGroup container, Context context, Object imagePath) {
//
                        View mView = LayoutInflater.from(context).inflate(R.layout.banner_image_layout,container,false);
                        ImageView imageView = mView.findViewById(R.id.iv_banner);
                        GlideUtil.showImage(imagePath,imageView);

                        return mView;
                    }
                })
                .setOnItemClickListener(new Banner.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position, View view) {
                        Toast.makeText(getApplicationContext(), "当前位置： " + position, Toast.LENGTH_SHORT).show();
                    }
                }).start();


        findViewById(R.id.btn_guide).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),SplashActivity.class));
            }
        });

    }


}
