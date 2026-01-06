package com.libra.common.io.reader;

/**
 * Created by gujicheng on 17/5/9.
 */

public interface Reader {
    void release();
    boolean seekRel(int pos);
    boolean seek(int pos);
    void setBuffer(byte[] code);
    byte[] getBuffer();
    int getPos();
    byte readByte();
    short readShort();
    int readInt();
}
