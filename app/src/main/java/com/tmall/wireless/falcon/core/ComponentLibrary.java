package com.tmall.wireless.falcon.core;

import android.util.SparseArray;

import com.libra.common.Common;
import com.libra.common.LogHelper;
import com.libra.common.io.reader.BufferReader;
import com.libra.common.io.reader.Reader;
import com.tmall.wireless.falcon.common.Utils;

/**
 * Created by gujicheng on 17/5/10.
 */

public class ComponentLibrary {
    private final static String TAG = "ComponentLibrary";

    private Reader mReader;

    private SparseArray<Item> mLibrary = new SparseArray<>();

    public static class Item {
        public Item(int startPos, int count, int width, int height, int autoDimDirection, float autoDimX, float autoDimY) {
            mStartPos = startPos;
            mCount = count;
            mWidth = width;
            mHeight = height;
            mAutoDimDirection = autoDimDirection;
            mAutoDimX = autoDimX;
            mAutoDimY = autoDimY;
        }

        public int mWidth;
        public int mHeight;
        public int mAutoDimDirection;
        public float mAutoDimX;
        public float mAutoDimY;
        public int mStartPos;
        public int mCount;
    }

    public void release() {
        if (null != mReader) {
            mReader.release();
            mReader = null;
        }

        if (null != mLibrary) {
            mLibrary.clear();
            mLibrary = null;
        }
    }

    public Item getItem(int comNameId) {
        return mLibrary.get(comNameId);
    }

    public Reader getComponentReader(int comNameId) {
        Reader ret = null;

        Item item = mLibrary.get(comNameId);
        if (null != item && null != mReader) {
            Reader br = BufferReader.malloc();
            br.setBuffer(mReader.getBuffer());
            br.seek(item.mStartPos);
            ret = br;
        }

        return ret;
    }

    public boolean load(Reader reader) {
        boolean ret = false;

        if (null != reader) {
            mReader = reader;

            ret = true;
            int count = reader.readShort();
            if (count > 0) {
                for (int i = 0; i < count; ++i) {
                    int comNameId = reader.readShort();

                    // width;
                    int width = readItem(reader);

                    // height
                    int height = readItem(reader);

                    // autoDimDirection, autoDimX, autoDimY
                    int autoDimDirection = readItem(reader, -100);
                    float autoDimX = 1;
                    float autoDimY = 1;
                    if (autoDimDirection > Common.AUTO_DIM_DIRECTION_NONE) {
                        autoDimX = readFloatItem(reader);
                        autoDimY = readFloatItem(reader);
                    } else {
                        autoDimDirection = Common.AUTO_DIM_DIRECTION_NONE;
                    }

                    int contentSize = reader.readShort();
                    mLibrary.put(comNameId, new Item(reader.getPos(), contentSize, width, height, autoDimDirection, autoDimX, autoDimY));
                    if (!reader.seekRel(contentSize)) {
                        ret = false;
                        LogHelper.e(TAG, "load failed");
                        break;
                    }
                }
            } else {
                LogHelper.e(TAG, "component count is zero");
            }
        }

        return ret;
    }

    private int readItem(Reader reader) {
        return readItem(reader, 0);
    }

    private int readItem(Reader reader, int errorValue) {
        int ret = errorValue;

        int type = reader.readByte();
        switch (type) {
            case Common.ATTR_ITEM_TYPE_INT:
                ret = reader.readInt();
                break;
            case Common.ATTR_ITEM_TYPE_INT_WP:
                ret = Utils.wp2px(reader.readInt());
                break;
            case Common.ATTR_ITEM_TYPE_INT_DP:
                ret = Utils.dp2px(reader.readInt());
                break;
            case Common.ATTR_ITEM_TYPE_FLOAT:
                ret =  (int)Float.intBitsToFloat(reader.readInt());
                break;
            case Common.ATTR_ITEM_TYPE_FLOAT_WP:
                ret =  (int) Utils.wp2px_f(Float.intBitsToFloat(reader.readInt()));
                break;
            case Common.ATTR_ITEM_TYPE_FLOAT_DP:
                ret =  (int) Utils.dp2px_f(Float.intBitsToFloat(reader.readInt()));
                break;
        }

        return ret;
    }

    private float readFloatItem(Reader reader) {
        float ret = -100;

        int type = reader.readByte();
        switch (type) {
            case Common.ATTR_ITEM_TYPE_INT:
                ret = reader.readInt();
                break;
            case Common.ATTR_ITEM_TYPE_INT_WP:
                ret = Utils.wp2px(reader.readInt());
                break;
            case Common.ATTR_ITEM_TYPE_INT_DP:
                ret = Utils.dp2px(reader.readInt());
                break;
            case Common.ATTR_ITEM_TYPE_FLOAT:
                ret = Float.intBitsToFloat(reader.readInt());
                break;
            case Common.ATTR_ITEM_TYPE_FLOAT_WP:
                ret = Utils.wp2px_f(Float.intBitsToFloat(reader.readInt()));
                break;
            case Common.ATTR_ITEM_TYPE_FLOAT_DP:
                ret = Utils.dp2px_f(Float.intBitsToFloat(reader.readInt()));
                break;
        }

        return ret;
    }
}
