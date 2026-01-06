package com.tmall.wireless.falcon.view;

import android.view.ViewGroup;
import android.widget.ImageView;

import com.libra.common.Common;
import com.libra.common.StringTab;
import com.tmall.wireless.falcon.PageContext;
import com.tmall.wireless.falcon.core.IBean;
import com.tmall.wireless.falcon.core.IBuilder;
import com.tmall.wireless.falcon.core.IView;

/**
 * Created by gujicheng on 17/5/10.
 */

public class Image extends ImageView implements IView {
    private final static String TAG = "Image";

    private PageContext mPageContext;
    private ViewAttrSetter mViewAttrSetter;
    protected int mId;
//    protected ImageLoader mImageLoader;
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
            return "ImageView";
        }

        @Override
        public IView build(PageContext pageContext, ViewCache viewCache) {
            return new Image(pageContext, viewCache);
        }
    }

    public Image(PageContext pageContext, ViewCache viewCache) {
        super(pageContext.getContext());

        mPageContext = pageContext;
//        mImageLoader = mPageContext.getApplicationContext().getImageLoader();
        mViewAttrSetter = new ViewAttrSetter(mPageContext, this, viewCache);
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
//        if (data instanceof JSONObject) {
//            JSONObject obj = ((JSONObject) data);
//            int type = obj.optInt(MyCommon.DATA_TYPE, -1);
//            switch (type) {
//                case 1:
//                    String v = obj.optString(MyCommon.DATA_VALUE);
//                    if (null != v) {
//                        setDrawable(v);
//                    } else {
//                        LogHelper.e(TAG, "setData, can not find value");
//                    }
//                    break;
//
//                default:
//                    LogHelper.e(TAG, "setData, can not recognize type:" + type + " data:" + data);
//            }
//        } else if (data instanceof String) {
            mPageContext.getApplicationContext().setImageSrc((String)data, this);
//        }
    }

    @Override
    public boolean setStyle(int keyId, Object value) {
        boolean ret = true;
        switch (keyId) {
            case StringTab.STR_ID_scaleType:
                setScaleType(ScaleType.values()[(Integer) value]);
                break;

            default:
                ret = mViewAttrSetter.setStyle(keyId, value);
        }

        return ret;
    }

    @Override
    public void setAuxiliary(Object auxiliary) {

    }

    @Override
    public void setBean(IBean bean) {
        mBean = bean;
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
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        switch (mAutoDimDirection) {
            case Common.AUTO_DIM_DIRECTION_X:
                if (MeasureSpec.EXACTLY == MeasureSpec.getMode(widthMeasureSpec)) {
                    heightMeasureSpec = MeasureSpec.makeMeasureSpec((int)((MeasureSpec.getSize(widthMeasureSpec) * mAutoDimY) / mAutoDimX), MeasureSpec.EXACTLY);
                }
                break;

            case Common.AUTO_DIM_DIRECTION_Y:
                if (MeasureSpec.EXACTLY == MeasureSpec.getMode(heightMeasureSpec)) {
                    widthMeasureSpec = MeasureSpec.makeMeasureSpec((int)((MeasureSpec.getSize(heightMeasureSpec) * mAutoDimX) / mAutoDimY), MeasureSpec.EXACTLY);
                }
                break;
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
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
    public boolean setIntValue(int key, int value) {
        boolean ret = true;
        switch (key) {
            case StringTab.STR_ID_scaleType:
                setScaleType(ScaleType.values()[value]);
                break;

            default:
                ret = mViewAttrSetter.setIntValue(key, value);
        }
        return ret;
    }

    @Override
    public boolean setFloatValue(int key, float value) {
        return mViewAttrSetter.setFloatValue(key, value);
    }

//    private void setDrawable(String value) {
//        int id = Utils.getResId(mPageContext.getResourceClass(), value);
//        if (id > -1) {
//            this.setImageResource(id);
//        } else {
//            LogHelper.e(TAG, "can not find image:" + value);
//        }
//    }

    @Override
    public boolean setStrValue(int key, String value) {
        boolean ret = true;
        switch (key) {
            case StringTab.STR_ID_src:
                mPageContext.getApplicationContext().setImageSrc(value, this);
//                setDrawable(value);
                break;

            default:
                ret = mViewAttrSetter.setStrValue(key, value);
        }
        return ret;
    }

    @Override
    public void onLoadFinished() {
    }

    @Override
    public void release() {
//        LogHelper.d(TAG, "release");
        mPageContext = null;
        this.setImageBitmap(null);
    }
}
