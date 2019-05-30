package com.skyward.mybanner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.skyward.banner.Banner;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Banner splashBanner = findViewById(R.id.splash_banner);

        List<Integer> mImageList = new ArrayList<>();


    }
}
