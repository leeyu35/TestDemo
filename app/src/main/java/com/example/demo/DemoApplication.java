package com.example.demo;

import android.app.Application;
import android.content.Context;

/**
 * Created by hubert on 2018/5/2.
 */
public class DemoApplication extends Application {

    private static DemoApplication instance;

    private static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mContext = getApplicationContext();
    }

    public static DemoApplication getInstance() {
        return instance;
    }


    public static Context getContext() {
        return mContext;
    }
}
