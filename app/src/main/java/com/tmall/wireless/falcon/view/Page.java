package com.tmall.wireless.falcon.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

import com.libra.common.Common;
import com.libra.common.StringTab;
import com.tmall.wireless.falcon.PageContext;
import com.tmall.wireless.falcon.core.IBean;
import com.tmall.wireless.falcon.core.IBuilder;
import com.tmall.wireless.falcon.core.IView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gujicheng on 17/5/13.
 */

public class Page extends ViewGroup implements IView {
    private final static String TAG = "Page";

    protected final static int MAX_ITEM_COUNT = 5;
    protected final static int DEFAULT_ANIMATOR_TIME_INTERVAL = 100;
    protected final static int DEFAULT_AUTO_SWITCH_TIME_INTERVAL = 500;
    protected final static int DEFAULT_STAY_TIME = 3000;
    protected final static int MSG_AUTO_SWITCH = 1;
    protected final static int VEL_THRESHOLD = 2000;

    protected SparseArray<List<Adapter.ViewHolder>> mItemCache = new SparseArray<>();

    protected Adapter mAdapter;
    protected int mCurPos;
    protected int mDownPos;
    protected boolean mIsNext;
    protected int mComponentId;

    protected int mStayTime = DEFAULT_STAY_TIME;
    protected int mAnimatorTimeInterval = DEFAULT_ANIMATOR_TIME_INTERVAL;
    protected int mAutoSwitchTimeInterval = DEFAULT_AUTO_SWITCH_TIME_INTERVAL;

    protected boolean mAutoSwitch = false;
    protected boolean mIsHorizontal = true;
    protected boolean mCanSlide = true;

    protected Handler mAutoSwitchHandler;

    protected boolean mDataChanged = true;

    private int mLastX;
    private int mLastY;

    private VelocityTracker mVelocityTracker;
    private int mPointerId;
    private int mMaxVelocity;

    protected MyAnimatorListener mAniListener = new MyAnimatorListener();
    protected Listener mListener;

    protected boolean mCanSwitch = true;
    protected Object mRawData;
    protected int mAutoDimDirection = Common.AUTO_DIM_DIRECTION_NONE;
    protected float mAutoDimX = 1;
    protected float mAutoDimY = 1;

    public interface Listener {
        void onPageFlip(int pos, int total);
    }

    private ViewAttrSetter mViewAttrSetter;
    protected PageContext mPageContext;
    protected int mId;

    protected int mVLeft;
    protected int mVTop;
    private Object mData;
    protected IBean mBean;

    public static class Builder implements IBuilder {
        @Override
        public String getName() {
            return "Page";
        }

        @Override
        public IView build(PageContext pageContext, ViewCache viewCache) {
            return new Page(pageContext, viewCache);
        }
    }

    public Page(PageContext pageContext, ViewCache viewCache) {
        super(pageContext.getContext());

        mAdapter = new ArrayAdapter(pageContext);
        mViewAttrSetter = new ViewAttrSetter(pageContext, this, viewCache);
        mCurPos = 0;

        mAutoSwitchHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                if (MSG_AUTO_SWITCH == msg.what) {
                    autoSwitch();
                }
            }
        };

        mMaxVelocity = ViewConfiguration.getMaximumFlingVelocity();
    }

    @Override
    public boolean setIntValue(int key, int value) {
        boolean ret = true;
        switch (key) {
            case StringTab.STR_ID_orientation:
                setOrientation(value == Common.ORIENTATION_HORIZONTAL ? true : false);
                break;
            case StringTab.STR_ID_autoSwitch:
                setAutoSwitch(value == 0 ? false : true);
                break;
            case StringTab.STR_ID_canSlide:
                setSlide(value == 0 ? false : true);
                break;
            case StringTab.STR_ID_animatorTime:
                setAnimatorTimeInterval(value);
                break;
            case StringTab.STR_ID_autoSwitchTime:
                setAutoSwitchTimeInterval(value);
                break;

            default:
                ret = mViewAttrSetter.setIntValue(key, value);
        }
        return ret;
    }

    @Override
    public boolean setFloatValue(int key, float value) {
        return mViewAttrSetter.setFloatValue(key, value);
    }

    @Override
    public boolean setStrValue(int key, String value) {
        return mViewAttrSetter.setStrValue(key, value);
    }

    @Override
    public void reset() {
//        setPadding(0, 0, 0, 0);
//        setVisibility(View.VISIBLE);
//
//        ViewGroup.MarginLayoutParams lp = this.getVLayoutParams();
//        if (null != lp && lp instanceof IAttr) {
//            ((IAttr)lp).reset();
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
        if (mData != data) {
            mAdapter.setData(data);
            mData = data;
        }
    }

    @Override
    public boolean setStyle(int keyId, Object value) {
        boolean ret = true;
        switch (keyId) {
            default:
                ret = mViewAttrSetter.setStyle(keyId, value);
        }
        return ret;
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

    @Override
    public int getVLeft() {
        return mVLeft;
    }

    @Override
    public int getVTop() {
        return mVTop;
    }

    @Override
    public void vMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public int getVMeasuredWidth() {
        return getMeasuredWidth();
    }

    @Override
    public int getVMeasuredHeight() {
        return getMeasuredHeight();
    }

    @Override
    public int getVMeasuredWidthWithMargin() {
        MarginLayoutParams p = this.getVLayoutParams();
        return this.getMeasuredWidth() + p.leftMargin + p.rightMargin;
    }

    @Override
    public int getVMeasuredHeightWithMargin() {
        MarginLayoutParams p = this.getVLayoutParams();
        return this.getMeasuredHeight() + p.topMargin + p.bottomMargin;
    }

    @Override
    public void vLayout(int left, int top, int right, int bottom) {
        mVLeft = left;
        mVTop = top;

        layout(left, top, right, bottom);
    }

    @Override
    public boolean isGone() {
        return (getVisibility() == GONE);
    }

    @Override
    public void setVLayoutParams(MarginLayoutParams param) {
        this.setLayoutParams(param);
    }

    @Override
    public void setComponentData(Object data) {
        mViewAttrSetter.setComponentData(data);
    }

    @Override
    public void setVMeasuredDimension(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public MarginLayoutParams getVLayoutParams() {
        return (MarginLayoutParams)this.getLayoutParams();
    }

    @Override
    public void onLoadFinished() {
    }

    @Override
    public void release() {
    }

    public void setAdapter(Adapter adapter) {
        mAdapter = adapter;
    }

    public void setListener(Listener lis) {
        mListener = lis;
    }

    public void setSlide(boolean slide) {
        mCanSlide = slide;
    }

    public void setAutoSwitchTimeInterval(int time) {
        mAutoSwitchTimeInterval = time;
    }

    public void setAnimatorTimeInterval(int time) {
        mAnimatorTimeInterval = time;
    }

    public void setStayTime(int time) {
        mStayTime = time;
    }

    public void setOrientation(boolean isHorizontal) {
        mIsHorizontal = isHorizontal;
    }

    public void setAutoSwitch(boolean auto) {
        mAutoSwitch = auto;
    }

    public void autoSwitch() {
        mIsNext = true;

        ObjectAnimator ani;
        if (mIsHorizontal) {
            ani = ObjectAnimator.ofInt(this, "scrollX", 0, this.getMeasuredWidth());
        } else {
            ani = ObjectAnimator.ofInt(this, "scrollY", 0, this.getMeasuredHeight());
        }
        ani.setDuration(mAutoSwitchTimeInterval).addListener(mAniListener);
        ani.start();
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);

        if (visibility != View.VISIBLE) {
            mCanSwitch = false;
            mAutoSwitchHandler.removeMessages(MSG_AUTO_SWITCH);
        } else {
            mCanSwitch = true;
            if (mAutoSwitch && mAdapter.getItemCount() > 1) {
                mAutoSwitchHandler.removeMessages(MSG_AUTO_SWITCH);
                mAutoSwitchHandler.sendEmptyMessageDelayed(MSG_AUTO_SWITCH, mStayTime);
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mCanSwitch = false;
        mAutoSwitchHandler.removeMessages(MSG_AUTO_SWITCH);
    }

    public void refresh() {
        mCanSwitch = true;

        if (mDataChanged) {
            removeAll();
            mDataChanged = false;

            initData();
        }

        if (mAutoSwitch && mAdapter.getItemCount() > 1) {
            mAutoSwitchHandler.removeMessages(MSG_AUTO_SWITCH);
            mAutoSwitchHandler.sendEmptyMessageDelayed(MSG_AUTO_SWITCH, mStayTime);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        refresh();
    }

    private void initData() {
        mCurPos = 0;

        int count = mAdapter.getItemCount();
        if (1 == count) {
            add(mCurPos);
        } else if (count > 1) {
            int pre = (mCurPos - 1);
            if (pre < 0) {
                pre += count;
            }
            int next = (mCurPos + 1) % count;
            add(pre);
            add(mCurPos);
           add(next);
        }

        if (count > 0 && null != mListener) {
            mListener.onPageFlip(1, count);
        }
    }

    protected void add(int pos) {
        add(pos, -1);
    }

    protected void add(int pos, int index) {
//        LogHelper.d(TAG, "add :" + Thread.currentThread().getName());
        int type = mAdapter.getType(pos);
        List<Adapter.ViewHolder> items = mItemCache.get(type);
        Adapter.ViewHolder vh;
        if (null != items && items.size() > 0) {
            vh = items.remove(0);
        } else {
            vh = mAdapter.onCreateViewHolder(type);
            vh.mType = type;
        }
        mAdapter.onBindViewHolder(vh, pos);
        if (index < 0) {
            this.addView(vh.mItemView);
        } else {
            this.addView(vh.mItemView, index);
        }
    }

    private void acquireVelocityTracker(final MotionEvent event) {
        if (null == mVelocityTracker) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    private void releaseVelocityTracker() {
        if (null != mVelocityTracker) {
            mVelocityTracker.clear();
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean ret = false;

        if (mCanSlide) {
            int x = (int) ev.getX();
            int y = (int) ev.getY();
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (mIsHorizontal) {
                        mDownPos = x;
                    } else {
                        mDownPos = y;
                    }
                    mLastX = x;
                    mLastY = y;
                    mPointerId = ev.getPointerId(0);
                    break;

                case MotionEvent.ACTION_MOVE:
                    int deltaX = x - mLastX;
                    int deltaY = y - mLastY;

                    if (mIsHorizontal) {
                        if (Math.abs(deltaX) > Math.abs(deltaY)) {
                            this.getParent().requestDisallowInterceptTouchEvent(true);
                            ret = true;
                        }
                    } else {
                        if (Math.abs(deltaY) > Math.abs(deltaX)) {
                            this.getParent().requestDisallowInterceptTouchEvent(true);
                            ret = true;
                        }
                    }
                    break;

                case MotionEvent.ACTION_UP:
                    break;
            }
        }

        return ret;
    }

    private void moveV(MotionEvent event) {
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownPos = y;
                break;

            case MotionEvent.ACTION_MOVE:
                int offset = y - mDownPos;
                this.setScrollY(-offset);
                break;

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mVelocityTracker.computeCurrentVelocity(1000, mMaxVelocity);
                final float velocityY = mVelocityTracker.getYVelocity(mPointerId);

                // animator
                int scrollY = this.getScrollY();
                int h = this.getMeasuredHeight();
                int dis = Math.abs(scrollY);
                if (dis > h / 2 || Math.abs(velocityY) > VEL_THRESHOLD) {
                    ObjectAnimator ani;
                    if (scrollY < 0) {
                        mIsNext = false;
                        ani = ObjectAnimator.ofInt(this, "scrollY", scrollY, -h);
                    } else {
                        mIsNext = true;
                        ani = ObjectAnimator.ofInt(this, "scrollY", scrollY, h);
                    }
                    ani.setDuration(mAnimatorTimeInterval).addListener(mAniListener);
                    ani.start();
                } else {
                    ObjectAnimator.ofInt(this, "scrollY", scrollY, 0).setDuration(mAnimatorTimeInterval).start();
                }

                releaseVelocityTracker();
                break;
        }
    }

    private void moveH(MotionEvent event) {
        int x = (int) event.getX();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownPos = x;
                break;

            case MotionEvent.ACTION_MOVE:
                int offset = x - mDownPos;
                this.setScrollX(-offset);
                break;

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mVelocityTracker.computeCurrentVelocity(1000, mMaxVelocity);
                final float velocityX = mVelocityTracker.getXVelocity(mPointerId);
                final float velocityY = mVelocityTracker.getYVelocity(mPointerId);

                // animator
                int scrollX = this.getScrollX();
                int w = this.getMeasuredWidth();
                int dis = Math.abs(scrollX);
                if (dis > w / 2 || Math.abs(velocityX) > VEL_THRESHOLD) {
                    ObjectAnimator ani;
                    if (scrollX < 0) {
                        mIsNext = false;
                        ani = ObjectAnimator.ofInt(this, "scrollX", scrollX, -w);
                    } else {
                        mIsNext = true;
                        ani = ObjectAnimator.ofInt(this, "scrollX", scrollX, w);
                    }
                    ani.setDuration(mAnimatorTimeInterval).addListener(mAniListener);
                    ani.start();
                } else {
                    ObjectAnimator.ofInt(this, "scrollX", scrollX, 0).setDuration(mAnimatorTimeInterval).start();
                }

                releaseVelocityTracker();
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        acquireVelocityTracker(event);

        if (mIsHorizontal) {
            moveH(event);
        } else {
            moveV(event);
        }

        return true;
    }

    protected void removeAll() {
        int count = this.getChildCount();
        for (int i = 0; i < count; ++i) {
            removeData(i);
        }

        this.removeAllViews();
    }

    private void removeData(int index) {
        View v = this.getChildAt(index);

        Adapter.ViewHolder vh = (Adapter.ViewHolder) v.getTag();
//        ((Container)vh.mItemView).getVirtualView().reset();
        List<Adapter.ViewHolder> items = mItemCache.get(vh.mType);
        if (null == items) {
            items = new ArrayList<>();
            mItemCache.put(vh.mType, items);
        }

        // reset
        ((IView)vh.mItemView).reset();

        if (items.size() >= MAX_ITEM_COUNT) {
            // reset
            items.remove(0);
        }
        items.add(vh);
    }

    private void remove(int index) {
//        LogHelper.d(TAG, "remove:" + Thread.currentThread().getName());
        removeData(index);
        this.removeViewAt(index);
    }

    private void changeChildren() {
        // remove old item
        int count = mAdapter.getItemCount();
        if (count <= 0 || this.getChildCount() <= 0) {
            return;
        }

        if (mIsNext) {
            remove(0);

            mCurPos = (mCurPos + 1) % count;

            int pos = (mCurPos + 1) % count;
            add(pos);
        } else {
            remove(this.getChildCount() - 1);

            mCurPos -= 1;
            if (mCurPos < 0) {
                mCurPos += count;
            }

            int pos = (mCurPos - 1);
            if (pos < 0) {
                pos += count;
            }
            add(pos, 0);
        }

        this.requestLayout();
        if (mIsHorizontal) {
            this.setScrollX(0);
        } else {
            this.setScrollY(0);
        }

        if (mAutoSwitch) {
            mAutoSwitchHandler.removeMessages(MSG_AUTO_SWITCH);
            if (mCanSwitch) {
                mAutoSwitchHandler.sendEmptyMessageDelayed(MSG_AUTO_SWITCH, mStayTime);
            }
        }
    }

    class MyAnimatorListener implements Animator.AnimatorListener {

        @Override
        public void onAnimationStart(Animator animator) {
        }

        @Override
        public void onAnimationEnd(Animator animator) {
            changeChildren();

            if (null != mListener) {
                mListener.onPageFlip(mCurPos + 1, mAdapter.getItemCount());
            }
        }

        @Override
        public void onAnimationCancel(Animator animator) {
        }

        @Override
        public void onAnimationRepeat(Animator animator) {
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        this.measureChildren(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = this.getChildCount();
        int w = r - l;
        int h = b - t;
        int offset = 0;

        if (mIsHorizontal) {
            if (count > 1) {
                offset = -w;
            }
            for (int i = 0; i < count; ++i) {
                View child = this.getChildAt(i);
                child.layout(offset, 0, offset + w, h);
                offset += w;
            }
        } else {
            if (count > 1) {
                offset = -h;
            }
            for (int i = 0; i < count; ++i) {
                View child = this.getChildAt(i);
                child.layout(0, offset, w, offset + h);
                offset += h;
            }
        }
    }
}
