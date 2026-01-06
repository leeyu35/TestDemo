package com.tmall.wireless.falcon.view;

import android.util.SparseArray;

import com.tmall.wireless.falcon.core.IView;

/**
 * Created by gujicheng on 17/5/12.
 */

public class ViewCache {
    private final static String TAG = "ViewCache";

    private SparseArray<IView> mCacheMap = new SparseArray<>();

    public void release() {
        mCacheMap.clear();
    }

    public void put(int key, IView view) {
        if (key > -1 && null != view) {
            mCacheMap.put(key, view);
        } else {
//            LogHelper.e(TAG, "put failed, key:" + key + "  view:" + view);
        }
    }

    public IView get(int key) {
        if (key > -1) {
            return mCacheMap.get(key);
        } else {
//            LogHelper.e(TAG, "key is empty");
        }

        return null;
    }
}
