package com.libra.common.io.writer;

/**
 * Created by gujicheng on 17/5/9.
 */

public interface Writer {
    void init();
    void reset();
    void release();
    void writeBytes(byte[] value);
    void writeByte(int value);
    void writeShort(int value);
    void writeInt(int value);
    boolean seek(int offset);
    boolean seekRel(int offset);
}
