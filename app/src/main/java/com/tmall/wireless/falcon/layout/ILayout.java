package com.tmall.wireless.falcon.layout;

import android.view.ViewGroup;

import com.tmall.wireless.falcon.core.IView;

/**
 * Created by gujicheng on 17/5/12.
 */

public interface ILayout extends IView {
    ViewGroup.MarginLayoutParams generateLayoutParams();
    void addChild(IView child);
    void replaceChild(IView child, int index, ViewGroup.MarginLayoutParams params);
}
