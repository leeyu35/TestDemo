package com.libra.common.io.loader.reader;

import android.os.Environment;
import android.text.TextUtils;

import com.libra.common.LogHelper;
import com.libra.common.io.reader.BufferReader;
import com.libra.common.io.reader.Reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by gujicheng on 17/5/10.
 */

public class SdcardReaderLoader implements ReaderLoader {
    private final static String TAG = "SdcardReaderLoader";

    @Override
    public void release() {
    }

    @Override
    public Reader getReader(String location, Object param) {
        Reader ret = null;

        if (!TextUtils.isEmpty(location)) {
            try {
                String path = Environment.getExternalStorageDirectory() +  File.separator + location;
                FileInputStream fin = new FileInputStream(path);
                int length = fin.available();
                byte[] buf = new byte[length];
                fin.read(buf);
                fin.close();

                BufferReader br = new BufferReader();
                br.setBuffer(buf);

                ret = br;
            } catch (FileNotFoundException e) {
                LogHelper.e(TAG, "new read file failed:" + location);
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            LogHelper.e(TAG, "location is empty");
        }

        return ret;
    }
}
