package com.skyward.mybanner;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.skyward.banner.Banner;
import com.skyward.banner.utils.BannerType;
import com.skyward.mybanner.utils.GlideUtil;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {

    private Button mButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Banner splashBanner = findViewById(R.id.splash_banner);

        final List<Integer> mImageList = new ArrayList<>();
        mImageList.add(R.mipmap.guide_one);
        mImageList.add(R.mipmap.guide_two);
        mImageList.add(R.mipmap.guide_three);
        mImageList.add(R.mipmap.guide_four);

        splashBanner.setData(mImageList)
                .setBannerType(BannerType.GUIDE_PAGES)

                .setCustomImageLoader(new Banner.CustomImageLoader() {
                    @Override
                    public View customImage(ViewGroup container, Context context, Object imagePath) {
                        View mView = LayoutInflater.from(context).inflate(R.layout.item_splash_layout, container, false);
                        ImageView imageView = mView.findViewById(R.id.iv_splash);
                        mButton = mView.findViewById(R.id.btn_skip);
                        GlideUtil.showImage(imagePath, imageView);

                        mButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                finish();
                            }
                        });
                        return mView;
                    }
                })
                .setOnPageChangeListener(new Banner.OnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position, View view) {

                        if (mButton == null) {
                            return;
                        }
                        if (position == mImageList.size() - 1) {
                            mButton.setVisibility(View.VISIBLE);
                        } else {
                            mButton.setVisibility(View.GONE);
                        }
                    }
                })

                .start();


    }
}
