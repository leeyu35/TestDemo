package com.tmall.wireless.falcon.layout.libra;

import android.view.View;
import android.view.ViewGroup;

import com.libra.common.Common;
import com.libra.common.LogHelper;
import com.tmall.wireless.falcon.PageContext;
import com.tmall.wireless.falcon.core.IBuilder;
import com.tmall.wireless.falcon.core.IView;
import com.tmall.wireless.falcon.layout.VLayoutBase;
import com.tmall.wireless.falcon.view.ViewCache;

/**
 * Created by gujicheng on 17/5/18.
 */

public class VAbsoluteLayout extends VLayoutBase {
    private final static String TAG = "VAbsoluteLayout";

    public static class Builder implements IBuilder {
        @Override
        public String getName() {
            return "VAbsoluteLayout";
        }

        @Override
        public IView build(PageContext pageContext, ViewCache viewCache) {
            return new VAbsoluteLayout(pageContext, viewCache);
        }
    }

    public VAbsoluteLayout(PageContext pageContext, ViewCache viewCache) {
        super(pageContext, viewCache);
    }

    @Override
    public AbsoluteLayout.LayoutParams generateLayoutParams() {
        return new AbsoluteLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0, 0);
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
        if (size > 0) {
            if (size > s && s > 0) {
                size = s;
            }
        } else if (AbsoluteLayout.LayoutParams.MATCH_PARENT == size) {
            if (s > 0) {
                size = s;
            }
        }

        if (size < 0) {
            return measureSpec;
//            throw new RuntimeException("absoluteLayout get MeasureSpec failed");
        } else {
            return View.MeasureSpec.makeMeasureSpec(size, View.MeasureSpec.EXACTLY);
        }
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

        int width = View.MeasureSpec.getSize(widthMeasureSpec);
        int height = View.MeasureSpec.getSize(heightMeasureSpec);

        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);

//        LogHelper.d(TAG, "onVMeasure width:" + width + " widthMode:" + widthMode + "  height:" + height + "  heightMode:" + heightMode);

        if (View.MeasureSpec.EXACTLY == widthMode && View.MeasureSpec.EXACTLY == heightMode) {
            measureVChildren(widthMeasureSpec, heightMeasureSpec);
            this.setVMeasuredDimension(width, height);
        } else {
            LogHelper.e(TAG, "absoluteLayout must be exactly");
            throw new RuntimeException("absoluteLayout must be exactly");
        }
    }

    @Override
    public void onVLayout(boolean changed, int left, int top, int right, int bottom) {
        int count = mChildren.size();
        for (int i = 0; i < count; i++) {
            IView child = mChildren.get(i);
            if (!child.isGone()) {
                AbsoluteLayout.LayoutParams lp = (AbsoluteLayout.LayoutParams) child.getVLayoutParams();

                int childLeft = left + lp.mLayoutX;
                int childTop = top + lp.mLayoutY;
                child.vLayout(childLeft, childTop,
                        childLeft + child.getVMeasuredWidth(),
                        childTop + child.getVMeasuredHeight());
            }
        }
    }
}
