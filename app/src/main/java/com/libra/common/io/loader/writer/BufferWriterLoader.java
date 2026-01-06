package com.libra.common.io.loader.writer;

import com.libra.common.io.writer.BufferWriter;
import com.libra.common.io.writer.Writer;

/**
 * Created by gujicheng on 17/5/10.
 */

public class BufferWriterLoader implements WriterLoader {
    @Override
    public Writer getWriter(String location) {
        return new BufferWriter();
    }
}
