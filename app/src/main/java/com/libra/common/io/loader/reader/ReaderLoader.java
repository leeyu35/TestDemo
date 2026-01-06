package com.libra.common.io.loader.reader;

import com.libra.common.io.reader.Reader;

/**
 * Created by gujicheng on 17/5/10.
 */

public interface ReaderLoader {
    void release();
    Reader getReader(String location, Object param);
}
