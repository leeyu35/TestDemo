package com.libra.common.io.reader;

import com.libra.common.LogHelper;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by gujicheng on 17/5/10.
 */

public class BufferReader implements Reader {
    private final static String TAG = "BufferReader";

    private byte[] mCode;
    private int mCurIndex;
    private int mCount;

    private volatile static List<Reader> sReaderCache = new LinkedList<>();

    public synchronized static Reader malloc() {
        Reader ret;
        if (null != sReaderCache && sReaderCache.size() > 0) {
            ret = sReaderCache.remove(0);
        } else {
            ret = new BufferReader();
        }
        return ret;
    }

    public synchronized static void free(Reader reader) {
        if (null != reader && null != sReaderCache) {
            sReaderCache.add(reader);
        }
    }

    private synchronized static void releaseCache() {
        if (null != sReaderCache) {
            sReaderCache.clear();
            sReaderCache = null;
        }
    }

    @Override
    public void setBuffer(byte[] code) {
        mCode = code;
        if (null != mCode) {
            mCount = mCode.length;
        } else {
            mCount = 0;
        }

        mCurIndex = 0;
    }

    @Override
    public byte[] getBuffer() {
        return mCode;
    }

    @Override
    public void release() {
        if (null != mCode) {
            mCode = null;
        }

        releaseCache();
    }

    @Override
    public boolean seekRel(int pos) {
        return seek(mCurIndex + pos);
    }

    @Override
    public boolean seek(int pos) {
        boolean ret = false;
        if (pos > mCount) {
            mCurIndex = mCount;
        } else if (pos < 0) {
            mCurIndex = 0;
        } else {
            ret = true;
            mCurIndex = pos;
        }
        return ret;
    }

    @Override
    public int getPos() {
        return mCurIndex;
    }

    @Override
    public byte readByte()  {
        if (null != mCode && mCurIndex < mCount) {
            return mCode[mCurIndex++];
        } else {
            LogHelper.e(TAG, "readByte error mCode:" + mCode + "  mCurIndex:"
                    + mCurIndex + "  mCount:" + mCount);
            return -1;
        }
    }

    @Override
    public short readShort()  {
        if (null != mCode && mCurIndex < mCount - 1) {
            return (short)(((mCode[mCurIndex++] & 0xff) << 8) | (mCode[mCurIndex++] & 0xff));
        } else {
            LogHelper.e(TAG, "readShort error mCode:" + mCode + "  mCurIndex:"
                    + mCurIndex + "  mCount:" + mCount);
            return -1;
        }
    }

    @Override
    public int readInt() {
        if (null != mCode && mCurIndex < mCount - 3) {
            return ((mCode[mCurIndex++] & 0xff) << 24) |
                    ((mCode[mCurIndex++] & 0xff) << 16) |
                    ((mCode[mCurIndex++] & 0xff) << 8) |
                    ((mCode[mCurIndex++] & 0xff));
        } else {
            LogHelper.e(TAG, "readInt error mCode:" + mCode + "  mCurIndex:"
                    + mCurIndex + "  mCount:" + mCount);
            return -1;
        }
    }
}
