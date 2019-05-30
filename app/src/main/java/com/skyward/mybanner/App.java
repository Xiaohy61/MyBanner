package com.skyward.mybanner;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * @author skyward
 * date: 2019/5/29
 * desc:
 */
public class App extends Application {

    public static App getContext;


    @Override
    public void onCreate() {
        super.onCreate();
        getContext = this;

        LeakCanary.install(this);
    }
}
