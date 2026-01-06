package com.tmall.wireless.falcon.view;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.libra.common.Common;
import com.libra.common.LogHelper;
import com.libra.common.StringTab;
import com.tmall.wireless.falcon.PageContext;
import com.tmall.wireless.falcon.core.IBean;
import com.tmall.wireless.falcon.core.IBuilder;
import com.tmall.wireless.falcon.core.IView;

/**
 * Created by gujicheng on 17/5/11.
 */

public class Scroller extends RecyclerView implements IView {
    private final static String TAG = "Scroller";

    protected LayoutManager mLM;
    protected int mMode = Common.SCROLLER_MODE_Linear;
    protected int mOrientation = LinearLayoutManager.VERTICAL;
    protected ScrollerAdapter mAdapter;
    private ViewAttrSetter mViewAttrSetter;
    protected int mId;
    protected int mComponentId;
    protected PageContext mPageContext;
    private Object mData;
    protected IBean mBean;
    protected Object mRawData;
    protected int mAutoDimDirection = Common.AUTO_DIM_DIRECTION_NONE;
    protected float mAutoDimX = 1;
    protected float mAutoDimY = 1;

    public static class Builder implements IBuilder {
        @Override
        public String getName() {
            return "Scroller";
        }

        @Override
        public IView build(PageContext pageContext, ViewCache viewCache) {
            return new Scroller(pageContext, viewCache);
        }
    }

    public Scroller(PageContext pageContext, ViewCache viewCache) {
        super(pageContext.getContext());

        mPageContext = pageContext;

        this.setRecyclerListener(new RecyclerListener() {
            @Override
            public void onViewRecycled(ViewHolder viewHolder) {
                if (viewHolder.itemView instanceof IView) {
//                    int id = ((IView)viewHolder.itemView).getComponentId();
//                    LogHelper.d(TAG, "onViewRecycled:" +  id + "  name:" + mPageContext.getStringTab().getString(id));
                    ((IView)viewHolder.itemView).reset();
                }
            }
        });

        this.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mViewAttrSetter = new ViewAttrSetter(pageContext, this, viewCache);

        mAdapter = new ScrollerAdapter(pageContext, this);
        this.setAdapter(mAdapter);

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
        // parse data
        if (mData != data) {
            mAdapter.setData(data);
            mAdapter.notifyDataSetChanged();
            mData = data;
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
        return getMeasuredWidth();
    }

    @Override
    public int getVMeasuredHeight() {
        return getMeasuredHeight();
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
    public void setVLayoutParams(MarginLayoutParams param) {
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
    public MarginLayoutParams getVLayoutParams() {
        return (MarginLayoutParams)this.getLayoutParams();
    }

    @Override
    public void onLoadFinished() {
        switch (mMode) {
            case Common.SCROLLER_MODE_Linear:
                mLM = new LinearLayoutManager(mPageContext.getContext());
                ((LinearLayoutManager) mLM).setOrientation(mOrientation);
                break;

            case Common.SCROLLER_MODE_StaggeredGrid:
                mLM = new StaggeredGridLayoutManager(2, mOrientation);
                break;

            default:
                LogHelper.e(TAG, "mode invalidate:" + mMode);
                break;
        }
        setLayoutManager(mLM);

//        switch (mMode) {
//            case Scroller.MODE_Linear:
//                ((LinearLayoutManager) mLM).setOrientation(mOrientation);
//                break;
//
//            case Scroller.MODE_StaggeredGrid:
//                ((StaggeredGridLayoutManager) mLM).setOrientation(mOrientation);
//                break;
//        }
    }

    @Override
    public boolean setIntValue(int key, int value) {
        boolean ret = true;
        switch (key) {
            case StringTab.STR_ID_orientation:
                mOrientation = value;
                break;

            case StringTab.STR_ID_mode:
                mMode = value;
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

    @Override
    public boolean setStrValue(int key, String value) {
        return mViewAttrSetter.setStrValue(key, value);
    }

    @Override
    public void release() {
        if (null != mAdapter) {
            mAdapter.destroy();
            mAdapter = null;
        }

        mLM = null;

        if (null != mViewAttrSetter) {
            mViewAttrSetter.release();
            mViewAttrSetter = null;
        }
    }
}
