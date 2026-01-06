package com.tmall.wireless.falcon.view;

import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.TextView;

import com.libra.common.Common;
import com.libra.common.LogHelper;
import com.libra.common.StringTab;
import com.tmall.wireless.falcon.PageContext;
import com.tmall.wireless.falcon.common.Utils;
import com.tmall.wireless.falcon.core.IBean;
import com.tmall.wireless.falcon.core.IBuilder;
import com.tmall.wireless.falcon.core.IView;

/**
 * Created by gujicheng on 17/5/10.
 */

public class Text extends TextView implements IView {
    private final static String TAG = "Text";

    private PageContext mPageContext;
    private ViewAttrSetter mViewAttrSetter;
    protected int mId;
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
            return "TextView";
        }

        @Override
        public IView build(PageContext pageContext, ViewCache viewCache) {
            return new Text(pageContext, viewCache);
        }
    }

    public Text(PageContext pageContext, ViewCache viewCache) {
        super(pageContext.getContext());

        mPageContext = pageContext;
        mViewAttrSetter = new ViewAttrSetter(mPageContext, this, viewCache);
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
        if (data instanceof String) {
            setText((String)data);
        } else {
            LogHelper.e(TAG, "setData not String:" + data);
        }
    }

    @Override
    public boolean setStyle(int keyId, Object value) {
        boolean ret = true;
        switch (keyId) {
            case StringTab.STR_ID_textSize:
                this.setTextSize((Float)value);
                break;

            case StringTab.STR_ID_textColor:
                this.setTextColor((Integer)value);
                break;

            case StringTab.STR_ID_textStyle:
                setTextStyle((Integer) value);
                break;

            case StringTab.STR_ID_ellipsize:
                setEllipsize(TextUtils.TruncateAt.values()[(Integer) value]);
                break;

            case StringTab.STR_ID_gravity:
                setGravity((Integer) value);
                break;

            default:
                ret = mViewAttrSetter.setStyle(keyId, value);
        }
        return ret;
    }

    @Override
    public void requestLayout() {
//        super.requestLayout();
    }

    @Override
    public void invalidate() {
//        super.invalidate();
    }

    @Override
    public void invalidate(int l, int t, int r, int b) {
//        super.invalidate(l, t, r, b);
    }

    @Override
    public void invalidate(Rect dirty) {
//        super.invalidate(dirty);
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
        Utils.setFlag(this, Utils.PFLAG_FORCE_LAYOUT);
//        if (TextUtils.equals("title", mId)) {
//            int height = View.MeasureSpec.getSize(heightMeasureSpec);
//
//            int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
//            LogHelper.d(TAG, "measure height:" + height + "  heightMode:" + heightMode + "  text:" + getText());
//        }
        measure(widthMeasureSpec, heightMeasureSpec);

//        if (TextUtils.equals("title", mId)) {
//            LogHelper.d(TAG, "measure  width:" + getVMeasuredWidth() + "  height:" + getMeasuredHeight() + "  text:" + getText());
//        }
    }

    @Override
    public int getVMeasuredWidth() {
        return getMeasuredWidth();
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
        return (getVisibility() == GONE);
    }

    @Override
    public void setVLayoutParams(ViewGroup.MarginLayoutParams param) {
        setLayoutParams(param);
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
        return (ViewGroup.MarginLayoutParams)getLayoutParams();
    }

    @Override
    public boolean setIntValue(int key, int value) {
        boolean ret = true;
        switch (key) {
            case StringTab.STR_ID_textSize:
                setTextSize(value);
                break;

            case StringTab.STR_ID_textColor:
                setTextColor(value);
                break;

            case StringTab.STR_ID_textStyle:
                setTextStyle(value);
                break;

            case StringTab.STR_ID_ellipsize:
                setEllipsize(TextUtils.TruncateAt.values()[value]);
                break;

            case StringTab.STR_ID_gravity:
                setGravity(value);
                break;

            default:
                ret = mViewAttrSetter.setIntValue(key, value);
        }
        return ret;
    }

    private void setTextStyle(int style) {
        int flag = Paint.ANTI_ALIAS_FLAG;
        if (0 != (style & Common.TEXT_STYLE_BOLD)) {
            flag |= Paint.FAKE_BOLD_TEXT_FLAG;
        }
        if (0 != (style & Common.TEXT_STYLE_STRIKE)) {
            flag |= Paint.STRIKE_THRU_TEXT_FLAG;
        }
        setPaintFlags(flag);

        if (0 != (style & Common.TEXT_STYLE_ITALIC)) {
            setTypeface(null, Typeface.BOLD_ITALIC);
        }
    }

    @Override
    public boolean setFloatValue(int key, float value) {
        boolean ret = true;
        switch (key) {
            case StringTab.STR_ID_textSize:
                setTextSize(value);
                break;

            default:
                ret = mViewAttrSetter.setFloatValue(key, value);
        }
        return ret;
    }

    @Override
    public boolean setStrValue(int key, String value) {
        boolean ret = true;
        switch (key) {
            case StringTab.STR_ID_text:
                this.setText(value);
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
        mPageContext = null;
        this.setText("");
    }
}
