package com.tmall.wireless.falcon.layout.libra;

import android.view.Gravity;
import android.view.ViewGroup;

import com.libra.common.Common;
import com.libra.common.LogHelper;
import com.libra.common.StringTab;
import com.tmall.wireless.falcon.PageContext;
import com.tmall.wireless.falcon.core.IBuilder;
import com.tmall.wireless.falcon.core.IView;
import com.tmall.wireless.falcon.view.ViewCache;

/**
 * Created by gujicheng on 17/5/20.
 */

public class VJustifyLayout extends VHLayout {
    private final static String TAG = "VJustifyLayout";

    public static class Builder implements IBuilder {
        @Override
        public String getName() {
            return "VJustifyLayout";
        }

        @Override
        public IView build(PageContext pageContext, ViewCache viewCache) {
            return new VJustifyLayout(pageContext, viewCache);
        }
    }

    public VJustifyLayout(PageContext pageContext, ViewCache viewCache) {
        super(pageContext, viewCache);
    }

    @Override
    public LayoutParams generateLayoutParams() {
        return new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onVLayout(boolean changed, int l, int t, int r, int b) {
        switch (mOrientation) {
            case android.widget.LinearLayout.HORIZONTAL: {
                int left = 0;
                int leftStart = l + mPaddingLeft;
                int rightStart = r - mPaddingRight;

                for (IView view : mChildren) {
                    if (view.isGone()) {
                        continue;
                    }

                    LayoutParams childP = (LayoutParams) view.getVLayoutParams();
                    int w = view.getVMeasuredWidth();
                    int h = view.getVMeasuredHeight();

                    if (Common.JUSTIFY_LAYOUT_ORIENTATION_START == childP.mOrientation) {
                        leftStart += childP.leftMargin;
                        left = leftStart;
                        leftStart += w + childP.rightMargin;
                    } else if (Common.JUSTIFY_LAYOUT_ORIENTATION_END == childP.mOrientation) {
                        rightStart -= childP.rightMargin + w;
                        left = rightStart;
                        rightStart -= childP.leftMargin;
                    } else {
                        LogHelper.e(TAG, "onComLayout HORIZONTAL direction invalidate:" + childP.mOrientation);
                    }

                    int tt;
                    int gravity = childP.gravity & 0xf0;
                    if (Gravity.CENTER_VERTICAL == gravity) {
                        tt = (b + t - h) >> 1;
                    } else if (Gravity.BOTTOM == gravity) {
                        tt = b - h - mPaddingBottom - childP.bottomMargin;
                    } else {
                        tt = t + mPaddingTop + childP.topMargin;
                    }
                    view.vLayout(left, tt, left + w, tt + h);
                }
                break;
            }

            case android.widget.LinearLayout.VERTICAL: {
                int top = 0;
                int topStart = t + mPaddingTop;
                int bottomStart = b - mPaddingBottom;

                for (IView view : mChildren) {
                    if (view.isGone()) {
                        continue;
                    }

                    LayoutParams childP = (LayoutParams) view.getVLayoutParams();
                    int w = view.getVMeasuredWidth();
                    int h = view.getVMeasuredHeight();

                    if (Common.JUSTIFY_LAYOUT_ORIENTATION_START == childP.mOrientation) {
                        topStart += childP.topMargin;
                        top = topStart;
                        topStart += h + childP.bottomMargin;
                    } else if (Common.JUSTIFY_LAYOUT_ORIENTATION_END == childP.mOrientation) {
                        bottomStart -= childP.bottomMargin + h;
                        top = bottomStart;
                        bottomStart -= childP.topMargin;
                    } else {
                        LogHelper.e(TAG, "onComLayout VERTICAL direction invalidate:" + childP.mOrientation);
                    }

                    int ll;
                    int gravity = childP.gravity & 0xf;
                    if (Gravity.CENTER_HORIZONTAL == gravity) {
                        ll = (r + l - w) >> 1;
                    } else if (Gravity.RIGHT == gravity) {
                        ll = r - mPaddingRight - childP.rightMargin - w;
                    } else {
                        ll = l + mPaddingLeft + childP.leftMargin;
                    }

                    view.vLayout(ll, top, ll + w, top + h);
                }
                break;
            }
        }
    }

    public static class LayoutParams extends LinearLayout.LayoutParams {
        public int mOrientation;

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        @Override
        public boolean setIntValue(int key, int value) {
            boolean ret = true;
            switch (key) {
                case StringTab.STR_ID_layout_orientation:
                    mOrientation = value;
                    break;

                default:
                    ret = super.setIntValue(key, value);
            }
            return ret;
        }
    }
}
