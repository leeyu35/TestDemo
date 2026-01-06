package com.libra.common.io.loader.writer;

import com.libra.common.io.writer.Writer;

/**
 * Created by gujicheng on 17/5/10.
 */

public interface WriterLoader {
    Writer getWriter(String location);
}
