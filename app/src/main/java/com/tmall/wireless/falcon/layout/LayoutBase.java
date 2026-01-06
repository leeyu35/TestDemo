package com.tmall.wireless.falcon.layout;

import android.view.View;
import android.view.ViewGroup;

import com.libra.common.Common;
import com.libra.common.StringTab;
import com.tmall.wireless.falcon.PageContext;
import com.tmall.wireless.falcon.core.IAttr;
import com.tmall.wireless.falcon.core.IBean;
import com.tmall.wireless.falcon.core.IView;
import com.tmall.wireless.falcon.view.Slot;
import com.tmall.wireless.falcon.view.ViewAttrSetter;
import com.tmall.wireless.falcon.view.ViewCache;

/**
 * Created by gujicheng on 17/5/10.
 */

public abstract class LayoutBase extends ViewGroup implements ILayout {
    private final static String TAG = "LayoutBase";

    protected PageContext mPageContext;
    private ViewAttrSetter mViewAttrSetter;
    protected int mId;
    protected ViewCache mViewCache;
    protected int mChildCount;
    protected int mComponentId;
    protected IBean mBean;
    protected Object mRawData;
    protected int mAutoDimDirection = Common.AUTO_DIM_DIRECTION_NONE;
    protected float mAutoDimX = 1;
    protected float mAutoDimY = 1;

    public LayoutBase(PageContext pageContext, ViewCache viewCache) {
        super(pageContext.getContext());

        mViewCache = viewCache;
        mPageContext = pageContext;
        mViewAttrSetter = new ViewAttrSetter(mPageContext, this, viewCache);
    }

    @Override
    public void reset() {
        int count = getChildCount();
        for (int i = 0; i < count; ++i) {
            View v = this.getChildAt(i);
            ((IView)v).reset();
        }

//        setPadding(0, 0, 0, 0);
//        setVisibility(View.VISIBLE);
//
//        ViewGroup.MarginLayoutParams lp = this.getVLayoutParams();
//        if (null != lp && lp instanceof IAttr) {
//            ((IAttr)lp).reset();
//        }
    }

    @Override
    public void setAutoDimDirection(int value) {
        mAutoDimDirection = value;
    }

    @Override
    public void setAutoDimX(float value) {
        mAutoDimX = value;
    }

    @Override
    public void setAutoDimY(float value) {
        mAutoDimY = value;
    }

    @Override
    public Object getRawData() {
        return mRawData;
    }

    @Override
    public void setRawData(Object data) {
        mRawData = data;
    }

    @Override
    public IBean getBean() {
        return mBean;
    }

    @Override
    public void setBean(IBean bean) {
        mBean = bean;
    }

    @Override
    public int getComponentId() {
        return mComponentId;
    }

    @Override
    public void setComponentId(int id) {
        mComponentId = id;
    }

    @Override
    public int getVLeft() {
        return 0;
    }

    @Override
    public int getVTop() {
        return 0;
    }

    @Override
    public void vMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public int getVMeasuredWidth() {
        return this.getMeasuredWidth();
    }

    @Override
    public int getVMeasuredHeight() {
        return this.getMeasuredHeight();
    }

    @Override
    public int getVMeasuredWidthWithMargin() {
        MarginLayoutParams p = this.getVLayoutParams();
        return this.getMeasuredWidth() + p.leftMargin + p.rightMargin;
    }

    @Override
    public int getVMeasuredHeightWithMargin() {
        MarginLayoutParams p = this.getVLayoutParams();
        return this.getMeasuredHeight() + p.topMargin + p.bottomMargin;
    }

    @Override
    public void vLayout(int left, int top, int right, int bottom) {
        layout(left, top, right, bottom);
    }

    @Override
    public boolean isGone() {
        return (getVisibility() == GONE);
    }

    @Override
    public void setVMeasuredDimension(int widthMeasureSpec, int heightMeasureSpec) {
        this.setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void addChild(IView child) {
        if (child instanceof View) {
            this.addView((View)child);
        } else if (child instanceof Slot) {
            ((Slot)child).setParent(this, mChildCount);
        }
        ++mChildCount;
    }

    @Override
    public LayoutParams generateLayoutParams() {
        return new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public static class LayoutParams extends MarginLayoutParams implements IAttr {
        public LayoutParams(int width, int height) {
            super(width, height);
        }

        @Override
        public void reset() {
            leftMargin = 0;
            topMargin = 0;
            rightMargin = 0;
            bottomMargin = 0;
        }

        @Override
        public boolean setIntValue(int key, int value) {
            boolean ret = true;
            switch (key) {
                case StringTab.STR_ID_layout_width:
                    width = value;
                    break;

                case StringTab.STR_ID_layout_height:
                    height = value;
                    break;

                case StringTab.STR_ID_layout_marginLeft:
                    leftMargin = value;
                    break;

                case StringTab.STR_ID_layout_marginTop:
                    topMargin = value;
                    break;

                case StringTab.STR_ID_layout_marginRight:
                    rightMargin = value;
                    break;

                case StringTab.STR_ID_layout_marginBottom:
                    bottomMargin = value;
                    break;

                default:
                    ret = false;
            }
            return ret;
        }

        @Override
        public boolean setFloatValue(int key, float value) {
            boolean ret = true;
            switch (key) {
                case StringTab.STR_ID_layout_width:
                    width = (int)value;
                    break;

                case StringTab.STR_ID_layout_height:
                    height = (int)value;
                    break;

                case StringTab.STR_ID_layout_marginLeft:
                    leftMargin = (int)value;
                    break;

                case StringTab.STR_ID_layout_marginTop:
                    topMargin = (int)value;
                    break;

                case StringTab.STR_ID_layout_marginRight:
                    rightMargin = (int)value;
                    break;

                case StringTab.STR_ID_layout_marginBottom:
                    bottomMargin = (int)value;
                    break;

                default:
                    ret = false;
            }
            return ret;
        }

        @Override
        public boolean setStrValue(int key, String value) {
            return false;
        }

        @Override
        public boolean setStyle(int key, Object value) {
            boolean ret = true;
            switch (key) {
                case StringTab.STR_ID_layout_width:
                    width = (Integer) value;
                    break;

                case StringTab.STR_ID_layout_height:
                    height = (Integer) value;
                    break;

                case StringTab.STR_ID_layout_marginLeft:
                    leftMargin = (Integer) value;
                    break;

                case StringTab.STR_ID_layout_marginTop:
                    topMargin = (Integer) value;
                    break;

                case StringTab.STR_ID_layout_marginRight:
                    rightMargin = (Integer) value;
                    break;

                case StringTab.STR_ID_layout_marginBottom:
                    bottomMargin = (Integer) value;
                    break;

                default:
                    ret = false;
            }
            return ret;
        }
    }

    public static LayoutParams generateRootLayoutParams() {
        return new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void setVLayoutParams(MarginLayoutParams param) {
        setLayoutParams(param);
    }

    @Override
    public MarginLayoutParams getVLayoutParams() {
        return (MarginLayoutParams)this.getLayoutParams();
    }

    @Override
    public void release() {
        mPageContext = null;
    }

    @Override
    public int getVId() {
        return mId;
    }

    @Override
    public void setVId(int id) {
        mId = id;
    }

    @Override
    public void setComponentData(Object data) {
        mViewAttrSetter.setComponentData(data);
    }

    @Override
    public boolean setStyle(int keyId, Object value) {
        boolean ret = true;
        switch (keyId) {
            default:
                ret = mViewAttrSetter.setStyle(keyId, value);
        }
        return ret;
    }

    @Override
    public void setData(Object data) {
    }

    @Override
    public void setAuxiliary(Object auxiliary) {
    }

    @Override
    public boolean setIntValue(int key, int value) {
        return mViewAttrSetter.setIntValue(key, value);
    }

    @Override
    public boolean setFloatValue(int key, float value) {
        return mViewAttrSetter.setFloatValue(key, value);
    }

    @Override
    public boolean setStrValue(int key, String value) {
        return mViewAttrSetter.setStrValue(key, value);
    }

    @Override
    public void onLoadFinished() {
    }
}
