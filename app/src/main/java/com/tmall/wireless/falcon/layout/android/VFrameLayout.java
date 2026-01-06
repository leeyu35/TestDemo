package com.tmall.wireless.falcon.layout.android;

import android.graphics.Rect;
import android.view.Gravity;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;

import com.libra.common.StringTab;
import com.tmall.wireless.falcon.PageContext;
import com.tmall.wireless.falcon.core.IBuilder;
import com.tmall.wireless.falcon.core.IView;
import com.tmall.wireless.falcon.layout.VLayoutBase;
import com.tmall.wireless.falcon.view.ViewCache;

import java.util.ArrayList;

import static android.R.attr.layoutDirection;
import static android.view.View.MEASURED_HEIGHT_STATE_SHIFT;
import static android.view.View.MEASURED_STATE_MASK;
import static android.view.View.combineMeasuredStates;
import static android.view.View.resolveSizeAndState;
import static android.view.ViewGroup.getChildMeasureSpec;

/**
 * Created by gujicheng on 17/6/5.
 */

public class VFrameLayout extends VLayoutBase {
    private static final int DEFAULT_CHILD_GRAVITY = Gravity.TOP | Gravity.START;

    @ViewDebug.ExportedProperty(category = "measurement")
    boolean mMeasureAllChildren = false;

    @ViewDebug.ExportedProperty(category = "padding")
    private int mForegroundPaddingLeft = 0;

    @ViewDebug.ExportedProperty(category = "padding")
    private int mForegroundPaddingTop = 0;

    @ViewDebug.ExportedProperty(category = "padding")
    private int mForegroundPaddingRight = 0;

    @ViewDebug.ExportedProperty(category = "padding")
    private int mForegroundPaddingBottom = 0;

    private final Rect mSelfBounds = new Rect();
    private final Rect mOverlayBounds = new Rect();

    private final ArrayList<IView> mMatchParentChildren = new ArrayList<>(1);

    public static class Builder implements IBuilder {
        @Override
        public String getName() {
            return "VFrameLayout";
        }

        @Override
        public IView build(PageContext pageContext, ViewCache viewCache) {
            return new VFrameLayout(pageContext, viewCache);
        }
    }

    public VFrameLayout(PageContext pageContext, ViewCache viewCache) {
        super(pageContext, viewCache);
    }

    public static class LayoutParams extends VLayoutBase.LayoutParams {
        public int gravity = -1;

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        @Override
        public boolean setIntValue(int key, int value) {
            boolean ret = true;
            switch (key) {
                case StringTab.STR_ID_layout_gravity:
                    gravity = value;
                    break;

                default:
                    ret = super.setIntValue(key, value);
            }
            return ret;
        }
    }

    @Override
    public LayoutParams generateLayoutParams() {
        return new LayoutParams(ViewGroup.MarginLayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    int getPaddingLeftWithForeground() {
        return mPaddingLeft + mForegroundPaddingLeft;
    }

    int getPaddingRightWithForeground() {
        return mPaddingRight + mForegroundPaddingRight;
    }

    private int getPaddingTopWithForeground() {
        return mPaddingTop + mForegroundPaddingTop;
    }

    private int getPaddingBottomWithForeground() {
        return mPaddingBottom + mForegroundPaddingBottom;
    }

    protected void measureChildWithMargins(IView child,
                                           int parentWidthMeasureSpec, int widthUsed,
                                           int parentHeightMeasureSpec, int heightUsed) {
        final LayoutParams lp = (LayoutParams) child.getVLayoutParams();

        final int childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec,
                mPaddingLeft + mPaddingRight + lp.leftMargin + lp.rightMargin
                        + widthUsed, lp.width);
        final int childHeightMeasureSpec = getChildMeasureSpec(parentHeightMeasureSpec,
                mPaddingTop + mPaddingBottom + lp.topMargin + lp.bottomMargin
                        + heightUsed, lp.height);

        child.vMeasure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    public final int getMeasuredState() {
        return (mMeasuredWidth&MEASURED_STATE_MASK)
                | ((mMeasuredHeight>>MEASURED_HEIGHT_STATE_SHIFT)
                & (MEASURED_STATE_MASK>>MEASURED_HEIGHT_STATE_SHIFT));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onVMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = mChildren.size();

        final boolean measureMatchParentChildren =
                View.MeasureSpec.getMode(widthMeasureSpec) != View.MeasureSpec.EXACTLY ||
                        View.MeasureSpec.getMode(heightMeasureSpec) != View.MeasureSpec.EXACTLY;
        mMatchParentChildren.clear();

        int maxHeight = 0;
        int maxWidth = 0;
        int childState = 0;

        for (int i = 0; i < count; i++) {
            final IView child = mChildren.get(i);
            if (mMeasureAllChildren || !child.isGone()) {
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
                final LayoutParams lp = (LayoutParams) child.getVLayoutParams();
                maxWidth = Math.max(maxWidth,
                        child.getVMeasuredWidth() + lp.leftMargin + lp.rightMargin);
                maxHeight = Math.max(maxHeight,
                        child.getVMeasuredHeight() + lp.topMargin + lp.bottomMargin);
                childState = combineMeasuredStates(childState, getMeasuredState());
                if (measureMatchParentChildren) {
                    if (lp.width == LayoutParams.MATCH_PARENT ||
                            lp.height == LayoutParams.MATCH_PARENT) {
                        mMatchParentChildren.add(child);
                    }
                }
            }
        }

        // Account for padding too
        maxWidth += getPaddingLeftWithForeground() + getPaddingRightWithForeground();
        maxHeight += getPaddingTopWithForeground() + getPaddingBottomWithForeground();

        // Check against our minimum height and width
        maxHeight = Math.max(maxHeight, 20);
        maxWidth = Math.max(maxWidth, 20);

        // Check against our foreground's minimum height and width
        setVMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, childState),
                resolveSizeAndState(maxHeight, heightMeasureSpec,
                        childState << MEASURED_HEIGHT_STATE_SHIFT));

        count = mMatchParentChildren.size();
        if (count > 1) {
            for (int i = 0; i < count; i++) {
                final IView child = mMatchParentChildren.get(i);
                final LayoutParams lp = (LayoutParams) child.getVLayoutParams();

                final int childWidthMeasureSpec;
                if (lp.width == LayoutParams.MATCH_PARENT) {
                    final int width = Math.max(0, getVMeasuredWidth()
                            - getPaddingLeftWithForeground() - getPaddingRightWithForeground()
                            - lp.leftMargin - lp.rightMargin);
                    childWidthMeasureSpec = View.MeasureSpec.makeMeasureSpec(
                            width, View.MeasureSpec.EXACTLY);
                } else {
                    childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec,
                            getPaddingLeftWithForeground() + getPaddingRightWithForeground() +
                                    lp.leftMargin + lp.rightMargin,
                            lp.width);
                }

                final int childHeightMeasureSpec;
                if (lp.height == LayoutParams.MATCH_PARENT) {
                    final int height = Math.max(0, getVMeasuredHeight()
                            - getPaddingTopWithForeground() - getPaddingBottomWithForeground()
                            - lp.topMargin - lp.bottomMargin);
                    childHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec(
                            height, View.MeasureSpec.EXACTLY);
                } else {
                    childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec,
                            getPaddingTopWithForeground() + getPaddingBottomWithForeground() +
                                    lp.topMargin + lp.bottomMargin,
                            lp.height);
                }

                child.vMeasure(childWidthMeasureSpec, childHeightMeasureSpec);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onVLayout(boolean changed, int left, int top, int right, int bottom) {
        layoutChildren(left, top, right, bottom, false /* no force left gravity */);
    }

    void layoutChildren(int left, int top, int right, int bottom,
                        boolean forceLeftGravity) {
        final int count = mChildren.size();

        final int parentLeft = getPaddingLeftWithForeground();
        final int parentRight = right - left - getPaddingRightWithForeground();

        final int parentTop = getPaddingTopWithForeground();
        final int parentBottom = bottom - top - getPaddingBottomWithForeground();

        for (int i = 0; i < count; i++) {
            final IView child = mChildren.get(i);
            if (!child.isGone()) {
                final LayoutParams lp = (LayoutParams) child.getVLayoutParams();

                final int width = child.getVMeasuredWidth();
                final int height = child.getVMeasuredHeight();

                int childLeft;
                int childTop;

                int gravity = lp.gravity;
                if (gravity == -1) {
                    gravity = DEFAULT_CHILD_GRAVITY;
                }

//                final int layoutDirection = getLayoutDirection();
                final int absoluteGravity = Gravity.getAbsoluteGravity(gravity, layoutDirection);
                final int verticalGravity = gravity & Gravity.VERTICAL_GRAVITY_MASK;

                switch (absoluteGravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
                    case Gravity.CENTER_HORIZONTAL:
                        childLeft = parentLeft + (parentRight - parentLeft - width) / 2 +
                                lp.leftMargin - lp.rightMargin;
                        break;
                    case Gravity.RIGHT:
                        if (!forceLeftGravity) {
                            childLeft = parentRight - width - lp.rightMargin;
                            break;
                        }
                    case Gravity.LEFT:
                    default:
                        childLeft = parentLeft + lp.leftMargin;
                }

                switch (verticalGravity) {
                    case Gravity.TOP:
                        childTop = parentTop + lp.topMargin;
                        break;
                    case Gravity.CENTER_VERTICAL:
                        childTop = parentTop + (parentBottom - parentTop - height) / 2 +
                                lp.topMargin - lp.bottomMargin;
                        break;
                    case Gravity.BOTTOM:
                        childTop = parentBottom - height - lp.bottomMargin;
                        break;
                    default:
                        childTop = parentTop + lp.topMargin;
                }

                child.vLayout(childLeft, childTop, childLeft + width, childTop + height);
            }
        }
    }

    /**
     * Sets whether to consider all children, or just those in
     * the VISIBLE or INVISIBLE state, when measuring. Defaults to false.
     *
     * @param measureAll true to consider children marked GONE, false otherwise.
     * Default value is false.
     *
     * @attr ref android.R.styleable#FrameLayout_measureAllChildren
     */

    /**
     * Determines whether all children, or just those in the VISIBLE or
     * INVISIBLE state, are considered when measuring.
     *
     * @return Whether all children are considered when measuring.
     *
     * @deprecated This method is deprecated in favor of
     * {@link #getMeasureAllChildren() getMeasureAllChildren()}, which was
     * renamed for consistency with
     * {@link #setMeasureAllChildren(boolean) setMeasureAllChildren()}.
     */

    /**
     * Determines whether all children, or just those in the VISIBLE or
     * INVISIBLE state, are considered when measuring.
     *
     * @return Whether all children are considered when measuring.
     */
    public boolean getMeasureAllChildren() {
        return mMeasureAllChildren;
    }
}
