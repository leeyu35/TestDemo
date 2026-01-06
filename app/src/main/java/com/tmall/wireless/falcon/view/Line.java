package com.tmall.wireless.falcon.view;

import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;

import com.libra.common.Common;
import com.libra.common.StringTab;
import com.tmall.wireless.falcon.PageContext;
import com.tmall.wireless.falcon.core.IBean;
import com.tmall.wireless.falcon.core.IBuilder;
import com.tmall.wireless.falcon.core.IView;

/**
 * Created by gujicheng on 17/5/14.
 */

public class Line extends View implements IView {
    private final static String TAG = "Line";

    protected boolean mIsHorizontal;
    protected int mLineColor;
    protected int mLineWidth = 1;
    protected int mStyle;
    protected float[] mDashEffect = {3, 5, 3, 5};

    private ViewAttrSetter mViewAttrSetter;
    protected PageContext mPageContext;
    protected int mId;

    protected Paint mPaint;

    protected int mVLeft;
    protected int mVTop;
    protected int mComponentId;
    protected IBean mBean;
    protected Object mRawData;
    protected int mAutoDimDirection = Common.AUTO_DIM_DIRECTION_NONE;
    protected float mAutoDimX = 1;
    protected float mAutoDimY = 1;

    public static class Builder implements IBuilder {
        @Override
        public String getName() {
            return "Line";
        }

        @Override
        public IView build(PageContext pageContext, ViewCache viewCache) {
            return new Line(pageContext, viewCache);
        }
    }

    public Line(PageContext pageContext, ViewCache viewCache) {
        super(pageContext.getContext());
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mViewAttrSetter = new ViewAttrSetter(pageContext, this, viewCache);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        ViewGroup.LayoutParams lp = this.getLayoutParams();
        int width = lp.width;
        int height = lp.height;
        if (mIsHorizontal) {
            if (width <= 0) {
                width = MeasureSpec.getSize(widthMeasureSpec);
            }
            height = mLineWidth;
        } else {
            width = mLineWidth;
            if (height <= 0) {
                height = MeasureSpec.getSize(heightMeasureSpec);
            }
        }

        setMeasuredDimension(width, height);
    }

    @Override
    public boolean setIntValue(int key, int value) {
        boolean ret = true;
        switch (key) {
            case StringTab.STR_ID_lineOrientation:
                mIsHorizontal = (Common.ORIENTATION_HORIZONTAL == value) ? true : false;
                break;

            case StringTab.STR_ID_lineStyle:
                mStyle = value;
                setStyle(mStyle);
                break;

            case StringTab.STR_ID_lineColor:
                mLineColor = value;
                mPaint.setColor(mLineColor);
                break;

            case StringTab.STR_ID_lineWidth:
                mLineWidth = value;
                mPaint.setStrokeWidth(mLineWidth);
                break;

            default:
                ret = mViewAttrSetter.setIntValue(key, value);
        }
        return ret;
    }

    @Override
    public boolean setFloatValue(int key, float value) {
        boolean ret = true;
        switch (key) {
            case StringTab.STR_ID_lineWidth:
                mLineWidth = (int)value;
                mPaint.setStrokeWidth(mLineWidth);
                break;

            default:
                ret = mViewAttrSetter.setFloatValue(key, value);
        }
        return ret;
    }

    @Override
    public boolean setStrValue(int key, String value) {
        return mViewAttrSetter.setStrValue(key, value);
    }

    @Override
    public boolean setStyle(int keyId, Object value) {
        boolean ret = true;
        switch (keyId) {
            case StringTab.STR_ID_lineStyle:
                mStyle = (Integer) value;
                setStyle(mStyle);
                break;

            case StringTab.STR_ID_lineColor:
                mLineColor = (Integer) value;
                mPaint.setColor(mLineColor);
                break;

            case StringTab.STR_ID_lineWidth:
                mLineWidth = (Integer) value;
                mPaint.setStrokeWidth(mLineWidth);
                break;

            default:
                ret = mViewAttrSetter.setStyle(keyId, value);
        }
        return ret;
    }

    @Override
    public void reset() {
//        setPadding(0, 0, 0, 0);
//        setVisibility(View.VISIBLE);
//
//        ViewGroup.MarginLayoutParams lp = this.getVLayoutParams();
//        if (null != lp && lp instanceof IAttr) {
//            ((IAttr)lp).reset();
//        }
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
    public int getVId() {
        return mId;
    }

    @Override
    public void setVId(int id) {
        mId = id;
    }

    @Override
    public void setData(Object data) {
    }

    @Override
    public void setAuxiliary(Object auxiliary) {
    }

    @Override
    public IBean getBean() {
        return mBean;
    }

    @Override
    public void setRawData(Object data) {
        mRawData = data;
    }

    @Override
    public Object getRawData() {
        return mRawData;
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
    public void setBean(IBean bean) {
        mBean = bean;
    }

    @Override
    public int getVPaddingLeft() {
        return getPaddingLeft();
    }

    @Override
    public int getVPaddingTop() {
        return getPaddingTop();
    }

    @Override
    public int getVPaddingRight() {
        return getPaddingRight();
    }

    @Override
    public int getVPaddingBottom() {
        return getPaddingBottom();
    }

    @Override
    public int getVLeft() {
        return mVLeft;
    }

    @Override
    public int getVTop() {
        return mVTop;
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
        return getMeasuredHeight();
    }

    @Override
    public int getVMeasuredWidthWithMargin() {
        ViewGroup.MarginLayoutParams p = this.getVLayoutParams();
        return this.getMeasuredWidth() + p.leftMargin + p.rightMargin;
    }

    @Override
    public int getVMeasuredHeightWithMargin() {
        ViewGroup.MarginLayoutParams p = this.getVLayoutParams();
        return this.getMeasuredHeight() + p.topMargin + p.bottomMargin;
    }

    @Override
    public void vLayout(int left, int top, int right, int bottom) {
        mVLeft = left;
        mVTop = top;
//        layout(left, top, right, bottom);
    }

    @Override
    public boolean isGone() {
        return (this.getVisibility() == GONE);
    }

    @Override
    public void setVLayoutParams(ViewGroup.MarginLayoutParams param) {
        this.setLayoutParams(param);
    }

    @Override
    public void setComponentData(Object data) {
        mViewAttrSetter.setComponentData(data);
    }

    @Override
    public void setVMeasuredDimension(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public ViewGroup.MarginLayoutParams getVLayoutParams() {
        return (ViewGroup.MarginLayoutParams)this.getLayoutParams();
    }

    @Override
    public void onLoadFinished() {
    }

    @Override
    public void release() {
    }

    public void setColor(int color) {
        mPaint.setColor(color);
    }

    public void setStyle(int style) {
        switch (style) {
            case Common.LINE_STYLE_DASH:
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setPathEffect(new DashPathEffect(mDashEffect, 1));
                this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                break;

            case Common.LINE_STYLE_SOLID:
                mPaint.setStyle(Paint.Style.FILL);
                break;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = this.getMeasuredWidth();
        int height = this.getMeasuredHeight();

        int strokeWidth = (int) mPaint.getStrokeWidth();
        if (mIsHorizontal) {
            int top = strokeWidth >> 1;
            canvas.drawLine(this.getPaddingLeft(), top, width - this.getPaddingRight(), top, mPaint);
        } else {
            int left = strokeWidth >> 1;
            canvas.drawLine(left, this.getPaddingTop(), left, height - this.getPaddingBottom(), mPaint);
        }
    }
}
