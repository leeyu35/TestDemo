package com.libra.common.io.loader.reader;

import com.libra.common.io.reader.BufferReader;
import com.libra.common.io.reader.Reader;

/**
 * Created by gujicheng on 17/5/10.
 */

public class BufferReaderLoader implements ReaderLoader {
    private final static String TAG = "BufferReaderLoader";

    @Override
    public void release() {
    }

    @Override
    public Reader getReader(String location, Object param) {
        Reader ret = new BufferReader();
        if (param instanceof byte[]) {
            ret.setBuffer((byte[])param);
        }
        return ret;
    }
}
