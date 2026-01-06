package com.tmall.wireless.falcon.view;

import android.text.TextUtils;
import android.view.ViewGroup;

import com.libra.common.LogHelper;
import com.libra.common.StringTab;
import com.tmall.wireless.falcon.PageContext;
import com.tmall.wireless.falcon.common.Const;
import com.tmall.wireless.falcon.core.IBean;
import com.tmall.wireless.falcon.core.IBuilder;
import com.tmall.wireless.falcon.core.IView;
import com.tmall.wireless.falcon.layout.ILayout;

import org.json.JSONObject;

/**
 * Created by gujicheng on 17/5/13.
 */

public class Slot implements IView {
    private final static String TAG = "Slot";

    private PageContext mPageContext;
    private int mId;
    private ViewGroup.MarginLayoutParams mLayoutParams;
    private ILayout mParent;
    private int mIndex;
    private IView mView;

    public static class Builder implements IBuilder {
        @Override
        public String getName() {
            return "Slot";
        }

        @Override
        public IView build(PageContext pageContext, ViewCache viewCache) {
            return new Slot(pageContext, viewCache);
        }
    }

    public Slot(PageContext pageContext, ViewCache viewCache) {
        mPageContext = pageContext;
    }

    @Override
    public void release() {
        mPageContext = null;
        mParent = null;
        mLayoutParams = null;
    }

    public void setParent(ILayout parent, int index) {
        mParent = parent;
        mIndex = index;
    }

    @Override
    public boolean setIntValue(int key, int value) {
        boolean ret = true;

        switch (key) {
            case StringTab.STR_ID_id:
                setVId(value);
                break;
            default:
                ret = false;
        }
        return ret;
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
    public void reset() {
        if (null != mView) {
            mPageContext.recycleComponent(mView);
            mView = null;
        }
        LogHelper.d(TAG, "reset");
    }

    @Override
    public int getComponentId() {
        return 0;
    }

    @Override
    public void setComponentId(int id) {

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
        if (data instanceof JSONObject) {
            JSONObject obj = (JSONObject)data;
            if (null == mView) {
                String template = obj.optString(Const.TEMPLATE);
                if (!TextUtils.isEmpty(template)) {
                    IView view = mPageContext.newComponent(template);
                    if (null != mParent && null != mLayoutParams) {

                        mParent.replaceChild(view, mIndex, mLayoutParams);

                        // update view cache
    //                    mViewCache.put(mId, (IView) view);
                        mView = view;

                        // release this slot
    //                    this.release();

                        // set data
                        view.setComponentData(obj);
    //                    LogHelper.d(TAG, "set slot mIndex:" + mIndex + "  obj:" + obj);
                    } else {
                        LogHelper.e(TAG, "parent or layoutParams is null");
                    }
                } else {
                    LogHelper.e(TAG, "");
                }
            } else {
                mView.setComponentData(obj);
            }
        } else {
            LogHelper.e(TAG, "setData, data invalidate:" + data);
        }
    }

    @Override
    public boolean setStyle(int keyId, Object value) {
        return false;
    }

    @Override
    public void setAuxiliary(Object auxiliary) {
    }

    @Override
    public IBean getBean() {
        return null;
    }

    @Override
    public void setRawData(Object data) {

    }

    @Override
    public Object getRawData() {
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
    public void setBean(IBean bean) {

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
    }

    @Override
    public int getVMeasuredWidth() {
        return 0;
    }

    @Override
    public int getVMeasuredHeight() {
        return 0;
    }

    @Override
    public int getVMeasuredWidthWithMargin() {
        return 0;
    }

    @Override
    public int getVMeasuredHeightWithMargin() {
        return 0;
    }

    @Override
    public void vLayout(int left, int top, int right, int bottom) {

    }

    @Override
    public boolean isGone() {
        return false;
    }

    @Override
    public void setVLayoutParams(ViewGroup.MarginLayoutParams param) {
        mLayoutParams = param;
    }

    @Override
    public void setComponentData(Object data) {
    }

    @Override
    public void setVMeasuredDimension(int widthMeasureSpec, int heightMeasureSpec) {
    }

    @Override
    public ViewGroup.MarginLayoutParams getVLayoutParams() {
        return null;
    }

    @Override
    public void onLoadFinished() {
    }

}
