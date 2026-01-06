package com.tmall.wireless.falcon.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gujicheng on 17/5/22.
 */

public class AsyncWorkManager {
    private final static String TAG = "AsyncWorkManager";

    private final static int DEFAULT_ASYNC_THREAD_COUNT = 10;

    private List<AsyncWork> mAsyncWork = new ArrayList<>();
    private volatile int mIndex = 0;
    private int mThreadCount;

    public AsyncWorkManager() {
        this(DEFAULT_ASYNC_THREAD_COUNT);
    }

    public AsyncWorkManager(int threadCount) {
        mThreadCount = threadCount;
        for (int i = 0; i < mThreadCount; ++i) {
            mAsyncWork.add(new AsyncWork());
        }
    }

    public void reset() {
        for (AsyncWork work : mAsyncWork) {
            work.reset();
        }
    }

    public void release() {
        for (AsyncWork work : mAsyncWork) {
            work.release();
        }

        mAsyncWork.clear();
    }

    public synchronized AsyncWork mallocWork() {
        AsyncWork ret = mAsyncWork.get(mIndex);
        mIndex += 1;
        if (mIndex >= mThreadCount) {
            mIndex = 0;
        }
        return ret;
    }
}
