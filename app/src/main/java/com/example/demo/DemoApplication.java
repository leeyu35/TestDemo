package com.example.demo;

import android.app.Application;

/**
 * Created by hubert on 2018/5/2.
 */
public class DemoApplication extends Application {

    private static DemoApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static DemoApplication getInstance() {
        return instance;
    }

}
