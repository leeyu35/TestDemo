package com.libra.common;

import android.util.Log;

/**
 * Created by gujicheng on 17/5/9.
 */

public class LogHelper {
    private final static String TAG = "AutoRender";

    public static void d(String tag, String msg) {
        Log.d(TAG, String.format("%s:%s", tag, msg));
    }

    public static void e(String tag, String msg) {
        Log.e(TAG, String.format("%s:%s", tag, msg));
    }

    public static void w(String tag, String msg) {
        Log.w(TAG, String.format("%s:%s", tag, msg));
    }
}
