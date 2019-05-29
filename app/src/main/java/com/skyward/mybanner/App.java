package com.skyward.mybanner;

import android.app.Application;

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
    }
}
