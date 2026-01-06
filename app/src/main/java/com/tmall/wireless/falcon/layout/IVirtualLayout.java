package com.tmall.wireless.falcon.layout;

/**
 * Created by gujicheng on 17/5/18.
 */

public interface IVirtualLayout extends ILayout {
    void onVMeasure(int widthMeasureSpec, int heightMeasureSpec);
    void onVLayout(boolean changed, int left, int top, int right, int bottom);
}
