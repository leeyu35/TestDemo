package com.tmall.wireless.falcon.layout.libra;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.libra.common.Common;
import com.libra.common.LogHelper;
import com.libra.common.StringTab;
import com.tmall.wireless.falcon.PageContext;
import com.tmall.wireless.falcon.core.IBuilder;
import com.tmall.wireless.falcon.core.IView;
import com.tmall.wireless.falcon.layout.VLayoutBase;
import com.tmall.wireless.falcon.view.ViewCache;

import static android.view.ViewGroup.getChildMeasureSpec;

/**
 * Creaed by gujicheng on 17/5/20.
 */

public class VRatioLayout extends VLayoutBase {
    private final static String TAG = "VRatioLayout";

    protected int mOrientation;
    protected int mMeasureChildrenWidth;
    protected int mMeasureChildrenHeight;

    protected int mTotalRatio;
    protected int mFixDim;

    public static class Builder implements IBuilder {
        @Override
        public String getName() {
            return "VRatioLayout";
        }

        @Override
        public IView build(PageContext pageContext, ViewCache viewCache) {
            return new VRatioLayout(pageContext, viewCache);
        }
    }

    public VRatioLayout(PageContext pageContext, ViewCache viewCache) {
        super(pageContext, viewCache);

        mOrientation = Common.RATIO_LAYOUT_ORIENTATION_HORIZONTAL;
        mMeasureChildrenWidth = 0;
        mMeasureChildrenHeight = 0;
    }

    @Override
    public LayoutParams generateLayoutParams() {
        return new LayoutParams(ViewGroup.MarginLayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public boolean setIntValue(int key, int value) {
        boolean ret = true;
        switch (key) {
            case StringTab.STR_ID_orientation:
                mOrientation = value;
                break;

            default:
                ret = super.setIntValue(key, value);
        }
        return ret;
    }

    @Override
    public void onVMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        switch (mAutoDimDirection) {
            case Common.AUTO_DIM_DIRECTION_X:
                if (View.MeasureSpec.EXACTLY == View.MeasureSpec.getMode(widthMeasureSpec)) {
                    heightMeasureSpec = View.MeasureSpec.makeMeasureSpec((int)((View.MeasureSpec.getSize(widthMeasureSpec) * mAutoDimY) / mAutoDimX), View.MeasureSpec.EXACTLY);
                }
                break;

            case Common.AUTO_DIM_DIRECTION_Y:
                if (View.MeasureSpec.EXACTLY == View.MeasureSpec.getMode(heightMeasureSpec)) {
                    widthMeasureSpec = View.MeasureSpec.makeMeasureSpec((int)((View.MeasureSpec.getSize(heightMeasureSpec) * mAutoDimX) / mAutoDimY), View.MeasureSpec.EXACTLY);
                }
                break;
        }

        switch (mOrientation) {
            case Common.RATIO_LAYOUT_ORIENTATION_VERTICAL:
                measureVertical(widthMeasureSpec, heightMeasureSpec);
                break;

            case Common.RATIO_LAYOUT_ORIENTATION_HORIZONTAL:
                measureHorizontal(widthMeasureSpec, heightMeasureSpec);
                break;
        }

        super.onVMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    protected void measureHorizontalRatioComChild(IView child, int parentWidthMeasureSpec, int parentHeightMeasureSpec) {
        LayoutParams childParam = (LayoutParams) child.getVLayoutParams();

        int childHeightMeasureSpec = getChildMeasureSpec(parentHeightMeasureSpec,
                mPaddingTop + mPaddingBottom + childParam.topMargin + childParam.bottomMargin, childParam.height);

        int childWidthMeasureSpec;
        if (childParam.mLayoutRatio > 0) {
            childWidthMeasureSpec = getRatioChildMeasureSpec(parentWidthMeasureSpec,
                    mPaddingLeft + mPaddingRight, childParam.width, childParam.mLayoutRatio,
                    child.getVPaddingLeft() + child.getVPaddingRight());
        } else {
            childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec,
                    mPaddingLeft + mPaddingRight + childParam.leftMargin + childParam.rightMargin, childParam.width);
        }

        child.vMeasure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    protected void measureVerticalRatioComChild(IView child, int parentWidthMeasureSpec, int parentHeightMeasureSpec) {
        LayoutParams childParam = (LayoutParams) child.getVLayoutParams();

        int childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec,
                mPaddingLeft + mPaddingRight + childParam.leftMargin + childParam.rightMargin, childParam.width);

        int childHeightMeasureSpec;
        if (childParam.mLayoutRatio > 0) {
            childHeightMeasureSpec = getRatioChildMeasureSpec(parentHeightMeasureSpec,
                    mPaddingTop + mPaddingBottom, childParam.height, childParam.mLayoutRatio,
                    child.getVPaddingTop() + child.getVPaddingBottom());
        } else {
            childHeightMeasureSpec = getChildMeasureSpec(parentHeightMeasureSpec,
                    mPaddingTop + mPaddingBottom + childParam.topMargin + childParam.bottomMargin, childParam.height);
        }

        child.vMeasure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    protected int getRatioChildMeasureSpec(int parentSpec, int padding, int childDimension, float ratio, int childMargin) {
        int parentSpecMode = View.MeasureSpec.getMode(parentSpec);
        int parentSpecSize = View.MeasureSpec.getSize(parentSpec);

        int size = Math.max(0, parentSpecSize - padding - mFixDim);

        int resultSize = 0;
        int resultMode = View.MeasureSpec.UNSPECIFIED;

        switch (parentSpecMode) {
            // Parent has imposed an exact size on us
            case View.MeasureSpec.EXACTLY:
                if (ratio > 0) {
                    resultSize = (int)((ratio * size / mTotalRatio) - childMargin);
                    if (resultSize < 0) {
                        resultSize = 0;
                    }
                    resultMode = View.MeasureSpec.EXACTLY;
                } else if (childDimension >= 0) {
                    resultSize = childDimension;
                    resultMode = View.MeasureSpec.EXACTLY;
                }
                break;

            // Parent has imposed a maximum size on us
            case View.MeasureSpec.AT_MOST:
            case View.MeasureSpec.UNSPECIFIED:
                break;
        }

        return View.MeasureSpec.makeMeasureSpec(resultSize, resultMode);
    }

    private void measureHorizontal(int widthMeasureSpec, int heightMeasureSpec) {
        int width = View.MeasureSpec.getSize(widthMeasureSpec);
        int height = View.MeasureSpec.getSize(heightMeasureSpec);

        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);

        mFixDim = 0;
        findTotalRatio();

        boolean hasMatchHeight = false;
        for (IView child : mChildren) {
            if (child.isGone()) {
                continue;
            }
            LayoutParams p = (LayoutParams) child.getVLayoutParams();
            if (((View.MeasureSpec.EXACTLY != heightMode) && (LayoutParams.MATCH_PARENT == p.height)) || p.mLayoutRatio > 0) {
                hasMatchHeight = true;
            }

            measureHorizontalRatioComChild(child, widthMeasureSpec, heightMeasureSpec);

            if (p.mLayoutRatio <= 0) {
                mFixDim += child.getVMeasuredWidthWithMargin();
            } else {
                mFixDim += p.leftMargin + p.rightMargin;
            }
        }

        setVMeasuredDimension(getRealWidth(widthMode, width), getRealHeight(heightMode, height));

        // forceUniformHeight
        if (hasMatchHeight) {
            int uniformMeasureSpec = View.MeasureSpec.makeMeasureSpec(getVMeasuredWidth(),
                    View.MeasureSpec.EXACTLY);
            int uniformMeasureHeightSpec = View.MeasureSpec.makeMeasureSpec(getVMeasuredHeight(),
                    View.MeasureSpec.EXACTLY);

            for (IView child : mChildren) {
                if (child.isGone()) {
                    continue;
                }
                LayoutParams p = (LayoutParams) child.getVLayoutParams();
                if (LayoutParams.MATCH_PARENT == p.height || p.mLayoutRatio > 0) {
                    measureHorizontalRatioComChild(child, uniformMeasureSpec, uniformMeasureHeightSpec);
                }
            }
        }
    }

    private void findTotalRatio() {
        mTotalRatio = 0;
        for (IView child : mChildren) {
            if (child.isGone()) {
                continue;
            }

            LayoutParams p = (LayoutParams) child.getVLayoutParams();
            if (null != p) {
                mTotalRatio += p.mLayoutRatio;
            } else {
                LogHelper.e(TAG, "findTotalRatio param is null");
            }
        }
    }

    final private void measureVertical(int widthMeasureSpec, int heightMeasureSpec) {
        int width = View.MeasureSpec.getSize(widthMeasureSpec);
        int height = View.MeasureSpec.getSize(heightMeasureSpec);

        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);

        mFixDim = 0;
        findTotalRatio();

        boolean hasMatchWidth = false;
        for (IView child : mChildren) {
            if (child.isGone()) {
                continue;
            }

            LayoutParams p = (LayoutParams) child.getVLayoutParams();
            if (((View.MeasureSpec.EXACTLY != widthMode) && (LayoutParams.MATCH_PARENT == p.width)) || p.mLayoutRatio > 0) {
                hasMatchWidth = true;
            }
            measureVerticalRatioComChild(child, widthMeasureSpec, heightMeasureSpec);

            if (p.mLayoutRatio <= 0) {
                mFixDim += child.getVMeasuredHeightWithMargin();
            } else {
                mFixDim += p.topMargin + p.bottomMargin;
            }
        }

        setVMeasuredDimension(getRealWidth(widthMode, width),
                getRealHeight(heightMode, height));

        // forceUniformWidth
        if (hasMatchWidth) {
            int uniformMeasureSpec = View.MeasureSpec.makeMeasureSpec(getVMeasuredWidth(),
                    View.MeasureSpec.EXACTLY);
            int uniformMeasureHeightSpec = View.MeasureSpec.makeMeasureSpec(getVMeasuredHeight(),
                    View.MeasureSpec.EXACTLY);

            for (IView child : mChildren) {
                if (child.isGone()) {
                    continue;
                }

                LayoutParams p = (LayoutParams) child.getVLayoutParams();
                if (LayoutParams.MATCH_PARENT == p.width || p.mLayoutRatio > 0) {
                    measureVerticalRatioComChild(child, uniformMeasureSpec, uniformMeasureHeightSpec);
                }
            }
        }
    }

    private int getRealWidth(int mode, int size) {
        int ret = size;
        if (View.MeasureSpec.AT_MOST == mode) {
            int childrenWidth = 0;

            if (Common.RATIO_LAYOUT_ORIENTATION_HORIZONTAL == mOrientation) {
                ret = size;
            } else if (Common.RATIO_LAYOUT_ORIENTATION_VERTICAL == mOrientation) {
                for (IView child : mChildren) {
                    if (child.isGone()) {
                        continue;
                    }
                    int h = child.getVMeasuredWidthWithMargin();
                    if (h > childrenWidth) {
                        childrenWidth = h;
                    }
                }

                mMeasureChildrenWidth = childrenWidth;
                childrenWidth += mPaddingLeft + mPaddingRight;
                ret = Math.min(size, childrenWidth);
            }

        } else if (View.MeasureSpec.EXACTLY == mode) {
            ret = size;
        } else {
            Log.e(TAG, "getRealWidth error mode:" + mode);
        }

        return ret;
    }

    private int getRealHeight(int mode, int size) {
        int ret = size;
        if (View.MeasureSpec.AT_MOST == mode) {
            int childrenHeight = 0;

            if (Common.RATIO_LAYOUT_ORIENTATION_HORIZONTAL == mOrientation) {
                for (IView child : mChildren) {
                    if (child.isGone()) {
                        continue;
                    }
                    int h = child.getVMeasuredHeightWithMargin();
                    if (h > childrenHeight) {
                        childrenHeight = h;
                    }
                }
                mMeasureChildrenHeight = childrenHeight;
                childrenHeight += mPaddingTop + mPaddingBottom;
                ret = Math.min(size, childrenHeight);
            } else if (Common.RATIO_LAYOUT_ORIENTATION_VERTICAL == mOrientation) {
                ret = size;
            }

        } else if (View.MeasureSpec.EXACTLY == mode) {
            ret = size;
        } else {
            int childrenHeight = 0;

            if (Common.RATIO_LAYOUT_ORIENTATION_HORIZONTAL == mOrientation) {
                for (IView child : mChildren) {
                    if (child.isGone()) {
                        continue;
                    }
                    int h = child.getVMeasuredHeightWithMargin();
                    if (h > childrenHeight) {
                        childrenHeight = h;
                    }
                }
                mMeasureChildrenHeight = childrenHeight;

                childrenHeight += mPaddingTop + mPaddingBottom;
                ret = Math.max(size, childrenHeight);
            } else if (Common.RATIO_LAYOUT_ORIENTATION_VERTICAL == mOrientation) {
                ret = size;
            }
        }

        return ret;
    }

    @Override
    public void onVLayout(boolean changed, int l, int t, int r, int b) {
        switch (mOrientation) {
            case Common.RATIO_LAYOUT_ORIENTATION_HORIZONTAL: {
                int left = l + mPaddingLeft;
                for (IView view : mChildren) {
                    if (view.isGone()) {
                        continue;
                    }

                    LayoutParams childP = (LayoutParams) view.getVLayoutParams();
                    int w = view.getVMeasuredWidth();
                    int h = view.getVMeasuredHeight();
                    left += childP.leftMargin;

                    int tt;
                    int gravity = childP.mLayoutGravity & 0xf0;
                    if (Gravity.CENTER_VERTICAL == gravity) {
                        tt = (b + t - h) >> 1;
                    } else if (Gravity.BOTTOM == gravity) {
                        tt = b - h - mPaddingBottom - childP.bottomMargin;
                    } else {
                        tt = t + mPaddingTop + childP.topMargin;
                    }
                    view.vLayout(left, tt, left + w, tt + h);

                    left += w + childP.rightMargin;
                }
                break;
            }

            case Common.RATIO_LAYOUT_ORIENTATION_VERTICAL: {
                int top = t + mPaddingTop;
                for (IView view : mChildren) {
                    if (view.isGone()) {
                        continue;
                    }

                    LayoutParams childP = (LayoutParams) view.getVLayoutParams();
                    int w = view.getVMeasuredWidth();
                    int h = view.getVMeasuredHeight();
                    top += childP.topMargin;

                    int ll;
                    int gravity = childP.mLayoutGravity & 0xf;
                    if (Gravity.CENTER_HORIZONTAL == gravity) {
                        ll = (r + l - w) >> 1;
                    } else if (Gravity.RIGHT == gravity) {
                        ll = r - mPaddingRight- childP.rightMargin - w;
                    } else {
                        ll = l + mPaddingLeft + childP.leftMargin;
                    }

                    view.vLayout(ll, top, ll + w, top + h);

                    top += h + childP.bottomMargin;
                }
                break;
            }
        }
    }

    public static class LayoutParams extends VLayoutBase.LayoutParams {
        public float mLayoutRatio;
        public int mLayoutGravity;

        public LayoutParams(int width, int height) {
            super(width, height);
            mLayoutRatio = 0;
        }

        @Override
        public boolean setIntValue(int key, int value) {
            boolean ret = true;
            switch (key) {
                case StringTab.STR_ID_layout_gravity:
                    mLayoutGravity = value;
                    break;

                case StringTab.STR_ID_layout_ratio:
                    mLayoutRatio = value;
                    break;

                default:
                    ret = super.setIntValue(key, value);
            }
            return ret;
        }

        @Override
        public boolean setFloatValue(int key, float value) {
            boolean ret = true;
            switch (key) {
                case StringTab.STR_ID_layout_ratio:
                    mLayoutRatio = value;
                    break;

                default:
                    ret = super.setFloatValue(key, value);
            }
            return ret;
        }
    }
}
