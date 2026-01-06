package com.libra.common.io.loader.writer;

import android.os.Environment;
import android.text.TextUtils;

import com.libra.common.LogHelper;
import com.libra.common.io.writer.FileWriter;
import com.libra.common.io.writer.Writer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

/**
 * Created by gujicheng on 17/5/10.
 */

public class SdcardWriterLoader implements WriterLoader {
    private final static String TAG = "SdcardWriterLoader";

    @Override
    public Writer getWriter(String location) {
        Writer ret = null;
        if (!TextUtils.isEmpty(location)) {
            try {
                location = Environment.getExternalStorageDirectory() +  File.separator + location;
                RandomAccessFile file = new RandomAccessFile(location, "rw");
                if (null != file) {
                    ret = new FileWriter(file);
                } else {
                    LogHelper.e(TAG, "file is null:" + location);
                }
            } catch (FileNotFoundException e) {
                LogHelper.e(TAG, "new RandomAccessFile failed:" + location);
                e.printStackTrace();
            }
        } else {
            LogHelper.e(TAG, "location is empty");
        }

        return ret;
    }
}
