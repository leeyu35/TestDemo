package com.tmall.wireless.falcon.view;

import android.view.View;
import android.view.ViewGroup;

import com.libra.common.Common;
import com.libra.common.LogHelper;
import com.libra.common.StringTab;
import com.tmall.wireless.falcon.PageContext;
import com.tmall.wireless.falcon.common.Const;
import com.tmall.wireless.falcon.core.IBean;
import com.tmall.wireless.falcon.core.IBuilder;
import com.tmall.wireless.falcon.core.IView;
import com.tmall.wireless.falcon.core.StubView;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by gujicheng on 17/5/13.
 */

public class Grid extends ViewGroup implements IView {
    private final static String TAG = "Grid";

    protected final static int DEFAULT_COLUMN_COUNT = 2;
    protected final static int DEFAULT_ITEM_HEIGHT = 0;

    protected int mColumnCount = DEFAULT_COLUMN_COUNT;
    protected int mRowCount;
    protected int mItemHeight = DEFAULT_ITEM_HEIGHT;
    protected int mItemWidth;
    protected int mCalItemHeight;
    protected int mComponentId;

    protected int mItemHorizontalMargin = 0;
    protected int mItemVerticalMargin = 0;

    private ViewAttrSetter mViewAttrSetter;
    protected PageContext mPageContext;
    protected int mId;
    protected IBean mBean;
    protected Object mRawData;

    protected int mAutoDimDirection = Common.AUTO_DIM_DIRECTION_NONE;
    protected float mAutoDimX = 1;
    protected float mAutoDimY = 1;

    private Object mData = null;

    public static class Builder implements IBuilder {
        @Override
        public String getName() {
            return "Grid";
        }

        @Override
        public IView build(PageContext pageContext, ViewCache viewCache) {
            return new Grid(pageContext, viewCache);
        }
    }

    public Grid(PageContext context, ViewCache viewCache) {
        super(context.getContext());

        mPageContext = context;
        mViewAttrSetter = new ViewAttrSetter(context, this, viewCache);
    }

    public void setColumnCount(int count) {
        mColumnCount = count;
    }

    public void setItemHorizontalMargin(int margin) {
        mItemHorizontalMargin = margin;
    }

    public void setItemVerticalMargin(int margin) {
        mItemVerticalMargin = margin;
    }

    public void setItemHeight(int h) {
        mItemHeight = h;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int space = this.getPaddingLeft() + this.getPaddingRight() + mItemHorizontalMargin * (mColumnCount - 1);

        int childCount = this.getChildCount();

//        Log.d(TAG, "width:" + width+ "  widthMode:" + widthMode + "  height:" + height + "  heightMode:" + heightMode);
        mItemWidth = (width - space) / mColumnCount;
        int index = 0;
        if (mItemHeight <= 0) {
            if (childCount > 0) {
                View child = this.getChildAt(index++);
                child.measure(MeasureSpec.makeMeasureSpec(mItemWidth, MeasureSpec.EXACTLY), heightMeasureSpec);
                mCalItemHeight = child.getMeasuredHeight();
            } else {
                mCalItemHeight = height - this.getPaddingTop() - this.getPaddingBottom();
            }
        } else {
            mCalItemHeight = mItemHeight;
        }

//        Log.d(TAG, "mCalItemHeight:" + mCalItemHeight + "mItemWidth:" + mItemWidth);

        int widthSpec = MeasureSpec.makeMeasureSpec(mItemWidth, MeasureSpec.EXACTLY);
        int heightSpec = MeasureSpec.makeMeasureSpec(mCalItemHeight, MeasureSpec.EXACTLY);
        for (int i = index; i < childCount; ++i) {
            final View child = this.getChildAt(i);
            child.measure(widthSpec, heightSpec);
        }

        mRowCount = (childCount / mColumnCount) + ((childCount % mColumnCount) > 0 ? 1 : 0);
        int h = mRowCount * mCalItemHeight + (mRowCount - 1) * mItemVerticalMargin + this.getPaddingTop() + this.getPaddingBottom();

        int realHeight;
        if (MeasureSpec.UNSPECIFIED == heightMode) {
            realHeight = h;
        } else {
            realHeight = Math.min(height, h);
        }

        setMeasuredDimension(width, realHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = this.getChildCount();
//        LogHelper.d(TAG, "onLayout count:" + count);

        int index = 0;
        int top = this.getPaddingTop();
        int paddingLeft = this.getPaddingLeft();
        for (int row = 0; row < mRowCount; ++row) {
            int left = paddingLeft;
            for (int col = 0; col < mColumnCount; ++col) {
                if (index < count) {
                    View child = this.getChildAt(index);
//                    LogHelper.d(TAG, "layout left:" + left + "  top:" + top + " width:" + mItemWidth + "  height:" + mCalItemHeight);
                    if (child instanceof StubView) {
                        ((StubView)child).vLayout(left, top, left + mItemWidth, top + mCalItemHeight);
                    } else {
                        child.layout(left, top, left + mItemWidth, top + mCalItemHeight);
                    }
                } else {
                    break;
                }
                ++index;
                left += mItemWidth + mItemHorizontalMargin;
            }
            top += mCalItemHeight + mItemVerticalMargin;
        }
//        LogHelper.d(TAG, "onLayout end count:" + count);
    }

    @Override
    public boolean setIntValue(int key, int value) {
        boolean ret = true;

        switch (key) {
            case StringTab.STR_ID_columnCount:
                mColumnCount = value;
                break;

            case StringTab.STR_ID_itemHorizontalMargin:
                mItemHorizontalMargin = value;
                break;

            case StringTab.STR_ID_itemVerticalMargin:
                mItemVerticalMargin = value;
                break;

            case StringTab.STR_ID_itemHeight:
                mItemHeight = value;
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
            case StringTab.STR_ID_itemHorizontalMargin:
                mItemHorizontalMargin = (int)value;
                break;

            case StringTab.STR_ID_itemVerticalMargin:
                mItemVerticalMargin = (int)value;
                break;

            case StringTab.STR_ID_itemHeight:
                mItemHeight = (int)value;
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
    public void reset() {
//        setPadding(0, 0, 0, 0);
//        setVisibility(View.VISIBLE);

//        MarginLayoutParams lp = this.getVLayoutParams();
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

    private void recycleViews() {
        int childCount = getChildCount();
        if (childCount > 0) {
            IView views[] = new IView[childCount];
            int count = 0;
            for(int i = 0; i < childCount; ++i) {
                View v = getChildAt(i);
                if (v instanceof IView) {
                    ((IView) v).reset();
                    views[count] = (IView)v;
                    ++count;
                }
            }
            removeAllViews();

            for (int i = 0; i < count; ++i) {
                mPageContext.recycleComponent(views[i]);
            }
        }
    }

    @Override
    public void setData(Object data) {
        if (null != data && data != mData) {
            mData = data;
            if (data instanceof JSONArray) {
                JSONArray arr = (JSONArray) data;
                if (null != arr) {
                    int size = arr.length();
                    boolean recycled = false;

                    if (getChildCount() != size) {
                        recycleViews();
                        recycled = true;
                    } else {
                        int n = getChildCount();
                        for (int i = 0; i < n; ++i) {
                            ((IView)getChildAt(i)).reset();
                        }
                    }

                    for (int i = 0; i < size; ++i) {
                        JSONObject obj = arr.optJSONObject(i);
                        if (null != obj) {
                            String template = obj.optString(Const.TEMPLATE);
                            if (recycled) {
                                IView v = mPageContext.newComponent(template);
                                if (null != v) {
                                    if (v instanceof View) {
                                        v.setComponentData(obj);
                                        this.addView((View)v);
                                    } else {
                                        LogHelper.e(TAG, "not view");
                                    }
                                } else {
                                    LogHelper.e(TAG, "new item failed");
                                }
                            } else {
                                ((IView)this.getChildAt(i)).setComponentData(obj);
                            }
                        }
                    }
                }
            } else {
                LogHelper.e(TAG, "data is not array");
            }
        } else {
//            LogHelper.e(TAG, "data is null");
        }
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
//        setFlag(this, PFLAG_LAYOUT_REQUIRED);
        layout(left, top, right, bottom);
    }

    @Override
    public boolean isGone() {
        return (this.getVisibility() == GONE);
    }

    @Override
    public void setVLayoutParams(MarginLayoutParams param) {
        this.setLayoutParams(param);
    }

    @Override
    public void setComponentData(Object data) {
        mViewAttrSetter.setComponentData(data);
    }

    @Override
    public void setVMeasuredDimension(int widthMeasureSpec, int heightMeasureSpec) {
        this.setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public MarginLayoutParams getVLayoutParams() {
        return (MarginLayoutParams)this.getLayoutParams();
    }

    @Override
    public void onLoadFinished() {

    }

    @Override
    public void release() {

    }
}
