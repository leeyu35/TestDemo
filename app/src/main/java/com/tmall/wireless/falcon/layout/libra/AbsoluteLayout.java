package com.tmall.wireless.falcon.layout.libra;

import android.view.View;
import android.view.ViewGroup;

import com.libra.common.Common;
import com.libra.common.LogHelper;
import com.libra.common.StringTab;
import com.tmall.wireless.falcon.PageContext;
import com.tmall.wireless.falcon.core.IBuilder;
import com.tmall.wireless.falcon.core.IView;
import com.tmall.wireless.falcon.layout.LayoutBase;
import com.tmall.wireless.falcon.view.ViewCache;

/**
 * Created by gujicheng on 17/5/9.
 */

public class AbsoluteLayout extends LayoutBase {
    private final static String TAG = "AbsoluteLayout";

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

    public static class Builder implements IBuilder {
        @Override
        public String getName() {
            return "AbsoluteLayout";
        }

        @Override
        public IView build(PageContext pageContext, ViewCache viewCache) {
            return new AbsoluteLayout(pageContext, viewCache);
        }
    }

    public AbsoluteLayout(PageContext pageContext, ViewCache viewCache) {
        super(pageContext, viewCache);
    }

    @Override
    public LayoutParams generateLayoutParams() {
        return new LayoutParams(MarginLayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0, 0);
    }

    @Override
    public void replaceChild(IView child, int index, MarginLayoutParams params) {
        if (child instanceof View) {
            addView((View)child, index, params);
        } else {
            LogHelper.e(TAG, "replaceChild failed");
        }
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

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (MeasureSpec.EXACTLY == widthMode && MeasureSpec.EXACTLY == heightMode) {
            measureChildren(widthMeasureSpec, heightMeasureSpec);
            this.setMeasuredDimension(width, height);
        } else {
            LogHelper.e(TAG, "absoluteLayout must be exactly");
            throw new RuntimeException("absoluteLayout must be exactly");
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                LayoutParams lp = (LayoutParams) child.getLayoutParams();

                int childLeft = lp.mLayoutX;
                int childTop = lp.mLayoutY;
                child.layout(childLeft, childTop,
                        childLeft + child.getMeasuredWidth(),
                        childTop + child.getMeasuredHeight());
            }
        }
    }

    public static class LayoutParams extends LayoutBase.LayoutParams {
        public int mLayoutX;
        public int mLayoutY;

        public LayoutParams(int width, int height, int x, int y) {
            super(width, height);
            this.mLayoutX = x;
            this.mLayoutY = y;
        }

        @Override
        public boolean setIntValue(int key, int value) {
            boolean ret = true;
            switch (key) {
                case StringTab.STR_ID_layout_x:
                    mLayoutX = value;
                    break;

                case StringTab.STR_ID_layout_y:
                    mLayoutY = value;
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
                case StringTab.STR_ID_layout_x:
                    mLayoutX = (int)value;
                    break;

                case StringTab.STR_ID_layout_y:
                    mLayoutY = (int)value;
                    break;

                default:
                    ret = super.setFloatValue(key, value);
            }
            return ret;
        }
    }
}
