package com.tmall.wireless.falcon.core;

/**
 * Created by gujicheng on 17/5/12.
 */

public interface IAttr {
    void reset();

    boolean setIntValue(int key, int value);
    boolean setFloatValue(int key, float value);
    boolean setStrValue(int key, String value);

    boolean setStyle(int key, Object value);
}
