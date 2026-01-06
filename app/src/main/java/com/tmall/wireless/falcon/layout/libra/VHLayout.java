package com.tmall.wireless.falcon.layout.libra;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.libra.common.Common;
import com.libra.common.StringTab;
import com.tmall.wireless.falcon.PageContext;
import com.tmall.wireless.falcon.core.IBuilder;
import com.tmall.wireless.falcon.core.IView;
import com.tmall.wireless.falcon.layout.VLayoutBase;
import com.tmall.wireless.falcon.view.ViewCache;

/**
 * Created by gujicheng on 17/5/19.
 */

public class VHLayout extends VLayoutBase {
    private final static String TAG = "VirtualLinearLayout";

    protected int mMeasureChildrenWidth;
    protected int mMeasureChildrenHeight;

    protected int mGravity = Gravity.TOP | Gravity.LEFT;
    protected int mOrientation = android.widget.LinearLayout.HORIZONTAL;

    public static class Builder implements IBuilder {
        @Override
        public String getName() {
            return "VHLayout";
        }

        @Override
        public IView build(PageContext pageContext, ViewCache viewCache) {
            return new VHLayout(pageContext, viewCache);
        }
    }

    public VHLayout(PageContext pageContext, ViewCache viewCache) {
        super(pageContext, viewCache);
    }

    @Override
    public LinearLayout.LayoutParams generateLayoutParams() {
        return new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

//    @Override
//    public void vMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        ViewGroup.LayoutParams lp = this.getVLayoutParams();
//
//        super.vMeasure(getMeasureSpec(lp.width, widthMeasureSpec), getMeasureSpec(lp.height, heightMeasureSpec));
//    }

    protected static int getMeasureSpec(int size, int measureSpec) {
        int s = View.MeasureSpec.getSize(measureSpec);
        int mode = View.MeasureSpec.getMode(measureSpec);
        if (size == ViewGroup.LayoutParams.MATCH_PARENT) {
            return View.MeasureSpec.makeMeasureSpec(s, View.MeasureSpec.EXACTLY);
        }
        return measureSpec;
    }

    @Override
    public boolean setIntValue(int key, int value) {
        boolean ret = true;
        switch (key) {
            case StringTab.STR_ID_orientation:
                mOrientation = value;
                break;

            case StringTab.STR_ID_gravity:
                mGravity = value;
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

        mMeasureChildrenWidth = 0;
        mMeasureChildrenHeight = 0;

        switch (mOrientation) {
            case LinearLayout.VERTICAL:
                measureVertical(widthMeasureSpec, heightMeasureSpec);
                break;

            case LinearLayout.HORIZONTAL:
                measureHorizontal(widthMeasureSpec, heightMeasureSpec);
                break;
        }

        super.onVMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    final private void measureVertical(int widthMeasureSpec, int heightMeasureSpec) {
        int width = View.MeasureSpec.getSize(widthMeasureSpec);
        int height = View.MeasureSpec.getSize(heightMeasureSpec);

        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);

        boolean hasMatchWidth = false;
        for (IView child : mChildren) {
            if(!child.isGone()) {
                LinearLayout.LayoutParams p = (LinearLayout.LayoutParams) child.getVLayoutParams();
                if ((View.MeasureSpec.EXACTLY != widthMode) && (ViewGroup.LayoutParams.MATCH_PARENT == p.width)) {
                    hasMatchWidth = true;
                }
                measureVChild(child, widthMeasureSpec, heightMeasureSpec);
            }
        }

        setVMeasuredDimension(getRealWidth(widthMode, width),
                getRealHeight(heightMode, height));

        // forceUniformWidth
        if (hasMatchWidth) {
            int uniformMeasureSpec = View.MeasureSpec.makeMeasureSpec(getVMeasuredWidth(),
                    View.MeasureSpec.EXACTLY);

            for (IView child : mChildren) {
                if (!child.isGone()) {
                    LinearLayout.LayoutParams p = (LinearLayout.LayoutParams)child.getVLayoutParams();
                    if (ViewGroup.LayoutParams.MATCH_PARENT == p.width) {
                        measureVChild(child, uniformMeasureSpec, heightMeasureSpec);
                    }
                }
            }
        }
    }

    private void measureHorizontal(int widthMeasureSpec, int heightMeasureSpec) {
        int width = View.MeasureSpec.getSize(widthMeasureSpec);
        int height = View.MeasureSpec.getSize(heightMeasureSpec);

        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);

        boolean hasMatchHeight = false;
        for (IView child : mChildren) {
            if (!child.isGone()) {
                LinearLayout.LayoutParams p = (LinearLayout.LayoutParams)child.getVLayoutParams();
                if ((View.MeasureSpec.EXACTLY != heightMode) && (ViewGroup.LayoutParams.MATCH_PARENT == p.height)) {
                    hasMatchHeight = true;
                }
                measureVChild(child, widthMeasureSpec, heightMeasureSpec);
            }
        }

        setVMeasuredDimension(getRealWidth(widthMode, width),
                getRealHeight(heightMode, height));

        // forceUniformHeight
        if (hasMatchHeight) {
            int uniformMeasureSpec = View.MeasureSpec.makeMeasureSpec(getVMeasuredHeight(),
                    View.MeasureSpec.EXACTLY);

            for (IView child : mChildren) {
                if (!child.isGone()) {
                    LinearLayout.LayoutParams p = (LinearLayout.LayoutParams)child.getVLayoutParams();
                    if (ViewGroup.LayoutParams.MATCH_PARENT == p.height) {
                        measureVChild(child, widthMeasureSpec, uniformMeasureSpec);
                    }
                }
            }
        }
    }

    private int getRealWidth(int mode, int size) {
        int ret = size;
        if (View.MeasureSpec.AT_MOST == mode) {
            int childrenWidth = 0;

            if (android.widget.LinearLayout.HORIZONTAL == mOrientation) {
                for (IView child : mChildren) {
                    if (child.isGone()) {
                        continue;
                    }
                    childrenWidth += child.getVMeasuredWidthWithMargin();
                }
                mMeasureChildrenWidth = childrenWidth;
                childrenWidth += mPaddingLeft + mPaddingRight;
            } else if (android.widget.LinearLayout.VERTICAL == mOrientation) {
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
            }

            ret = Math.min(size, childrenWidth);
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

            if (LinearLayout.HORIZONTAL == mOrientation) {
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
            } else if (android.widget.LinearLayout.VERTICAL == mOrientation) {
                for (IView child : mChildren) {
                    if (child.isGone()) {
                        continue;
                    }
                    childrenHeight += child.getVMeasuredHeightWithMargin();
                }
                mMeasureChildrenHeight = childrenHeight;

                childrenHeight += mPaddingTop + mPaddingBottom;
            }

            ret = Math.min(size, childrenHeight);
        } else if (View.MeasureSpec.EXACTLY == mode) {
            ret = size;
        } else {
            int childrenHeight = 0;

            if (android.widget.LinearLayout.HORIZONTAL == mOrientation) {
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
            } else if (android.widget.LinearLayout.VERTICAL == mOrientation) {
                for (IView child : mChildren) {
                    if (child.isGone()) {
                        continue;
                    }
                    childrenHeight += child.getVMeasuredHeightWithMargin();
                }
                mMeasureChildrenHeight = childrenHeight;

                childrenHeight += mPaddingTop + mPaddingBottom;
            }

            ret = Math.max(size, childrenHeight);
        }

        return ret;
    }

    private int getChildrenWidth() {
        if (mMeasureChildrenWidth <= 0) {
            mMeasureChildrenWidth = 0;
            for (IView child : mChildren) {
                mMeasureChildrenWidth += child.getVMeasuredWidthWithMargin();
            }
        }

        return mMeasureChildrenWidth;
    }

    private int getChildrenHeight() {
        if (mMeasureChildrenHeight <= 0) {
            mMeasureChildrenHeight = 0;
            for (IView child : mChildren) {
                mMeasureChildrenHeight += child.getVMeasuredHeightWithMargin();
            }
        }

        return mMeasureChildrenHeight;
    }

    @Override
    public void onVLayout(boolean changed, int l, int t, int r, int b) {
        switch (mOrientation) {
            case android.widget.LinearLayout.HORIZONTAL: {
                int left = 0;
                int gravity = mGravity & 0xf;
                if (gravity == Gravity.LEFT) {
                    left = l + mPaddingLeft;
                } else if (gravity == Gravity.CENTER_HORIZONTAL) {
                    left = (r + l - getChildrenWidth()) >> 1;
                } else {
                    left = (r - getChildrenWidth() - mPaddingRight);
                }

                for (IView view : mChildren) {
                    if (view.isGone()) {
                        continue;
                    }
                    LinearLayout.LayoutParams childP = (LinearLayout.LayoutParams) view.getVLayoutParams();
                    int w = view.getVMeasuredWidth();
                    int h = view.getVMeasuredHeight();
                    left += childP.leftMargin;

                    int tt;
                    gravity = childP.gravity & 0xf0;
                    if (gravity == Gravity.CENTER_VERTICAL) {
                        tt = (b + t - h) >> 1;
                    } else if (gravity == Gravity.BOTTOM) {
                        tt = b - h - mPaddingBottom - childP.bottomMargin;
                    } else {
                        tt = t + mPaddingTop + childP.topMargin;
                    }
                    view.vLayout(left, tt, left + w, tt + h);

                    left += w + childP.rightMargin;
                }
                break;
            }

            case android.widget.LinearLayout.VERTICAL: {
                int top;
                int gravity = mGravity & 0xf0;
                if (gravity == Gravity.TOP) {
                    top = t + mPaddingTop;
                } else if (gravity == Gravity.CENTER_VERTICAL) {
                    top = (b + t - getChildrenHeight()) >> 1;
                } else {
                    top = (b - getChildrenHeight() - mPaddingBottom);
                }

                for (IView view : mChildren) {
                    if (view.isGone()) {
                        continue;
                    }

                    android.widget.LinearLayout.LayoutParams childP = (android.widget.LinearLayout.LayoutParams) view.getVLayoutParams();
                    int w = view.getVMeasuredWidth();
                    int h = view.getVMeasuredHeight();
                    top += childP.topMargin;

                    int ll;
                    gravity = childP.gravity & 0xf;
                    if (gravity == Gravity.CENTER_HORIZONTAL) {
                        ll = (r + l - w) >> 1;
                    } else if (gravity == Gravity.RIGHT) {
                        ll = r - mPaddingRight - childP.rightMargin - w;
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
}
