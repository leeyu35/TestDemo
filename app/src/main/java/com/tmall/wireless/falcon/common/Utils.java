package com.tmall.wireless.falcon.common;

import android.content.Context;
import android.content.res.Resources;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.View;

import com.libra.common.LogHelper;
import com.tmall.wireless.falcon.core.IBean;

import java.lang.reflect.Field;

/**
 * Created by gujicheng on 16/8/18.
 */
public class Utils {
    private final static String TAG = "Utils";

    public final static int PFLAG_FORCE_LAYOUT = 0x00001000;
    public final static int PFLAG_LAYOUT_REQUIRED = 0x00002000;

    private final static int UED_SCREEN = 750;
    private static float sDensity;
    private static int sScreenWidth;
    private static double sSleepValue = 0;

    public static IBean parseBean(ClassLoader cl, String beanClass) {
        IBean ret = null;
        try {
            Object obj = Class.forName(beanClass, true, cl).newInstance();
            if (obj instanceof IBean) {
                ret = (IBean) obj;
//                mBean.init(mContext.getContext(), this);
            } else {
                LogHelper.e(TAG, beanClass + " is not bean");
            }
        } catch (InstantiationException e) {
            LogHelper.e(TAG, "error:" + e);
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            LogHelper.e(TAG, "error:" + e);
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            LogHelper.e(TAG, "error:" + e);
            e.printStackTrace();
        }
        return ret;
    }

    public static boolean isMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }

    public static void setFlag(Object obj, int flag) {
        try {
            Field f = View.class.getDeclaredField("mPrivateFlags");
            f.setAccessible(true);
            int v = f.getInt(obj);
            f.setInt(obj, v | flag);
        } catch (NoSuchFieldException e) {
            LogHelper.e(TAG, "NoSuchFieldException " + e);
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            LogHelper.e(TAG, "IllegalAccessException " + e);
            e.printStackTrace();
        }
    }

    public static void init(Context context) {
        final Resources resources = context.getResources();
        final DisplayMetrics dm = resources.getDisplayMetrics();
        sDensity = dm.density;
        sScreenWidth = dm.widthPixels;
    }

    public static void sleep() {
        sleep(0);
//        sleep(1000000);
    }

    public static void sleep(int count) {
        for(int i = 0; i < count; ++i) {
            sSleepValue = sSleepValue * 1000;
        }
    }

    public static int getResId(Class<?> drawableClass, String variableName) {
        if (null != drawableClass) {
            try {
                Field idField = drawableClass.getDeclaredField(variableName);
                return idField.getInt(idField);
            } catch (Exception e) {
                LogHelper.e(TAG, "getResId failed:" + variableName + e);
                e.printStackTrace();
            }
        } else {
            LogHelper.e(TAG, "getResId failed, drawableClass is null");
        }
        return -1;
    }

    public static float wp2px_f(double value) {
        return (float)((value * sScreenWidth) / UED_SCREEN);
    }

    public static int wp2px(double value) {
        return (int)((value * sScreenWidth) / UED_SCREEN + 0.5f);
    }

    public static float dp2px_f(double dpValue) {
        final float scale = sDensity < 0 ? 1.0f : sDensity;

        return (float) (dpValue * scale);
    }

    public static int dp2px(double dpValue) {
        final float scale = sDensity < 0 ? 1.0f : sDensity;

        int finalValue;
        if (dpValue >= 0) {
            finalValue = (int) (dpValue * scale + 0.5f);
        } else {
            finalValue = -((int) (-dpValue * scale + 0.5f));
        }
        return finalValue;
    }

    public static boolean isSpace(char ch) {
        return ((' ' == ch) || ('\t' == ch) || ('\n' == ch));
    }

    public static boolean isDigit(char ch) {
        return ((ch >= '0') && (ch <= '9'));
    }

    public static boolean isHex(char ch) {
        return (ch >= '0' && ch <= '9') || (ch >= 'a' && ch <= 'f') || (ch >= 'A' && ch <= 'F');
    }
}
