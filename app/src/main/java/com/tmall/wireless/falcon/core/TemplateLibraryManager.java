package com.tmall.wireless.falcon.core;

import android.text.TextUtils;

import com.libra.common.LogHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gujicheng on 17/5/11.
 */

public class TemplateLibraryManager {
    private final static String TAG = "TemplateManager";

    private Map<String, Template> mMap = new HashMap<>();

    public static class Template {
        public String mLocation;
        public Object mParam;

        public Template(String location) {
            mLocation = location;
        }

        public Template(String location, String param) {
            mLocation = location;
            mParam = param;
        }

        @Override
        public String toString() {
            return "Template location:" + mLocation + "  param:" + mParam;
        }
    }

    public TemplateLibraryManager() {
    }

    public void addTemplate(String name, Template temp) {
        if (!TextUtils.isEmpty(name) && null != temp) {
            mMap.put(name, temp);
        } else {
            LogHelper.e(TAG, "addTemplate failed, name:" + name + "  temp:" + temp);
        }
    }

    public void release() {
        mMap.clear();
    }

    public Template getTemplate(String pageName) {
        if (!TextUtils.isEmpty(pageName)) {
            return mMap.get(pageName);
        }

        return null;
    }

}
