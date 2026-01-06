package com.libra.common.io.loader.reader;

import android.text.TextUtils;

import com.libra.common.LogHelper;
import com.libra.common.Common;
import com.libra.common.io.reader.Reader;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gujicheng on 17/5/10.
 */

public class ReaderFactory {
    private final static String TAG = "ReaderFactory";

    private Map<String, ReaderLoader> mMap = new HashMap<>();

    public void release() {
        for (Map.Entry<String, ReaderLoader> entry : mMap.entrySet()) {
            entry.getValue().release();
        }
        mMap.clear();
    }

    public Reader getReader(String location) {
        return getReader(location, null);
    }

    public Reader getReader(String location, Object param) {
        if (!TextUtils.isEmpty(location)) {
            String []strArr = location.split(Common.PREFIX_SEPARATOR);
            if (2 == strArr.length) {
                String prefix = strArr[0];
                String path = strArr[1];
                ReaderLoader wl = mMap.get(prefix);
                if (null == wl) {
                    if (TextUtils.equals(prefix, Common.PREFIX_SDCARD)) {
                        wl = new SdcardReaderLoader();
                    } else if (TextUtils.equals(prefix, Common.PREFIX_BUFFER)) {
                        wl = new BufferReaderLoader();
                    } else {
                        LogHelper.e(TAG, "can not recognize prefix:" + location);
                    }

                    if (null != wl) {
                        mMap.put(prefix, wl);
                    }
                }

                if (null != wl) {
                    return wl.getReader(path, param);
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
