package com.tmall.wireless.falcon;

import android.content.Context;
import android.text.TextUtils;
import android.util.SparseArray;

import com.libra.common.LogHelper;
import com.libra.common.StringTab;
import com.libra.common.io.reader.BufferReader;
import com.libra.common.io.reader.Reader;
import com.tmall.wireless.falcon.core.AsyncWorkManager;
import com.tmall.wireless.falcon.core.ComponentLibrary;
import com.tmall.wireless.falcon.core.IView;
import com.tmall.wireless.falcon.core.StubView;

import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by gujicheng on 17/5/9.
 */

public class PageContext {
    private final static String TAG = "PageContext";

    private ApplicationContext mAppContext;

    private int mMajorVersion;
    private int mMinorVersion;

    private String mMainComName;

    private SortedSet<Integer> mAsyncComponent = new TreeSet<>();

    private ComponentLibrary mComponentLibrary = new ComponentLibrary();
    private StringTab mStringTab = new StringTab();

    private volatile SparseArray<List<IView>> mComponentCache = new SparseArray<>();

    private Object mSynchronizedComObject = new Object();

    public PageContext(ApplicationContext context) {
        mAppContext = context;
    }

    public AsyncWorkManager getAsyncWorkManager() {
        if (null != mAppContext) {
            return mAppContext.getAsyncWorkManager();
        }
        return null;
    }

    public ApplicationContext getApplicationContext() {
        return mAppContext;
    }

    public Context getContext() {
        return mAppContext.getContext();
    }

    public StringTab getStringTab() {
        return mStringTab;
    }

    public void recycleComponent(IView com) {
        if (null != com) {
            synchronized (mSynchronizedComObject) {
                int componentId = com.getComponentId();
                if (componentId > 0) {
                    List<IView> comList = mComponentCache.get(componentId);
                    if (null == comList) {
                        comList = new LinkedList<>();
                        mComponentCache.put(componentId, comList);
                    }
                    comList.add(com);
                } else {
                    LogHelper.e(TAG, "recycleComponent invalidate com id:" + componentId);
                }
            }
        } else {
            LogHelper.e(TAG, "recycleComponent com is null");
        }
    }

    private IView getComponentFromCache(int componentId) {
        synchronized (mSynchronizedComObject) {
            List<IView> comList = mComponentCache.get(componentId);
            if (null != comList && comList.size() > 0) {
                return comList.remove(0);
            }
            return null;
        }
    }

    public IView newComponent(int comNameId) {
        // get from cache
        IView ret = null;

        if (null != mAppContext) {
            if (comNameId > -1) {
                ret = getComponentFromCache(comNameId);

                if (null == ret) {
                    if (mAsyncComponent.contains(comNameId)) {
                        // async component
                        ComponentLibrary.Item item = mComponentLibrary.getItem(comNameId);
                        ret = new StubView(this, comNameId, item.mWidth, item.mHeight, item.mAutoDimDirection, item.mAutoDimX, item.mAutoDimY);
                    } else {
                        // get from component library
                        ret = newComponentFromLibrary(comNameId);
                    }
                }
            } else {
                LogHelper.e(TAG, "comName is empty");
            }
        }
        return ret;
    }

    public IView newComponentFromLibrary(int comNameId) {
        IView ret = null;
        Reader reader = mComponentLibrary.getComponentReader(comNameId);
        if (null != reader) {
            ret = mAppContext.getComponentFactory().newComponent(reader, this);
            if (null != ret) {
                ret.setComponentId(comNameId);
            }
            BufferReader.free(reader);
        } else {
            LogHelper.e(TAG, "reader is null");
        }
        return ret;
    }

    public IView newComponent(String comName) {
        IView ret = null;

        if (!TextUtils.isEmpty(comName)) {
            ret = newComponent(mStringTab.getStringId(comName));
        } else {
            LogHelper.e(TAG, "comName is empty");
        }

        return ret;
    }

    public void release() {
        if (null != mAppContext) {
            mAppContext.getAsyncWorkManager().reset();
            mAppContext = null;
        }

        if (null != mComponentLibrary) {
            mComponentLibrary.release();
            mComponentLibrary = null;
        }

        if (null != mStringTab) {
            mStringTab.release();
            mStringTab = null;
        }

        if (null != mComponentCache) {
            for(int i = 0, size = mComponentCache.size(); i < size; i++) {
                List<IView> viewList = mComponentCache.valueAt(i);
                if (null != viewList) {
                    for (IView v : viewList) {
                        v.release();
                    }
                }
            }
            mComponentCache.clear();
        }
    }

    public String getMainComName() {
        return mMainComName;
    }

    public void setVersion(int majorVersion, int minorVersion) {
        mMajorVersion = majorVersion;
        minorVersion = minorVersion;
    }

    public boolean load(Reader reader) {
        boolean ret = false;

        if (null != reader) {
            // main component id
            int mainComNameId = reader.readShort();

            // support async component
            int asyncComponentCount = reader.readShort();
            for (int i = 0; i < asyncComponentCount; ++i) {
                mAsyncComponent.add((int)reader.readShort());
            }

            // component library
            if (ret = mComponentLibrary.load(reader)) {
                // string tab
                if (ret = mStringTab.load(reader)) {
                    mMainComName = mStringTab.getString(mainComNameId);
                    ret = true;
                } else {
                    LogHelper.e(TAG, "read string tab failed");
                }
            } else {
                LogHelper.e(TAG, "read component library failed");
            }
        }

        return ret;
    }
}
