package com.tmall.wireless.falcon.core;

import android.view.ViewGroup;

/**
 * Created by gujicheng on 17/5/11.
 */

public interface IView extends IAttr {
    int getComponentId();
    void setComponentId(int id);
    int getVId();
    void setVId(int id);
    void setData(Object data);
    boolean setStyle(int keyId, Object value);
    void setAuxiliary(Object auxiliary);
    void setBean(IBean bean);
    IBean getBean();
    void setRawData(Object data);
    Object getRawData();
    void setAutoDimDirection(int value);
    void setAutoDimX(float value);
    void setAutoDimY(float value);

    int getVPaddingLeft();
    int getVPaddingTop();
    int getVPaddingRight();
    int getVPaddingBottom();

    int getVLeft();
    int getVTop();

    void vMeasure(int widthMeasureSpec, int heightMeasureSpec);
    int getVMeasuredWidth();
    int getVMeasuredHeight();
    int getVMeasuredWidthWithMargin();
    int getVMeasuredHeightWithMargin();
    void vLayout(int left, int top, int right, int bottom);
    boolean isGone();

    void setVMeasuredDimension(int widthMeasureSpec, int heightMeasureSpec);

    ViewGroup.MarginLayoutParams getVLayoutParams();
    void setVLayoutParams(ViewGroup.MarginLayoutParams param);
    void setComponentData(Object data);
    void onLoadFinished();
    void release();
}
