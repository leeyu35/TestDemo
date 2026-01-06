package com.libra.common.io.writer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gujicheng on 17/5/10.
 */

public class BufferWriter implements Writer {
    private List<Byte> mBuffer = new ArrayList<>();

    public List<Byte> cloneBuffer() {
        List<Byte> ret = new ArrayList<>();
        ret.addAll(mBuffer);
        return ret;
    }

    @Override
    public void init() {
    }

    @Override
    public void reset() {
        mBuffer.clear();
    }

    @Override
    public void release() {
        mBuffer = null;
    }

    @Override
    public void writeBytes(byte[] value) {
    }

    @Override
    public void writeByte(int value) {
        mBuffer.add((byte)value);
    }

    @Override
    public void writeShort(int value) {
        mBuffer.add((byte) ((value & 0xff00) >> 8));
        mBuffer.add((byte) (value & 0xff));
    }

    @Override
    public void writeInt(int value) {
        mBuffer.add((byte) ((value & 0xff000000) >> 24));
        mBuffer.add((byte) ((value & 0xff0000) >> 16));
        mBuffer.add((byte) ((value & 0xff00) >> 8));
        mBuffer.add((byte) (value & 0xff));
    }

    @Override
    public boolean seek(int offset) {
        return false;
    }

    @Override
    public boolean seekRel(int offset) {
        return false;
    }
}
