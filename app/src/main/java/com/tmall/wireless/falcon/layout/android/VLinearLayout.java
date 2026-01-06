package com.tmall.wireless.falcon.layout.android;

import android.graphics.drawable.Drawable;
import android.support.annotation.IntDef;
import android.view.Gravity;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;

import com.libra.common.StringTab;
import com.tmall.wireless.falcon.PageContext;
import com.tmall.wireless.falcon.core.IBuilder;
import com.tmall.wireless.falcon.core.IView;
import com.tmall.wireless.falcon.layout.VLayoutBase;
import com.tmall.wireless.falcon.layout.libra.LinearLayout;
import com.tmall.wireless.falcon.view.ViewCache;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static android.view.View.MEASURED_HEIGHT_STATE_SHIFT;
import static android.view.View.MEASURED_SIZE_MASK;
import static android.view.View.MEASURED_STATE_MASK;
import static android.view.View.combineMeasuredStates;
import static android.view.View.resolveSizeAndState;
import static android.view.ViewGroup.getChildMeasureSpec;

/**
 * Created by gujicheng on 17/6/5.
 */

public class VLinearLayout extends VLayoutBase {
    @IntDef({HORIZONTAL, VERTICAL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface OrientationMode {}

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;

    /** @hide */
    @IntDef(flag = true,
            value = {
                    SHOW_DIVIDER_NONE,
                    SHOW_DIVIDER_BEGINNING,
                    SHOW_DIVIDER_MIDDLE,
                    SHOW_DIVIDER_END
            })
    @Retention(RetentionPolicy.SOURCE)
    public @interface DividerMode {}

    /**
     * Don't show any dividers.
     */
    public static final int SHOW_DIVIDER_NONE = 0;
    /**
     * Show a divider at the beginning of the group.
     */
    public static final int SHOW_DIVIDER_BEGINNING = 1;
    /**
     * Show dividers between each item in the group.
     */
    public static final int SHOW_DIVIDER_MIDDLE = 2;
    /**
     * Show a divider at the end of the group.
     */
    public static final int SHOW_DIVIDER_END = 4;

    /**
     * Whether the children of this layout are baseline aligned.  Only applicable
     * if {@link #mOrientation} is horizontal.
     */
    @ViewDebug.ExportedProperty(category = "layout")
    private boolean mBaselineAligned = true;

    /**
     * If this layout is part of another layout that is baseline aligned,
     * use the child at this index as the baseline.
     *
     * Note: this is orthogonal to {@link #mBaselineAligned}, which is concerned
     * with whether the children of this layout are baseline aligned.
     */
    @ViewDebug.ExportedProperty(category = "layout")
    private int mBaselineAlignedChildIndex = -1;

    /**
     * The additional offset to the child's baseline.
     * We'll calculate the baseline of this layout as we measure vertically; for
     * horizontal linear layouts, the offset of 0 is appropriate.
     */
    @ViewDebug.ExportedProperty(category = "measurement")
    private int mBaselineChildTop = 0;

    @ViewDebug.ExportedProperty(category = "measurement")
    private int mOrientation;

    @ViewDebug.ExportedProperty(category = "measurement", flagMapping = {
            @ViewDebug.FlagToString(mask = -1,
                    equals = -1, name = "NONE"),
            @ViewDebug.FlagToString(mask = Gravity.NO_GRAVITY,
                    equals = Gravity.NO_GRAVITY,name = "NONE"),
            @ViewDebug.FlagToString(mask = Gravity.TOP,
                    equals = Gravity.TOP, name = "TOP"),
            @ViewDebug.FlagToString(mask = Gravity.BOTTOM,
                    equals = Gravity.BOTTOM, name = "BOTTOM"),
            @ViewDebug.FlagToString(mask = Gravity.LEFT,
                    equals = Gravity.LEFT, name = "LEFT"),
            @ViewDebug.FlagToString(mask = Gravity.RIGHT,
                    equals = Gravity.RIGHT, name = "RIGHT"),
            @ViewDebug.FlagToString(mask = Gravity.START,
                    equals = Gravity.START, name = "START"),
            @ViewDebug.FlagToString(mask = Gravity.END,
                    equals = Gravity.END, name = "END"),
            @ViewDebug.FlagToString(mask = Gravity.CENTER_VERTICAL,
                    equals = Gravity.CENTER_VERTICAL, name = "CENTER_VERTICAL"),
            @ViewDebug.FlagToString(mask = Gravity.FILL_VERTICAL,
                    equals = Gravity.FILL_VERTICAL, name = "FILL_VERTICAL"),
            @ViewDebug.FlagToString(mask = Gravity.CENTER_HORIZONTAL,
                    equals = Gravity.CENTER_HORIZONTAL, name = "CENTER_HORIZONTAL"),
            @ViewDebug.FlagToString(mask = Gravity.FILL_HORIZONTAL,
                    equals = Gravity.FILL_HORIZONTAL, name = "FILL_HORIZONTAL"),
            @ViewDebug.FlagToString(mask = Gravity.CENTER,
                    equals = Gravity.CENTER, name = "CENTER"),
            @ViewDebug.FlagToString(mask = Gravity.FILL,
                    equals = Gravity.FILL, name = "FILL"),
            @ViewDebug.FlagToString(mask = Gravity.RELATIVE_LAYOUT_DIRECTION,
                    equals = Gravity.RELATIVE_LAYOUT_DIRECTION, name = "RELATIVE")
    }, formatToHexString = true)
    private int mGravity = Gravity.START | Gravity.TOP;

    @ViewDebug.ExportedProperty(category = "measurement")
    private int mTotalLength;

    @ViewDebug.ExportedProperty(category = "layout")
    private float mWeightSum;

    @ViewDebug.ExportedProperty(category = "layout")
    private boolean mUseLargestChild;

    private int[] mMaxAscent;
    private int[] mMaxDescent;

    private static final int VERTICAL_GRAVITY_COUNT = 4;

    private static final int INDEX_CENTER_VERTICAL = 0;
    private static final int INDEX_TOP = 1;
    private static final int INDEX_BOTTOM = 2;
    private static final int INDEX_FILL = 3;

    private Drawable mDivider;
    private int mDividerWidth;
    private int mDividerHeight;
    private int mShowDividers;
    private int mDividerPadding;

    public static final int LAYOUT_DIRECTION_UNDEFINED = -1;

    private int mLayoutDirection = LAYOUT_DIRECTION_UNDEFINED;


    public static class Builder implements IBuilder {
        @Override
        public String getName() {
            return "VLinearLayout";
        }

        @Override
        public IView build(PageContext pageContext, ViewCache viewCache) {
            return new VLinearLayout(pageContext, viewCache);
        }
    }

    public VLinearLayout(PageContext pageContext, ViewCache viewCache) {
        super(pageContext, viewCache);
    }

    @Override
    public LinearLayout.LayoutParams generateLayoutParams() {
        return new LinearLayout.LayoutParams(ViewGroup.MarginLayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
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

    public void setShowDividers(@DividerMode int showDividers) {
        if (showDividers != mShowDividers) {
//            requestLayout();
        }
        mShowDividers = showDividers;
    }

    /**
     * @return A flag set indicating how dividers should be shown around items.
     * @see #setShowDividers(int)
     */
    @DividerMode
    public int getShowDividers() {
        return mShowDividers;
    }

    /**
    public void setDividerPadding(int padding) {
        mDividerPadding = padding;
    }

    /**
     * Get the padding size used to inset dividers in pixels
     *
     * @see #setShowDividers(int)
     */
    public int getDividerPadding() {
        return mDividerPadding;
    }

    /**
     * <p>Returns the view at the specified index. This method can be overriden
     * to take into account virtual children. Refer to
     * {@link android.widget.TableLayout} and {@link android.widget.TableRow}
     * for an example.</p>
     *
     * @param index the child's index
     * @return the child at the specified index
     */
    IView getVirtualChildAt(int index) {
        return mChildren.get(index);
    }

    /**
     * <p>Returns the virtual number of children. This number might be different
     * than the actual number of children if the layout can hold virtual
     * children. Refer to
     * {@link android.widget.TableLayout} and {@link android.widget.TableRow}
     * for an example.</p>
     *
     * @return the virtual number of children
     */
    int getVirtualChildCount() {
        return mChildren.size();
    }

    /**
     * Returns the desired weights sum.
     *
     * @return A number greater than 0.0f if the weight sum is defined, or
     *         a number lower than or equals to 0.0f if not weight sum is
     *         to be used.
     */
    public float getWeightSum() {
        return mWeightSum;
    }

    /**
     * Defines the desired weights sum. If unspecified the weights sum is computed
     * at layout time by adding the layout_weight of each child.
     *
     * This can be used for instance to give a single child 50% of the total
     * available space by giving it a layout_weight of 0.5 and setting the
     * weightSum to 1.0.
     *
     * @param weightSum a number greater than 0.0f, or a number lower than or equals
     *        to 0.0f if the weight sum should be computed from the children's
     *        layout_weight
     */
    public void setWeightSum(float weightSum) {
        mWeightSum = Math.max(0.0f, weightSum);
    }

    @Override
    public void onVMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mOrientation == VERTICAL) {
            measureVertical(widthMeasureSpec, heightMeasureSpec);
        } else {
            measureHorizontal(widthMeasureSpec, heightMeasureSpec);
        }
    }

    /**
     * Determines where to position dividers between children.
     *
     * @param childIndex Index of child to check for preceding divider
     * @return true if there should be a divider before the child at childIndex
     * @hide Pending API consideration. Currently only used internally by the system.
     */
    protected boolean hasDividerBeforeChildAt(int childIndex) {
        if (childIndex == getVirtualChildCount()) {
            // Check whether the end divider should draw.
            return (mShowDividers & SHOW_DIVIDER_END) != 0;
        }
        boolean allViewsAreGoneBefore = allViewsAreGoneBefore(childIndex);
        if (allViewsAreGoneBefore) {
            // This is the first view that's not gone, check if beginning divider is enabled.
            return (mShowDividers & SHOW_DIVIDER_BEGINNING) != 0;
        } else {
            return (mShowDividers & SHOW_DIVIDER_MIDDLE) != 0;
        }
    }

    /**
     * Checks whether all (virtual) child views before the given index are gone.
     */
    private boolean allViewsAreGoneBefore(int childIndex) {
        for (int i = childIndex - 1; i >= 0; i--) {
            IView child = getVirtualChildAt(i);
            if (child != null && !child.isGone()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Measures the children when the orientation of this LinearLayout is set
     * to {@link #VERTICAL}.
     *
     * @param widthMeasureSpec Horizontal space requirements as imposed by the parent.
     * @param heightMeasureSpec Vertical space requirements as imposed by the parent.
     *
     * @see #getOrientation()
     * @see #setOrientation(int)
     */
    void measureVertical(int widthMeasureSpec, int heightMeasureSpec) {
        mTotalLength = 0;
        int maxWidth = 0;
        int childState = 0;
        int alternativeMaxWidth = 0;
        int weightedMaxWidth = 0;
        boolean allFillParent = true;
        float totalWeight = 0;

        final int count = getVirtualChildCount();

        final int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);

        boolean matchWidth = false;
        boolean skippedMeasure = false;

        final int baselineChildIndex = mBaselineAlignedChildIndex;
        final boolean useLargestChild = mUseLargestChild;

        int largestChildHeight = Integer.MIN_VALUE;

        // See how tall everyone is. Also remember max width.
        for (int i = 0; i < count; ++i) {
            final IView child = getVirtualChildAt(i);

            if (child == null) {
                mTotalLength += measureNullChild(i);
                continue;
            }

            if (child.isGone()) {
                i += getChildrenSkipCount(child, i);
                continue;
            }

            if (hasDividerBeforeChildAt(i)) {
                mTotalLength += mDividerHeight;
            }

            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) child.getVLayoutParams();

            totalWeight += lp.weight;

            if (heightMode == View.MeasureSpec.EXACTLY && lp.height == 0 && lp.weight > 0) {
                // Optimization: don't bother measuring children who are going to use
                // leftover space. These views will get measured again down below if
                // there is any leftover space.
                final int totalLength = mTotalLength;
                mTotalLength = Math.max(totalLength, totalLength + lp.topMargin + lp.bottomMargin);
                skippedMeasure = true;
            } else {
                int oldHeight = Integer.MIN_VALUE;

                if (lp.height == 0 && lp.weight > 0) {
                    // heightMode is either UNSPECIFIED or AT_MOST, and this
                    // child wanted to stretch to fill available space.
                    // Translate that to WRAP_CONTENT so that it does not end up
                    // with a height of 0
                    oldHeight = 0;
                    lp.height = LayoutParams.WRAP_CONTENT;
                }

                // Determine how big this child would like to be. If this or
                // previous children have given a weight, then we allow it to
                // use all available space (and we will shrink things later
                // if needed).
                measureChildBeforeLayout(
                        child, i, widthMeasureSpec, 0, heightMeasureSpec,
                        totalWeight == 0 ? mTotalLength : 0);

                if (oldHeight != Integer.MIN_VALUE) {
                    lp.height = oldHeight;
                }

                final int childHeight = child.getVMeasuredHeight();
                final int totalLength = mTotalLength;
                mTotalLength = Math.max(totalLength, totalLength + childHeight + lp.topMargin +
                        lp.bottomMargin + getNextLocationOffset(child));

                if (useLargestChild) {
                    largestChildHeight = Math.max(childHeight, largestChildHeight);
                }
            }

            /**
             * If applicable, compute the additional offset to the child's baseline
             * we'll need later when asked {@link #getBaseline}.
             */
            if ((baselineChildIndex >= 0) && (baselineChildIndex == i + 1)) {
                mBaselineChildTop = mTotalLength;
            }

            // if we are trying to use a child index for our baseline, the above
            // book keeping only works if there are no children above it with
            // weight.  fail fast to aid the developer.
            if (i < baselineChildIndex && lp.weight > 0) {
                throw new RuntimeException("A child of LinearLayout with index "
                        + "less than mBaselineAlignedChildIndex has weight > 0, which "
                        + "won't work.  Either remove the weight, or don't set "
                        + "mBaselineAlignedChildIndex.");
            }

            boolean matchWidthLocally = false;
            if (widthMode != View.MeasureSpec.EXACTLY && lp.width == LayoutParams.MATCH_PARENT) {
                // The width of the linear layout will scale, and at least one
                // child said it wanted to match our width. Set a flag
                // indicating that we need to remeasure at least that view when
                // we know our width.
                matchWidth = true;
                matchWidthLocally = true;
            }

            final int margin = lp.leftMargin + lp.rightMargin;
            final int measuredWidth = child.getVMeasuredWidth() + margin;
            maxWidth = Math.max(maxWidth, measuredWidth);
            childState = combineMeasuredStates(childState, getMeasuredState());

            allFillParent = allFillParent && lp.width == LayoutParams.MATCH_PARENT;
            if (lp.weight > 0) {
                /*
                 * Widths of weighted Views are bogus if we end up
                 * remeasuring, so keep them separate.
                 */
                weightedMaxWidth = Math.max(weightedMaxWidth,
                        matchWidthLocally ? margin : measuredWidth);
            } else {
                alternativeMaxWidth = Math.max(alternativeMaxWidth,
                        matchWidthLocally ? margin : measuredWidth);
            }

            i += getChildrenSkipCount(child, i);
        }

        if (mTotalLength > 0 && hasDividerBeforeChildAt(count)) {
            mTotalLength += mDividerHeight;
        }

        if (useLargestChild &&
                (heightMode == View.MeasureSpec.AT_MOST || heightMode == View.MeasureSpec.UNSPECIFIED)) {
            mTotalLength = 0;

            for (int i = 0; i < count; ++i) {
                final IView child = getVirtualChildAt(i);

                if (child == null) {
                    mTotalLength += measureNullChild(i);
                    continue;
                }

                if (child.isGone()) {
                    i += getChildrenSkipCount(child, i);
                    continue;
                }

                final LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)
                        child.getVLayoutParams();
                // Account for negative margins
                final int totalLength = mTotalLength;
                mTotalLength = Math.max(totalLength, totalLength + largestChildHeight +
                        lp.topMargin + lp.bottomMargin + getNextLocationOffset(child));
            }
        }

        // Add in our padding
        mTotalLength += mPaddingTop + mPaddingBottom;

        int heightSize = mTotalLength;

        // Check against our minimum height
        heightSize = Math.max(heightSize, 20);

        // Reconcile our calculated size with the heightMeasureSpec
        int heightSizeAndState = resolveSizeAndState(heightSize, heightMeasureSpec, 0);
        heightSize = heightSizeAndState & MEASURED_SIZE_MASK;

        // Either expand children with weight to take up available space or
        // shrink them if they extend beyond our current bounds. If we skipped
        // measurement on any children, we need to measure them now.
        int delta = heightSize - mTotalLength;
        if (skippedMeasure || delta != 0 && totalWeight > 0.0f) {
            float weightSum = mWeightSum > 0.0f ? mWeightSum : totalWeight;

            mTotalLength = 0;

            for (int i = 0; i < count; ++i) {
                final IView child = getVirtualChildAt(i);

                if (child.isGone()) {
                    continue;
                }

                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) child.getVLayoutParams();

                float childExtra = lp.weight;
                if (childExtra > 0) {
                    // Child said it could absorb extra space -- give him his share
                    int share = (int) (childExtra * delta / weightSum);
                    weightSum -= childExtra;
                    delta -= share;

                    final int childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec,
                            mPaddingLeft + mPaddingRight +
                                    lp.leftMargin + lp.rightMargin, lp.width);

                    // TODO: Use a field like lp.isMeasured to figure out if this
                    // child has been previously measured
                    if ((lp.height != 0) || (heightMode != View.MeasureSpec.EXACTLY)) {
                        // child was measured once already above...
                        // base new measurement on stored values
                        int childHeight = child.getVMeasuredHeight() + share;
                        if (childHeight < 0) {
                            childHeight = 0;
                        }

                        child.vMeasure(childWidthMeasureSpec,
                                View.MeasureSpec.makeMeasureSpec(childHeight, View.MeasureSpec.EXACTLY));
                    } else {
                        // child was skipped in the loop above.
                        // Measure for this first time here
                        child.vMeasure(childWidthMeasureSpec,
                                View.MeasureSpec.makeMeasureSpec(share > 0 ? share : 0,
                                        View.MeasureSpec.EXACTLY));
                    }

                    // Child may now not fit in vertical dimension.
                    childState = combineMeasuredStates(childState, getMeasuredState()
                            & (MEASURED_STATE_MASK>>MEASURED_HEIGHT_STATE_SHIFT));
                }

                final int margin =  lp.leftMargin + lp.rightMargin;
                final int measuredWidth = child.getVMeasuredWidth() + margin;
                maxWidth = Math.max(maxWidth, measuredWidth);

                boolean matchWidthLocally = widthMode != View.MeasureSpec.EXACTLY &&
                        lp.width == LayoutParams.MATCH_PARENT;

                alternativeMaxWidth = Math.max(alternativeMaxWidth,
                        matchWidthLocally ? margin : measuredWidth);

                allFillParent = allFillParent && lp.width == LayoutParams.MATCH_PARENT;

                final int totalLength = mTotalLength;
                mTotalLength = Math.max(totalLength, totalLength + child.getVMeasuredHeight() +
                        lp.topMargin + lp.bottomMargin + getNextLocationOffset(child));
            }

            // Add in our padding
            mTotalLength += mPaddingTop + mPaddingBottom;
            // TODO: Should we recompute the heightSpec based on the new total length?
        } else {
            alternativeMaxWidth = Math.max(alternativeMaxWidth,
                    weightedMaxWidth);


            // We have no limit, so make all weighted views as tall as the largest child.
            // Children will have already been measured once.
            if (useLargestChild && heightMode != View.MeasureSpec.EXACTLY) {
                for (int i = 0; i < count; i++) {
                    final IView child = getVirtualChildAt(i);

                    if (child == null || child.isGone()) {
                        continue;
                    }

                    final LinearLayout.LayoutParams lp =
                            (LinearLayout.LayoutParams) child.getVLayoutParams();

                    float childExtra = lp.weight;
                    if (childExtra > 0) {
                        child.vMeasure(
                                View.MeasureSpec.makeMeasureSpec(child.getVMeasuredWidth(),
                                        View.MeasureSpec.EXACTLY),
                                View.MeasureSpec.makeMeasureSpec(largestChildHeight,
                                        View.MeasureSpec.EXACTLY));
                    }
                }
            }
        }

        if (!allFillParent && widthMode != View.MeasureSpec.EXACTLY) {
            maxWidth = alternativeMaxWidth;
        }

        maxWidth += mPaddingLeft + mPaddingRight;

        // Check against our minimum width
        maxWidth = Math.max(maxWidth, 20);

        setVMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, childState),
                heightSizeAndState);

        if (matchWidth) {
            forceUniformWidth(count, heightMeasureSpec);
        }
    }

    public final int getMeasuredState() {
        return (mMeasuredWidth&MEASURED_STATE_MASK)
                | ((mMeasuredHeight>>MEASURED_HEIGHT_STATE_SHIFT)
                & (MEASURED_STATE_MASK>>MEASURED_HEIGHT_STATE_SHIFT));
    }

    private void forceUniformWidth(int count, int heightMeasureSpec) {
        // Pretend that the linear layout has an exact size.
        int uniformMeasureSpec = View.MeasureSpec.makeMeasureSpec(getVMeasuredWidth(),
                View.MeasureSpec.EXACTLY);
        for (int i = 0; i< count; ++i) {
            final IView child = getVirtualChildAt(i);
            if (!child.isGone()) {
                LinearLayout.LayoutParams lp = ((LinearLayout.LayoutParams)child.getVLayoutParams());

                if (lp.width == LayoutParams.MATCH_PARENT) {
                    // Temporarily force children to reuse their old measured height
                    // FIXME: this may not be right for something like wrapping text?
                    int oldHeight = lp.height;
                    lp.height = child.getVMeasuredHeight();

                    // Remeasue with new dimensions
                    measureChildWithMargins(child, uniformMeasureSpec, 0, heightMeasureSpec, 0);
                    lp.height = oldHeight;
                }
            }
        }
    }

    protected void measureChildWithMargins(IView child,
                                           int parentWidthMeasureSpec, int widthUsed,
                                           int parentHeightMeasureSpec, int heightUsed) {
        final LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) child.getVLayoutParams();

        final int childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec,
                mPaddingLeft + mPaddingRight + lp.leftMargin + lp.rightMargin
                        + widthUsed, lp.width);
        final int childHeightMeasureSpec = getChildMeasureSpec(parentHeightMeasureSpec,
                mPaddingTop + mPaddingBottom + lp.topMargin + lp.bottomMargin
                        + heightUsed, lp.height);

        child.vMeasure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    /**
     * Measures the children when the orientation of this LinearLayout is set
     * to {@link #HORIZONTAL}.
     *
     * @param widthMeasureSpec Horizontal space requirements as imposed by the parent.
     * @param heightMeasureSpec Vertical space requirements as imposed by the parent.
     *
     * @see #getOrientation()
     * @see #setOrientation(int)
     */
    void measureHorizontal(int widthMeasureSpec, int heightMeasureSpec) {
        mTotalLength = 0;
        int maxHeight = 0;
        int childState = 0;
        int alternativeMaxHeight = 0;
        int weightedMaxHeight = 0;
        boolean allFillParent = true;
        float totalWeight = 0;

        final int count = getVirtualChildCount();

        final int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);

        boolean matchHeight = false;
        boolean skippedMeasure = false;

        if (mMaxAscent == null || mMaxDescent == null) {
            mMaxAscent = new int[VERTICAL_GRAVITY_COUNT];
            mMaxDescent = new int[VERTICAL_GRAVITY_COUNT];
        }

        final int[] maxAscent = mMaxAscent;
        final int[] maxDescent = mMaxDescent;

        maxAscent[0] = maxAscent[1] = maxAscent[2] = maxAscent[3] = -1;
        maxDescent[0] = maxDescent[1] = maxDescent[2] = maxDescent[3] = -1;

        final boolean baselineAligned = mBaselineAligned;
        final boolean useLargestChild = mUseLargestChild;

        final boolean isExactly = widthMode == View.MeasureSpec.EXACTLY;

        int largestChildWidth = Integer.MIN_VALUE;

        // See how wide everyone is. Also remember max height.
        for (int i = 0; i < count; ++i) {
            final IView child = getVirtualChildAt(i);

            if (child == null) {
                mTotalLength += measureNullChild(i);
                continue;
            }

            if (child.isGone()) {
                i += getChildrenSkipCount(child, i);
                continue;
            }

            if (hasDividerBeforeChildAt(i)) {
                mTotalLength += mDividerWidth;
            }

            final LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)
                    child.getVLayoutParams();

            totalWeight += lp.weight;

            if (widthMode == View.MeasureSpec.EXACTLY && lp.width == 0 && lp.weight > 0) {
                // Optimization: don't bother measuring children who are going to use
                // leftover space. These views will get measured again down below if
                // there is any leftover space.
                if (isExactly) {
                    mTotalLength += lp.leftMargin + lp.rightMargin;
                } else {
                    final int totalLength = mTotalLength;
                    mTotalLength = Math.max(totalLength, totalLength +
                            lp.leftMargin + lp.rightMargin);
                }

                // Baseline alignment requires to measure widgets to obtain the
                // baseline offset (in particular for TextViews). The following
                // defeats the optimization mentioned above. Allow the child to
                // use as much space as it wants because we can shrink things
                // later (and re-measure).
                if (baselineAligned) {
                    final int freeWidthSpec = View.MeasureSpec.makeMeasureSpec(
                            View.MeasureSpec.getSize(widthMeasureSpec), View.MeasureSpec.UNSPECIFIED);
                    final int freeHeightSpec = View.MeasureSpec.makeMeasureSpec(
                            View.MeasureSpec.getSize(heightMeasureSpec), View.MeasureSpec.UNSPECIFIED);
                    child.vMeasure(freeWidthSpec, freeHeightSpec);
                } else {
                    skippedMeasure = true;
                }
            } else {
                int oldWidth = Integer.MIN_VALUE;

                if (lp.width == 0 && lp.weight > 0) {
                    // widthMode is either UNSPECIFIED or AT_MOST, and this
                    // child
                    // wanted to stretch to fill available space. Translate that to
                    // WRAP_CONTENT so that it does not end up with a width of 0
                    oldWidth = 0;
                    lp.width = LayoutParams.WRAP_CONTENT;
                }

                // Determine how big this child would like to be. If this or
                // previous children have given a weight, then we allow it to
                // use all available space (and we will shrink things later
                // if needed).
                measureChildBeforeLayout(child, i, widthMeasureSpec,
                        totalWeight == 0 ? mTotalLength : 0,
                        heightMeasureSpec, 0);

                if (oldWidth != Integer.MIN_VALUE) {
                    lp.width = oldWidth;
                }

                final int childWidth = child.getVMeasuredWidth();
                if (isExactly) {
                    mTotalLength += childWidth + lp.leftMargin + lp.rightMargin +
                            getNextLocationOffset(child);
                } else {
                    final int totalLength = mTotalLength;
                    mTotalLength = Math.max(totalLength, totalLength + childWidth + lp.leftMargin +
                            lp.rightMargin + getNextLocationOffset(child));
                }

                if (useLargestChild) {
                    largestChildWidth = Math.max(childWidth, largestChildWidth);
                }
            }

            boolean matchHeightLocally = false;
            if (heightMode != View.MeasureSpec.EXACTLY && lp.height == LayoutParams.MATCH_PARENT) {
                // The height of the linear layout will scale, and at least one
                // child said it wanted to match our height. Set a flag indicating that
                // we need to remeasure at least that view when we know our height.
                matchHeight = true;
                matchHeightLocally = true;
            }

            final int margin = lp.topMargin + lp.bottomMargin;
            final int childHeight = child.getVMeasuredHeight() + margin;
            childState = combineMeasuredStates(childState, getMeasuredState());

            if (baselineAligned) {
                final int childBaseline = -1;
                if (childBaseline != -1) {
                    // Translates the child's vertical gravity into an index
                    // in the range 0..VERTICAL_GRAVITY_COUNT
                    final int gravity = (lp.gravity < 0 ? mGravity : lp.gravity)
                            & Gravity.VERTICAL_GRAVITY_MASK;
                    final int index = ((gravity >> Gravity.AXIS_Y_SHIFT)
                            & ~Gravity.AXIS_SPECIFIED) >> 1;

                    maxAscent[index] = Math.max(maxAscent[index], childBaseline);
                    maxDescent[index] = Math.max(maxDescent[index], childHeight - childBaseline);
                }
            }

            maxHeight = Math.max(maxHeight, childHeight);

            allFillParent = allFillParent && lp.height == LayoutParams.MATCH_PARENT;
            if (lp.weight > 0) {
                /*
                 * Heights of weighted Views are bogus if we end up
                 * remeasuring, so keep them separate.
                 */
                weightedMaxHeight = Math.max(weightedMaxHeight,
                        matchHeightLocally ? margin : childHeight);
            } else {
                alternativeMaxHeight = Math.max(alternativeMaxHeight,
                        matchHeightLocally ? margin : childHeight);
            }

            i += getChildrenSkipCount(child, i);
        }

        if (mTotalLength > 0 && hasDividerBeforeChildAt(count)) {
            mTotalLength += mDividerWidth;
        }

        // Check mMaxAscent[INDEX_TOP] first because it maps to Gravity.TOP,
        // the most common case
        if (maxAscent[INDEX_TOP] != -1 ||
                maxAscent[INDEX_CENTER_VERTICAL] != -1 ||
                maxAscent[INDEX_BOTTOM] != -1 ||
                maxAscent[INDEX_FILL] != -1) {
            final int ascent = Math.max(maxAscent[INDEX_FILL],
                    Math.max(maxAscent[INDEX_CENTER_VERTICAL],
                            Math.max(maxAscent[INDEX_TOP], maxAscent[INDEX_BOTTOM])));
            final int descent = Math.max(maxDescent[INDEX_FILL],
                    Math.max(maxDescent[INDEX_CENTER_VERTICAL],
                            Math.max(maxDescent[INDEX_TOP], maxDescent[INDEX_BOTTOM])));
            maxHeight = Math.max(maxHeight, ascent + descent);
        }

        if (useLargestChild &&
                (widthMode == View.MeasureSpec.AT_MOST || widthMode == View.MeasureSpec.UNSPECIFIED)) {
            mTotalLength = 0;

            for (int i = 0; i < count; ++i) {
                final IView child = getVirtualChildAt(i);

                if (child == null) {
                    mTotalLength += measureNullChild(i);
                    continue;
                }

                if (child.isGone()) {
                    i += getChildrenSkipCount(child, i);
                    continue;
                }

                final LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)
                        child.getVLayoutParams();
                if (isExactly) {
                    mTotalLength += largestChildWidth + lp.leftMargin + lp.rightMargin +
                            getNextLocationOffset(child);
                } else {
                    final int totalLength = mTotalLength;
                    mTotalLength = Math.max(totalLength, totalLength + largestChildWidth +
                            lp.leftMargin + lp.rightMargin + getNextLocationOffset(child));
                }
            }
        }

        // Add in our padding
        mTotalLength += mPaddingLeft + mPaddingRight;

        int widthSize = mTotalLength;

        // Check against our minimum width
        widthSize = Math.max(widthSize, 20);

        // Reconcile our calculated size with the widthMeasureSpec
        int widthSizeAndState = resolveSizeAndState(widthSize, widthMeasureSpec, 0);
        widthSize = widthSizeAndState & MEASURED_SIZE_MASK;

        // Either expand children with weight to take up available space or
        // shrink them if they extend beyond our current bounds. If we skipped
        // measurement on any children, we need to measure them now.
        int delta = widthSize - mTotalLength;
        if (skippedMeasure || delta != 0 && totalWeight > 0.0f) {
            float weightSum = mWeightSum > 0.0f ? mWeightSum : totalWeight;

            maxAscent[0] = maxAscent[1] = maxAscent[2] = maxAscent[3] = -1;
            maxDescent[0] = maxDescent[1] = maxDescent[2] = maxDescent[3] = -1;
            maxHeight = -1;

            mTotalLength = 0;

            for (int i = 0; i < count; ++i) {
                final IView child = getVirtualChildAt(i);

                if (child == null || child.isGone()) {
                    continue;
                }

                final LinearLayout.LayoutParams lp =
                        (LinearLayout.LayoutParams) child.getVLayoutParams();

                float childExtra = lp.weight;
                if (childExtra > 0) {
                    // Child said it could absorb extra space -- give him his share
                    int share = (int) (childExtra * delta / weightSum);
                    weightSum -= childExtra;
                    delta -= share;

                    final int childHeightMeasureSpec = getChildMeasureSpec(
                            heightMeasureSpec,
                            mPaddingTop + mPaddingBottom + lp.topMargin + lp.bottomMargin,
                            lp.height);

                    // TODO: Use a field like lp.isMeasured to figure out if this
                    // child has been previously measured
                    if ((lp.width != 0) || (widthMode != View.MeasureSpec.EXACTLY)) {
                        // child was measured once already above ... base new measurement
                        // on stored values
                        int childWidth = child.getVMeasuredWidth() + share;
                        if (childWidth < 0) {
                            childWidth = 0;
                        }

                        child.vMeasure(
                                View.MeasureSpec.makeMeasureSpec(childWidth, View.MeasureSpec.EXACTLY),
                                childHeightMeasureSpec);
                    } else {
                        // child was skipped in the loop above. Measure for this first time here
                        child.vMeasure(View.MeasureSpec.makeMeasureSpec(
                                share > 0 ? share : 0, View.MeasureSpec.EXACTLY),
                                childHeightMeasureSpec);
                    }

                    // Child may now not fit in horizontal dimension.
                    childState = combineMeasuredStates(childState,
                            getMeasuredState() & MEASURED_STATE_MASK);
                }

                if (isExactly) {
                    mTotalLength += child.getVMeasuredWidth() + lp.leftMargin + lp.rightMargin +
                            getNextLocationOffset(child);
                } else {
                    final int totalLength = mTotalLength;
                    mTotalLength = Math.max(totalLength, totalLength + child.getVMeasuredWidth() +
                            lp.leftMargin + lp.rightMargin + getNextLocationOffset(child));
                }

                boolean matchHeightLocally = heightMode != View.MeasureSpec.EXACTLY &&
                        lp.height == LayoutParams.MATCH_PARENT;

                final int margin = lp.topMargin + lp .bottomMargin;
                int childHeight = child.getVMeasuredHeight() + margin;
                maxHeight = Math.max(maxHeight, childHeight);
                alternativeMaxHeight = Math.max(alternativeMaxHeight,
                        matchHeightLocally ? margin : childHeight);

                allFillParent = allFillParent && lp.height == LayoutParams.MATCH_PARENT;

                if (baselineAligned) {
                    final int childBaseline = -1;
                    if (childBaseline != -1) {
                        // Translates the child's vertical gravity into an index in the range 0..2
                        final int gravity = (lp.gravity < 0 ? mGravity : lp.gravity)
                                & Gravity.VERTICAL_GRAVITY_MASK;
                        final int index = ((gravity >> Gravity.AXIS_Y_SHIFT)
                                & ~Gravity.AXIS_SPECIFIED) >> 1;

                        maxAscent[index] = Math.max(maxAscent[index], childBaseline);
                        maxDescent[index] = Math.max(maxDescent[index],
                                childHeight - childBaseline);
                    }
                }
            }

            // Add in our padding
            mTotalLength += mPaddingLeft + mPaddingRight;
            // TODO: Should we update widthSize with the new total length?

            // Check mMaxAscent[INDEX_TOP] first because it maps to Gravity.TOP,
            // the most common case
            if (maxAscent[INDEX_TOP] != -1 ||
                    maxAscent[INDEX_CENTER_VERTICAL] != -1 ||
                    maxAscent[INDEX_BOTTOM] != -1 ||
                    maxAscent[INDEX_FILL] != -1) {
                final int ascent = Math.max(maxAscent[INDEX_FILL],
                        Math.max(maxAscent[INDEX_CENTER_VERTICAL],
                                Math.max(maxAscent[INDEX_TOP], maxAscent[INDEX_BOTTOM])));
                final int descent = Math.max(maxDescent[INDEX_FILL],
                        Math.max(maxDescent[INDEX_CENTER_VERTICAL],
                                Math.max(maxDescent[INDEX_TOP], maxDescent[INDEX_BOTTOM])));
                maxHeight = Math.max(maxHeight, ascent + descent);
            }
        } else {
            alternativeMaxHeight = Math.max(alternativeMaxHeight, weightedMaxHeight);

            // We have no limit, so make all weighted views as wide as the largest child.
            // Children will have already been measured once.
            if (useLargestChild && widthMode != View.MeasureSpec.EXACTLY) {
                for (int i = 0; i < count; i++) {
                    final IView child = getVirtualChildAt(i);

                    if (child == null || child.isGone()) {
                        continue;
                    }

                    final LinearLayout.LayoutParams lp =
                            (LinearLayout.LayoutParams) child.getVLayoutParams();

                    float childExtra = lp.weight;
                    if (childExtra > 0) {
                        child.vMeasure(
                                View.MeasureSpec.makeMeasureSpec(largestChildWidth, View.MeasureSpec.EXACTLY),
                                View.MeasureSpec.makeMeasureSpec(child.getVMeasuredHeight(),
                                        View.MeasureSpec.EXACTLY));
                    }
                }
            }
        }

        if (!allFillParent && heightMode != View.MeasureSpec.EXACTLY) {
            maxHeight = alternativeMaxHeight;
        }

        maxHeight += mPaddingTop + mPaddingBottom;

        // Check against our minimum height
        maxHeight = Math.max(maxHeight, 20);

        setVMeasuredDimension(widthSizeAndState | (childState&MEASURED_STATE_MASK),
                resolveSizeAndState(maxHeight, heightMeasureSpec,
                        (childState<<MEASURED_HEIGHT_STATE_SHIFT)));

        if (matchHeight) {
            forceUniformHeight(count, widthMeasureSpec);
        }
    }

    private void forceUniformHeight(int count, int widthMeasureSpec) {
        // Pretend that the linear layout has an exact size. This is the measured height of
        // ourselves. The measured height should be the max height of the children, changed
        // to accommodate the heightMeasureSpec from the parent
        int uniformMeasureSpec = View.MeasureSpec.makeMeasureSpec(getVMeasuredHeight(),
                View.MeasureSpec.EXACTLY);
        for (int i = 0; i < count; ++i) {
            final IView child = getVirtualChildAt(i);
            if (!child.isGone()) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) child.getVLayoutParams();

                if (lp.height == LayoutParams.MATCH_PARENT) {
                    // Temporarily force children to reuse their old measured width
                    // FIXME: this may not be right for something like wrapping text?
                    int oldWidth = lp.width;
                    lp.width = child.getVMeasuredWidth();

                    // Remeasure with new dimensions
                    measureChildWithMargins(child, widthMeasureSpec, 0, uniformMeasureSpec, 0);
                    lp.width = oldWidth;
                }
            }
        }
    }

    /**
     * <p>Returns the number of children to skip after measuring/laying out
     * the specified child.</p>
     *
     * @param child the child after which we want to skip children
     * @param index the index of the child after which we want to skip children
     * @return the number of children to skip, 0 by default
     */
    int getChildrenSkipCount(IView child, int index) {
        return 0;
    }

    /**
     * <p>Returns the size (width or height) that should be occupied by a null
     * child.</p>
     *
     * @param childIndex the index of the null child
     * @return the width or height of the child depending on the orientation
     */
    int measureNullChild(int childIndex) {
        return 0;
    }

    /**
     * <p>Measure the child according to the parent's measure specs. This
     * method should be overriden by subclasses to force the sizing of
     * children. This method is called by {@link #measureVertical(int, int)} and
     * {@link #measureHorizontal(int, int)}.</p>
     *
     * @param child the child to measure
     * @param childIndex the index of the child in this view
     * @param widthMeasureSpec horizontal space requirements as imposed by the parent
     * @param totalWidth extra space that has been used up by the parent horizontally
     * @param heightMeasureSpec vertical space requirements as imposed by the parent
     * @param totalHeight extra space that has been used up by the parent vertically
     */
    void measureChildBeforeLayout(IView child, int childIndex,
                                  int widthMeasureSpec, int totalWidth, int heightMeasureSpec,
                                  int totalHeight) {
        measureChildWithMargins(child, widthMeasureSpec, totalWidth,
                heightMeasureSpec, totalHeight);
    }

    /**
     * <p>Return the location offset of the specified child. This can be used
     * by subclasses to change the location of a given widget.</p>
     *
     * @param child the child for which to obtain the location offset
     * @return the location offset in pixels
     */
    int getLocationOffset(IView child) {
        return 0;
    }

    /**
     * <p>Return the size offset of the next sibling of the specified child.
     * This can be used by subclasses to change the location of the widget
     * following <code>child</code>.</p>
     *
     * @param child the child whose next sibling will be moved
     * @return the location offset of the next child in pixels
     */
    int getNextLocationOffset(IView child) {
        return 0;
    }

    @Override
    public void onVLayout(boolean changed, int l, int t, int r, int b) {
        if (mOrientation == VERTICAL) {
            layoutVertical(l, t, r, b);
        } else {
            layoutHorizontal(l, t, r, b);
        }
    }

    /**
     * Position the children during a layout pass if the orientation of this
     * LinearLayout is set to {@link #VERTICAL}.
     *
     * @see #getOrientation()
     * @see #setOrientation(int)
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    void layoutVertical(int left, int top, int right, int bottom) {
        final int paddingLeft = mPaddingLeft;

        int childTop;
        int childLeft;

        // Where right end of child should go
        final int width = right - left;
        int childRight = width - mPaddingRight;

        // Space available for child
        int childSpace = width - paddingLeft - mPaddingRight;

        final int count = getVirtualChildCount();

        final int majorGravity = mGravity & Gravity.VERTICAL_GRAVITY_MASK;
        final int minorGravity = mGravity & Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK;

        switch (majorGravity) {
            case Gravity.BOTTOM:
                // mTotalLength contains the padding already
                childTop = mPaddingTop + bottom - top - mTotalLength;
                break;

            // mTotalLength contains the padding already
            case Gravity.CENTER_VERTICAL:
                childTop = mPaddingTop + (bottom - top - mTotalLength) / 2;
                break;

            case Gravity.TOP:
            default:
                childTop = mPaddingTop;
                break;
        }

        for (int i = 0; i < count; i++) {
            final IView child = getVirtualChildAt(i);
            if (child == null) {
                childTop += measureNullChild(i);
            } else if (!child.isGone()) {
                final int childWidth = child.getVMeasuredWidth();
                final int childHeight = child.getVMeasuredHeight();

                final LinearLayout.LayoutParams lp =
                        (LinearLayout.LayoutParams) child.getVLayoutParams();

                int gravity = lp.gravity;
                if (gravity < 0) {
                    gravity = minorGravity;
                }
                final int layoutDirection = -1;
                final int absoluteGravity = Gravity.getAbsoluteGravity(gravity, layoutDirection);
                switch (absoluteGravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
                    case Gravity.CENTER_HORIZONTAL:
                        childLeft = paddingLeft + ((childSpace - childWidth) / 2)
                                + lp.leftMargin - lp.rightMargin;
                        break;

                    case Gravity.RIGHT:
                        childLeft = childRight - childWidth - lp.rightMargin;
                        break;

                    case Gravity.LEFT:
                    default:
                        childLeft = paddingLeft + lp.leftMargin;
                        break;
                }

                if (hasDividerBeforeChildAt(i)) {
                    childTop += mDividerHeight;
                }

                childTop += lp.topMargin;
                setChildFrame(child, childLeft, childTop + getLocationOffset(child),
                        childWidth, childHeight);
                childTop += childHeight + lp.bottomMargin + getNextLocationOffset(child);

                i += getChildrenSkipCount(child, i);
            }
        }
    }

    /**
     * Position the children during a layout pass if the orientation of this
     * LinearLayout is set to {@link #HORIZONTAL}.
     *
     * @see #getOrientation()
     * @see #setOrientation(int)
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    void layoutHorizontal(int left, int top, int right, int bottom) {
        final boolean isLayoutRtl = false;
        final int paddingTop = mPaddingTop;

        int childTop;
        int childLeft;

        // Where bottom of child should go
        final int height = bottom - top;
        int childBottom = height - mPaddingBottom;

        // Space available for child
        int childSpace = height - paddingTop - mPaddingBottom;

        final int count = getVirtualChildCount();

        final int majorGravity = mGravity & Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK;
        final int minorGravity = mGravity & Gravity.VERTICAL_GRAVITY_MASK;

        final boolean baselineAligned = mBaselineAligned;

        final int[] maxAscent = mMaxAscent;
        final int[] maxDescent = mMaxDescent;

        final int layoutDirection = -1;
        switch (Gravity.getAbsoluteGravity(majorGravity, layoutDirection)) {
            case Gravity.RIGHT:
                // mTotalLength contains the padding already
                childLeft = mPaddingLeft + right - left - mTotalLength;
                break;

            case Gravity.CENTER_HORIZONTAL:
                // mTotalLength contains the padding already
                childLeft = mPaddingLeft + (right - left - mTotalLength) / 2;
                break;

            case Gravity.LEFT:
            default:
                childLeft = mPaddingLeft;
                break;
        }

        int start = 0;
        int dir = 1;
        //In case of RTL, start drawing from the last child.
        if (isLayoutRtl) {
            start = count - 1;
            dir = -1;
        }

        for (int i = 0; i < count; i++) {
            int childIndex = start + dir * i;
            final IView child = getVirtualChildAt(childIndex);

            if (child == null) {
                childLeft += measureNullChild(childIndex);
            } else if (!child.isGone()) {
                final int childWidth = child.getVMeasuredWidth();
                final int childHeight = child.getVMeasuredHeight();
                int childBaseline = -1;

                final LinearLayout.LayoutParams lp =
                        (LinearLayout.LayoutParams) child.getVLayoutParams();

                if (baselineAligned && lp.height != LayoutParams.MATCH_PARENT) {
                    childBaseline = -1;
                }

                int gravity = lp.gravity;
                if (gravity < 0) {
                    gravity = minorGravity;
                }

                switch (gravity & Gravity.VERTICAL_GRAVITY_MASK) {
                    case Gravity.TOP:
                        childTop = paddingTop + lp.topMargin;
                        if (childBaseline != -1) {
                            childTop += maxAscent[INDEX_TOP] - childBaseline;
                        }
                        break;

                    case Gravity.CENTER_VERTICAL:
                        // Removed support for baseline alignment when layout_gravity or
                        // gravity == center_vertical. See bug #1038483.
                        // Keep the code around if we need to re-enable this feature
                        // if (childBaseline != -1) {
                        //     // Align baselines vertically only if the child is smaller than us
                        //     if (childSpace - childHeight > 0) {
                        //         childTop = paddingTop + (childSpace / 2) - childBaseline;
                        //     } else {
                        //         childTop = paddingTop + (childSpace - childHeight) / 2;
                        //     }
                        // } else {
                        childTop = paddingTop + ((childSpace - childHeight) / 2)
                                + lp.topMargin - lp.bottomMargin;
                        break;

                    case Gravity.BOTTOM:
                        childTop = childBottom - childHeight - lp.bottomMargin;
                        if (childBaseline != -1) {
                            int descent = child.getVMeasuredHeight() - childBaseline;
                            childTop -= (maxDescent[INDEX_BOTTOM] - descent);
                        }
                        break;
                    default:
                        childTop = paddingTop;
                        break;
                }

                if (hasDividerBeforeChildAt(childIndex)) {
                    childLeft += mDividerWidth;
                }

                childLeft += lp.leftMargin;
                setChildFrame(child, childLeft + getLocationOffset(child), childTop,
                        childWidth, childHeight);
                childLeft += childWidth + lp.rightMargin +
                        getNextLocationOffset(child);

                i += getChildrenSkipCount(child, childIndex);
            }
        }
    }

    private void setChildFrame(IView child, int left, int top, int width, int height) {
        child.vLayout(left, top, left + width, top + height);
    }

    /**
     * Should the layout be a column or a row.
     * @param orientation Pass {@link #HORIZONTAL} or {@link #VERTICAL}. Default
     * value is {@link #HORIZONTAL}.
     *
     * @attr ref android.R.styleable#LinearLayout_orientation
     */
    public void setOrientation(@OrientationMode int orientation) {
        if (mOrientation != orientation) {
            mOrientation = orientation;
//            requestLayout();
        }
    }

    /**
     * Returns the current orientation.
     *
     * @return either {@link #HORIZONTAL} or {@link #VERTICAL}
     */
    @OrientationMode
    public int getOrientation() {
        return mOrientation;
    }

    /**
     * Describes how the child views are positioned. Defaults to GRAVITY_TOP. If
     * this layout has a VERTICAL orientation, this controls where all the child
     * views are placed if there is extra vertical space. If this layout has a
     * HORIZONTAL orientation, this controls the alignment of the children.
     *
     * @param gravity See {@link Gravity}
     *
     * @attr ref android.R.styleable#LinearLayout_gravity
     */
    public void setGravity(int gravity) {
        if (mGravity != gravity) {
            if ((gravity & Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK) == 0) {
                gravity |= Gravity.START;
            }

            if ((gravity & Gravity.VERTICAL_GRAVITY_MASK) == 0) {
                gravity |= Gravity.TOP;
            }

            mGravity = gravity;
        }
    }

}
