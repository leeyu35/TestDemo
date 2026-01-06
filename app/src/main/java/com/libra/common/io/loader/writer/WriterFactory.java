package com.libra.common.io.loader.writer;

import android.text.TextUtils;

import com.libra.common.LogHelper;
import com.libra.common.Common;
import com.libra.common.io.writer.Writer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gujicheng on 17/5/10.
 */

public class WriterFactory {
    private final static String TAG = "WriterFactory";

    private Map<String, WriterLoader> mMap = new HashMap<>();

    public void release() {
        mMap.clear();
    }

    public Writer getWriter(String location) {
        if (!TextUtils.isEmpty(location)) {
            String []strArr = location.split("@");
            if (2 == strArr.length) {
                String prefix = strArr[0];
                String path = strArr[1];
                WriterLoader wl = mMap.get(prefix);
                if (null == wl) {
                    if (TextUtils.equals(prefix, Common.PREFIX_SDCARD)) {
                        wl = new SdcardWriterLoader();
                    } else if (TextUtils.equals(prefix, Common.PREFIX_BUFFER)) {
                        wl = new BufferWriterLoader();
                    } else {
                        LogHelper.e(TAG, "can not recognize prefix:" + location);
                    }

                    if (null != wl) {
                        mMap.put(prefix, wl);
                    }
                }

                if (null != wl) {
                    return wl.getWriter(path);
                } else {
                    LogHelper.e(TAG, "wl is null");
                }
            } else {
                LogHelper.e(TAG, "location invalidate:" + location);
            }
        } else {
            LogHelper.e(TAG, "location is null");
        }

        return null;
    }
}
