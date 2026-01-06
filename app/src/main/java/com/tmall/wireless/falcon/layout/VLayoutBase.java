package com.tmall.wireless.falcon.layout;

import android.view.View;
import android.view.ViewGroup;

import com.libra.common.Common;
import com.libra.common.LogHelper;
import com.libra.common.StringTab;
import com.tmall.wireless.falcon.PageContext;
import com.tmall.wireless.falcon.common.Const;
import com.tmall.wireless.falcon.common.Utils;
import com.tmall.wireless.falcon.core.BGView;
import com.tmall.wireless.falcon.core.IAttr;
import com.tmall.wireless.falcon.core.IBean;
import com.tmall.wireless.falcon.core.IView;
import com.tmall.wireless.falcon.view.Slot;
import com.tmall.wireless.falcon.view.ViewCache;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static android.view.ViewGroup.getChildMeasureSpec;

/**
 * Created by gujicheng on 17/5/18.
 */
public class VLayoutBase implements IVirtualLayout {
    private final static String TAG = "VLayoutBase";

    protected int mMeasuredWidth;
    protected int mMeasuredHeight;

    protected int mPaddingLeft;
    protected int mPaddingTop;
    protected int mPaddingRight;
    protected int mPaddingBottom;

    protected int mVisibility = View.VISIBLE;

    protected int mId;
    protected int mVLeft;
    protected int mVTop;

    protected ViewGroup.MarginLayoutParams mLayoutParams;
    protected BGView mBackgroundView;

    protected List<IView> mChildren = new ArrayList<>();
    protected ViewCache mViewCache;
    protected int mComponentId;
    protected int mChildCount = 0;
    protected PageContext mPageContext;
    protected IBean mBean;
    protected Object mRawData;
    protected int mAutoDimDirection = Common.AUTO_DIM_DIRECTION_NONE;
    protected float mAutoDimX = 1;
    protected float mAutoDimY = 1;

    public VLayoutBase(PageContext pageContext, ViewCache viewCache) {
        mViewCache = viewCache;
        mPageContext = pageContext;
    }

    public int getChildrenCount() {
    return mChildren.size();
}

    public IView getChild(int index) {
        if (index >= 0 && index < mChildren.size()) {
            return mChildren.get(index);
        }
        return null;
    }

    protected void measureVChildren(int widthMeasureSpec, int heightMeasureSpec) {
        final int size = mChildren.size();
        for (int i = 0; i < size; ++i) {
            final IView child = mChildren.get(i);
            if (!child.isGone()) {
                measureVChild(child, widthMeasureSpec, heightMeasureSpec);
            }
        }
    }

    protected void measureVChild(IView child, int parentWidthMeasureSpec,
                                 int parentHeightMeasureSpec) {
        final ViewGroup.LayoutParams lp = child.getVLayoutParams();

        final int childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec,
                mPaddingLeft + mPaddingRight, lp.width);
        final int childHeightMeasureSpec = getChildMeasureSpec(parentHeightMeasureSpec,
                mPaddingTop + mPaddingBottom, lp.height);

        child.vMeasure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    @Override
    public int getVPaddingLeft() {
        return mPaddingLeft;
    }

    @Override
    public int getVPaddingTop() {
        return mPaddingTop;
    }

    @Override
    public int getVPaddingRight() {
        return mPaddingRight;
    }

    @Override
    public int getVPaddingBottom() {
        return mPaddingBottom;
    }

    @Override
    public int getVTop() {
        return mVTop;
    }

    @Override
    public int getVLeft() {
        return mVLeft;
    }

    @Override
    public boolean setIntValue(int key, int value) {
        boolean ret = true;

        switch (key) {
            case StringTab.STR_ID_paddingLeft:
                mPaddingLeft = value;
                break;

            case StringTab.STR_ID_paddingTop:
                mPaddingTop = value;
                break;

            case StringTab.STR_ID_paddingRight:
                mPaddingRight = value;
                break;

            case StringTab.STR_ID_paddingBottom:
                mPaddingBottom = value;
                break;

            case StringTab.STR_ID_backgroundColor: {
                View v = getBGViewInternal();
                if (null != v) {
                    v.setBackgroundColor(value);
                }
                break;
            }

            case StringTab.STR_ID_visibility:
                mVisibility = value;
                break;

            case StringTab.STR_ID_autoDimDirection:
                setAutoDimDirection(value);
                break;
            case StringTab.STR_ID_autoDimX:
                setAutoDimX(value);
                break;
            case StringTab.STR_ID_autoDimY:
                setAutoDimY(value);
                break;

            case StringTab.STR_ID_id:
                setVId(value);
                break;

            default:
                ret = false;
        }
        return ret;
    }

    private BGView getBGViewInternal() {
        if (null == mBackgroundView) {
            mBackgroundView = new BGView(mPageContext.getContext());
        }
        return mBackgroundView;
    }

    @Override
    public boolean setFloatValue(int key, float value) {

        boolean ret = true;

        switch (key) {
            case StringTab.STR_ID_paddingLeft:
                mPaddingLeft = (int)value;
                break;

            case StringTab.STR_ID_paddingTop:
                mPaddingTop = (int)value;
                break;

            case StringTab.STR_ID_paddingRight:
                mPaddingRight = (int)value;
                break;

            case StringTab.STR_ID_paddingBottom:
                mPaddingBottom = (int)value;
                break;

            case StringTab.STR_ID_autoDimX:
                setAutoDimX(value);
                break;
            case StringTab.STR_ID_autoDimY:
                setAutoDimY(value);
                break;
            default:
                ret = false;
        }

        return ret;
    }

    @Override
    public boolean setStrValue(int key, String value) {
        boolean ret = true;

        switch (key) {
            case StringTab.STR_ID_background:
                mPageContext.getApplicationContext().setBackground(value, getBGViewInternal());
                break;

            case StringTab.STR_ID_dataSource:
                break;

            case StringTab.STR_ID_bean:
                setBean(Utils.parseBean(this.getClass().getClassLoader(), value));
                break;
            default:
                ret = false;
        }

        return ret;
    }

    @Override
    public boolean setStyle(int keyId, Object value) {
        boolean ret = true;
        switch (keyId) {
            case StringTab.STR_ID_paddingLeft:
                mPaddingLeft = (Integer) value;
                break;

            case StringTab.STR_ID_paddingTop:
                mPaddingTop = (Integer) value;
                break;

            case StringTab.STR_ID_paddingRight:
                mPaddingRight = (Integer) value;
                break;

            case StringTab.STR_ID_paddingBottom:
                mPaddingBottom = (Integer) value;
                break;

            case StringTab.STR_ID_backgroundColor:
                View v = getBGViewInternal();
                if (null != v) {
                    v.setBackgroundColor((Integer) value);
                }
                break;

            case StringTab.STR_ID_visibility:
                mVisibility = (Integer) value;
                break;

            case StringTab.STR_ID_autoDimDirection:
                setAutoDimDirection((Integer)value);
                break;
            case StringTab.STR_ID_autoDimX:
                setAutoDimX((Float)value);
                break;
            case StringTab.STR_ID_autoDimY:
                setAutoDimY((Float)value);
                break;

            case StringTab.STR_ID_background:
                mPageContext.getApplicationContext().setBackground((String)value, getBGViewInternal());
                break;

            default:
                ret = false;
        }
        return ret;

    }

    @Override
    public void reset() {
        for (IView child : mChildren) {
            child.reset();
        }

//        mPaddingLeft = 0;
//        mPaddingTop = 0;
//        mPaddingRight = 0;
//        mPaddingBottom = 0;
//
//        mVisibility = View.VISIBLE;
//
//        if (null != mLayoutParams && mLayoutParams instanceof IAttr) {
//            ((IAttr)mLayoutParams).reset();
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
    public void vMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        onVMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public int getVMeasuredWidth() {
        return mMeasuredWidth;
    }

    @Override
    public int getVMeasuredHeight() {
        return mMeasuredHeight;
    }

    @Override
    public int getVMeasuredWidthWithMargin() {
        ViewGroup.MarginLayoutParams p = this.getVLayoutParams();
        return mMeasuredWidth + p.leftMargin + p.rightMargin;
    }

    @Override
    public int getVMeasuredHeightWithMargin() {
        ViewGroup.MarginLayoutParams p = this.getVLayoutParams();
        return mMeasuredHeight + p.topMargin + p.bottomMargin;
    }

    @Override
    public void vLayout(int left, int top, int right, int bottom) {
        mVLeft = left;
        mVTop = top;
        BGView bg = getBGView();
        if (null != bg) {
            bg.setVLeft(left);
            bg.setVTop(top);
        }

        onVLayout(true, left, top, right, bottom);
    }

    @Override
    public boolean isGone() {
        return (mVisibility == View.GONE);
    }

    @Override
    public void setVLayoutParams(ViewGroup.MarginLayoutParams params) {
        mLayoutParams = params;
    }

    @Override
    public void setComponentData(Object data) {
        if (null != data && data instanceof JSONObject) {
            StringTab st = mPageContext.getStringTab();

            JSONObject objData = (JSONObject)data;
            JSONArray itemArr = objData.optJSONArray(Const.DATA_ATTRS);
            if (null != itemArr) {
                int size = itemArr.length();
                for (int i = 0; i < size; ++i) {
                    JSONObject item = itemArr.optJSONObject(i);
                    if (null != item) {
                        String rawId = item.optString(Const.DATA_ID);
                        int id = st.getStringId(rawId);
                        if (id > -1) {
                            IView v = mViewCache.get(id);
                            if (null != v) {
                                v.setRawData(item);

                                Object obj = item.opt(Const.DATA_DATA);
                                if (null != obj) {
                                    v.setData(obj);
                                }

                                JSONObject styleObj = item.optJSONObject(Const.DATA_STYLE);
                                if (null != styleObj) {
                                    Iterator<String> it = styleObj.keys();
                                    while (it.hasNext()) {
                                        String key = it.next();
                                        Object value = styleObj.opt(key);
                                        int keyId = st.getStringId(key);
                                        if (!v.setStyle(keyId, value)) {
                                            ViewGroup.MarginLayoutParams lp = v.getVLayoutParams();
                                            if (null != lp && lp instanceof IAttr) {
                                                ((IAttr)lp).setStyle(keyId, value);
                                            }
                                        }

                                    }
                                }

                                obj = item.opt(Const.DATA_AUXILIARY);
                                if (null != obj) {
                                    v.setAuxiliary(obj);
                                }
                            } else {
                                LogHelper.e(TAG, "can not find view by id:" + id);
                            }
                        }
                    }
                }
            }
        } else {
            LogHelper.e(TAG, "data invalidate:"  + data);
        }
    }

    @Override
    public void setVMeasuredDimension(int width, int height) {
        mMeasuredWidth = width;
        mMeasuredHeight = height;
    }

    @Override
    public ViewGroup.MarginLayoutParams getVLayoutParams() {
        return mLayoutParams;
    }

    @Override
    public void onLoadFinished() {
    }

    public BGView getBGView() {
        return mBackgroundView;
    }

    @Override
    public void release() {
    }

    @Override
    public ViewGroup.MarginLayoutParams generateLayoutParams() {
        return new LayoutBase.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void addChild(IView child) {
        if (null != child) {
            if (child instanceof Slot) {
                ((Slot) child).setParent(this, mChildCount);
            } else {
                mChildren.add(child);
            }
            ++mChildCount;
        }
    }

    @Override
    public void replaceChild(IView child, int index, ViewGroup.MarginLayoutParams params) {
        throw new RuntimeException("can not replaceChild");
    }

    @Override
    public void onVMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        BGView bg = getBGView();
        if (null != bg) {
            bg.setVMeasuredDimension(mMeasuredWidth, mMeasuredHeight);
        }
    }

    @Override
    public void onVLayout(boolean changed, int left, int top, int right, int bottom) {
    }

    public static class LayoutParams extends ViewGroup.MarginLayoutParams implements IAttr {
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

}
