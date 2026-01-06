package com.libra.common.io.writer;

import com.libra.common.LogHelper;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by gujicheng on 17/5/9.
 */

public class FileWriter implements Writer {
    private final static String TAG = "FileWriter";

    private RandomAccessFile mFile;

    public FileWriter(RandomAccessFile file) {
        mFile = file;
    }

    @Override
    public void init() {

    }

    @Override
    public void reset() {
    }

    @Override
    public void release() {
        if (null != mFile) {
            try {
                mFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            mFile = null;
        }
    }

    @Override
    public void writeBytes(byte[] value) {
        if (null != mFile && null != value) {
            try {
                mFile.write(value);
            } catch (IOException e) {
                LogHelper.e(TAG, "seek failed");
                e.printStackTrace();
            }
        } else {
            LogHelper.e(TAG, "writeBytes params is invalidate");
        }
    }

    @Override
    public void writeByte(int value) {
        if (null != mFile) {
            try {
                mFile.writeByte(value);
            } catch (IOException e) {
                LogHelper.e(TAG, "seek failed");
                e.printStackTrace();
            }
        } else {
            LogHelper.e(TAG, "writeShort params is invalidate");
        }
    }

    @Override
    public void writeShort(int value) {
        if (null != mFile) {
            try {
                mFile.writeShort(value);
            } catch (IOException e) {
                LogHelper.e(TAG, "seek failed");
                e.printStackTrace();
            }
        } else {
            LogHelper.e(TAG, "writeShort params is invalidate");
        }
    }

    @Override
    public void writeInt(int value) {
        if (null != mFile) {
            try {
                mFile.writeInt(value);
            } catch (IOException e) {
                LogHelper.e(TAG, "seek failed");
                e.printStackTrace();
            }
        } else {
            LogHelper.e(TAG, "writeInt params is invalidate");
        }
    }

    @Override
    public boolean seekRel(int offset) {
        return false;
    }

    @Override
    public boolean seek(int offset) {
        boolean ret = false;
        if (null != mFile) {
            try {
                mFile.seek(offset);
                ret = true;
            } catch (IOException e) {
                LogHelper.e(TAG, "seek failed");
                e.printStackTrace();
            }
        } else {
            LogHelper.e(TAG, "seek params is invalidate");
        }

        return ret;
    }
}
