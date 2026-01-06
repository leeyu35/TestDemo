package com.tmall.wireless.falcon.core;

import android.view.View;
import android.view.ViewGroup;

import com.libra.common.LogHelper;
import com.tmall.wireless.falcon.PageContext;
import com.tmall.wireless.falcon.event.DefaultClickProcessor;
import com.tmall.wireless.falcon.layout.VLayoutBase;

/**
 * Created by gujicheng on 17/5/18.
 */

public class Container extends ViewGroup implements IView, View.OnClickListener {
    private final static String TAG = "Container";

    private IView mView;
    private PageContext mPageContext;

    public Container(PageContext pageContext) {
        super(pageContext.getContext());

        mPageContext = pageContext;
        setOnClickListener(this);
    }

    public IView getView() {
        return mView;
    }

    public void attachView(IView view) {
        if (null != view) {
            mView = view;
            attachViewImp(view);
        }
    }

    public void attachViewImp(IView view) {
        if (null != view) {
            if (view instanceof VLayoutBase) {
                VLayoutBase vlb = (VLayoutBase) view;

                BGView bg = vlb.getBGView();
                if (null != bg) {
                    this.addView(bg);
                }

                int size = vlb.getChildrenCount();
                for (int i = 0; i < size; ++i) {
                    attachViewImp(vlb.getChild(i));
                }
            } else if (view instanceof View) {
                this.addView((View) view);
                ((View)view).setOnClickListener(this);
            }
        }
    }

    @Override
    public void reset() {
        if (null != mView) {
            mView.reset();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (null != mView) {
            mView.vMeasure(widthMeasureSpec, heightMeasureSpec);

            setMeasuredDimension(mView.getVMeasuredWidth(), mView.getVMeasuredHeight());
        } else {
            LogHelper.e(TAG, "onMeasure failed, view is null");
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = this.getChildCount();
        for (int i = 0; i < count; ++i) {
            View child = this.getChildAt(i);
            IView v = (IView) child;
            if (!v.isGone()) {
                int left = v.getVLeft();
                int top = v.getVTop();
                ((View)v).layout(left, top, left + child.getMeasuredWidth(), top + child.getMeasuredHeight());
            }
        }
    }

    @Override
    public int getComponentId() {
        if (null != mView) {
            return mView.getComponentId();
        }
        return 0;
    }

    @Override
    public void setComponentId(int id) {
    }

    @Override
    public int getVId() {
        return -1;
    }

    @Override
    public void setVId(int id) {
    }

    @Override
    public void setData(Object data) {

    }

    @Override
    public boolean setStyle(int keyId, Object value) {
        return false;
    }

    @Override
    public void setAuxiliary(Object auxiliary) {
    }

    @Override
    public void setBean(IBean bean) {
    }

    @Override
    public IBean getBean() {
        if (null != mView) {
            return mView.getBean();
        }
        return null;
    }

    @Override
    public void setRawData(Object data) {

    }

    @Override
    public Object getRawData() {
        if (null != mView) {
            return mView.getRawData();
        }
        return null;
    }

    @Override
    public void setAutoDimDirection(int value) {

    }

    @Override
    public void setAutoDimX(float value) {

    }

    @Override
    public void setAutoDimY(float value) {

    }

    @Override
    public int getVPaddingLeft() {
        return 0;
    }

    @Override
    public int getVPaddingTop() {
        return 0;
    }

    @Override
    public int getVPaddingRight() {
        return 0;
    }

    @Override
    public int getVPaddingBottom() {
        return 0;
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
        onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public int getVMeasuredWidth() {
        return mView.getVMeasuredWidth();
    }

    @Override
    public int getVMeasuredHeight() {
        return mView.getVMeasuredHeight();
    }

    @Override
    public int getVMeasuredWidthWithMargin() {
        return 0;
    }

    @Override
    public int getVMeasuredHeightWithMargin() {
        return 0;
    }

    public void vLayout(int l, int t, int r, int b) {
        layout(l, t, r, b);
    }

    @Override
    public boolean isGone() {
        return false;
    }

    @Override
    public void setVMeasuredDimension(int widthMeasureSpec, int heightMeasureSpec) {
        this.setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public MarginLayoutParams getVLayoutParams() {
        return null;
    }

    @Override
    public void setVLayoutParams(MarginLayoutParams param) {
        this.setLayoutParams(param);
    }

    @Override
    public void setComponentData(Object data) {
        if (null != mView) {
            mView.setComponentData(data);
        }
    }

    @Override
    public void onLoadFinished() {
    }

    @Override
    public void release() {
    }

    @Override
    public boolean setIntValue(int key, int value) {
        return false;
    }

    @Override
    public boolean setFloatValue(int key, float value) {
        return false;
    }

    @Override
    public boolean setStrValue(int key, String value) {
        return false;
    }

    @Override
    public void onClick(View view) {
        // self bean process
        IView v = (IView)view;
        IBean bean = v.getBean();
        if (null != bean) {
            if (bean.onClick(v)) {
                return;
            }
        }

        // self default process
        DefaultClickProcessor fcp = mPageContext.getApplicationContext().getDefaultClickProcessor();
        if (null != fcp) {
            DefaultClickProcessor.IClick defaultClick = fcp.getDefaultClick();
            if (null != defaultClick) {
                if (defaultClick.onClick(v)) {
                    return ;
                }
            }
        }

        // container bean process
        View parent = (ViewGroup)view.getParent();
        if (parent instanceof Container) {
            bean = ((IView)parent).getBean();
            if (null != bean) {
                bean.onClick(v);
            }
        }
    }
}