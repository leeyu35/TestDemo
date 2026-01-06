package com.tmall.wireless.falcon.core;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;

import com.libra.common.Common;
import com.libra.common.LogHelper;
import com.tmall.wireless.falcon.PageContext;
import com.tmall.wireless.falcon.common.Utils;

/**
 * Created by gujicheng on 17/5/18.
 */

public class StubView extends ViewGroup implements IView {
    private final static String TAG = "StubView";

    private final static int STATE_RECYCLED = 1;
    private final static int STATE_CREATED = 2;
    private final static int STATE_BINDED = 3;
    private final static int STATE_MEASURED = 4;

    private final static int DEFAULT_WH = 500;

    private final static int MSG_NOTIFY_CREATE_FINISH = 1;
    private final static int MSG_NOTIFY_BIND_FINISH = 2;
    private final static int MSG_NOTIFY_MEASURE_FINISH = 3;

    private Container mContainer;
    private int mComponentType;
    private PageContext mPageContext;
    private AsyncWork mAsyncWork;
    protected int mComponentId;

    private int mMeasuredWidthMeasureSpec = -1;
    private int mMeasuredHeightMeasureSpec = -1;

    private int mMeasuredWidth = -1;
    private int mMeasuredHeight = -1;

    protected Handler mHandler;

    private volatile int mState = STATE_RECYCLED;

    private int mComponentWidth;
    private int mComponentHeight;
    private int mAutoDimDirection;
    private float mAutoDimX;
    private float mAutoDimY;

    public StubView(PageContext pageContext, int type, int componentWidth, int componentHeight, int autoDimDirection, float autoDimX, float autoDimY) {
        super(pageContext.getContext());

        mComponentWidth = componentWidth;
        mComponentHeight = componentHeight;
        mAutoDimDirection = autoDimDirection;
        mAutoDimX = autoDimX;
        mAutoDimY = autoDimY;

        mPageContext = pageContext;
        mAsyncWork = pageContext.getAsyncWorkManager().mallocWork();
        mComponentType = type;

        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MSG_NOTIFY_CREATE_FINISH:
                        doCreateFinish();
                        break;

                    case MSG_NOTIFY_BIND_FINISH:
                        doBindFinish();
                        break;

                    case MSG_NOTIFY_MEASURE_FINISH:
                        doMeasureFinish();
                        break;
                }
            }
        };

        reset();

        mAsyncWork.asyncCreate(this, mComponentType);
    }

    private void doCreateFinish() {
        if (STATE_RECYCLED == mState) {
            mState = STATE_CREATED;
        }
    }

    public void notifyCreateFinish() {
        mHandler.sendEmptyMessage(MSG_NOTIFY_CREATE_FINISH);
    }

    private void doBindFinish() {
        if (STATE_RECYCLED == mState || STATE_CREATED == mState) {
            mState = STATE_BINDED;
        }
    }

    public void notifyBindFinish() {
        mHandler.sendEmptyMessage(MSG_NOTIFY_BIND_FINISH);
    }

    private void doMeasureFinish() {
        if(STATE_RECYCLED != mState) {
            mState = STATE_MEASURED;
            mMeasuredWidth = mContainer.getVMeasuredWidth();
            mMeasuredHeight = mContainer.getVMeasuredHeight();
            if (getChildCount() <= 0) {
                this.addView(mContainer);
            } else {
                this.requestLayout();
            }
        }
    }

    public void notifyMeasureFinish() {
        mHandler.sendEmptyMessage(MSG_NOTIFY_MEASURE_FINISH);
    }

    public Container getContainer() {
        return mContainer;
    }

    public void setContainer(Container container) {
        mContainer = container;
    }

    public PageContext getPageContext() {
        return mPageContext;
    }

    @Override
    public void reset() {
        mState = STATE_RECYCLED;
        mMeasuredWidthMeasureSpec = -1;
        mMeasuredHeightMeasureSpec = -1;

        mMeasuredWidth = -1;
        mMeasuredHeight = -1;

        if (this.getChildCount() > 0) {
            ((IView)this.getChildAt(0)).reset();
        }

        this.removeAllViews();
    }

    public IView getView() {
        return mContainer;
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

        if (mMeasuredWidthMeasureSpec != widthMeasureSpec || mMeasuredHeightMeasureSpec != heightMeasureSpec) {
            // should asyncMeasure now!
            if (getChildCount() > 0) {
                LogHelper.d(TAG, "onMeasure need remove");
                removeAllViews();
            }
            mAsyncWork.asyncMeasure(this, widthMeasureSpec, heightMeasureSpec);
            mMeasuredWidthMeasureSpec = widthMeasureSpec;
            mMeasuredHeightMeasureSpec = heightMeasureSpec;
        }

        if (mMeasuredWidth <= -1) {
            mMeasuredWidth = DEFAULT_WH;
            widthMeasureSpec = AsyncWork.getMeasureSpec(mComponentWidth, widthMeasureSpec);
            if (MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.UNSPECIFIED) {
                mMeasuredWidth = MeasureSpec.getSize(widthMeasureSpec);
            }
        }

        if (mMeasuredHeight <= -1) {
            mMeasuredHeight = DEFAULT_WH;
            heightMeasureSpec = AsyncWork.getMeasureSpec(mComponentHeight, heightMeasureSpec);
            if (MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.UNSPECIFIED) {
                mMeasuredHeight = MeasureSpec.getSize(heightMeasureSpec);
            }
        }
        setMeasuredDimension(mMeasuredWidth, mMeasuredHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (1 == getChildCount()) {
            View child = this.getChildAt(0);
            Utils.setFlag(child, Utils.PFLAG_LAYOUT_REQUIRED);
            child.layout(0, 0, child.getMeasuredWidth(), child.getMeasuredHeight());
        }
    }

    @Override
    public int getComponentId() {
        if (null != mContainer) {
            return mContainer.getComponentId();
        } else {
            LogHelper.e(TAG, "getComponentId view is null");
        }
        return -1;
    }

    @Override
    public void setComponentId(int id) {
        mComponentId = id;
    }

    @Override
    public int getVId() {
        return -1;
    }

    @Override
    public void setVId(int id) {
    }

    @Override
    public void setData(Object data) {

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

    public void vLayout(int l, int t, int r, int b) {
        layout(l, t, r, b);
    }

    @Override
    public boolean isGone() {
        return false;
    }

    @Override
    public void setVMeasuredDimension(int widthMeasureSpec, int heightMeasureSpec) {
    }

    @Override
    public MarginLayoutParams getVLayoutParams() {
        return null;
    }

    @Override
    public void setVLayoutParams(MarginLayoutParams param) {
        this.setLayoutParams(param);
    }

    @Override
    public void setComponentData(Object data) {
        mAsyncWork.asyncBind(this, data);
    }

    @Override
    public void onLoadFinished() {
    }

    @Override
    public void release() {
    }

    @Override
    public boolean setIntValue(int key, int value) {
        return false;
    }

    @Override
    public boolean setFloatValue(int key, float value) {
        return false;
    }

    @Override
    public boolean setStrValue(int key, String value) {
        return false;
    }
}