package com.tmall.wireless.falcon.core;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;

import com.libra.common.LogHelper;
import com.tmall.wireless.falcon.PageContext;
import com.tmall.wireless.falcon.common.Utils;
import com.tmall.wireless.falcon.layout.libra.AbsoluteLayout;

/**
 * Created by gujicheng on 17/5/18.
 */

public class AsyncWork {
    private final static String TAG = "AsyncWork";

    private final static int MSG_CREATE = 0;
    private final static int MSG_BIND = 1;
    private final static int MSG_MEASURE = 2;

    private final static int STATE_Start = 1;
    private final static int STATE_Stop = 2;

    private HandlerThread mWorkHandlerThread;
    private Handler mHandler;
    private volatile int mState = STATE_Start;

    public AsyncWork() {
        mWorkHandlerThread = new HandlerThread("async_measure_layout");
        mWorkHandlerThread.start();

        mHandler = new Handler( mWorkHandlerThread.getLooper() ) {
            @Override
            public void handleMessage(Message msg) {
                if (STATE_Start == mState) {
                    switch (msg.what) {
                        case MSG_CREATE:
                            doCreate((StubView)msg.obj, msg.arg1);
                            break;

                        case MSG_BIND:
                            BindData bd = (BindData)msg.obj;
                            doBind(bd.mStub, bd.mData);
                            break;

                        case MSG_MEASURE:
                            doMeasure((StubView)msg.obj, msg.arg1, msg.arg2);
                            break;
                    }
                }
            }
        };
    }

    public synchronized void release() {
        mState = STATE_Stop;
        reset();
        mWorkHandlerThread.quit();
        mHandler = null;
    }

    public void reset() {
        mHandler.removeMessages(MSG_CREATE);
        mHandler.removeMessages(MSG_BIND);
        mHandler.removeMessages(MSG_MEASURE);
    }

    private synchronized void doCreate(StubView stub, int type) {
        if (STATE_Start == mState) {
            PageContext pageContext = stub.getPageContext();
            IView view = pageContext.newComponentFromLibrary(type);
            if (null != view) {
                Container container;
                if (view instanceof Container) {
                    container = (Container)view;
                } else {
                    container = new Container(pageContext);
                    container.attachView(view);
                }

                stub.setContainer(container);

                IBean bean = container.getBean();
                if (null != bean) {
                    bean.init();
                }

                if (STATE_Start == mState) {
                    synchronized (this) {
                        if (STATE_Start == mState) {
                            stub.notifyCreateFinish();
                        }
                    }
                }
            } else {
                LogHelper.e(TAG, "doCreate failed:" + type);
            }
        }
    }

    private void doBind(StubView stub, Object data) {
        // bind data
        IView container = stub.getContainer();
        if (null != container) {
            container.setComponentData(data);

            // data process
            IBean bean = container.getBean();
            if (null != bean) {
                bean.afterBindData(container, data);
            }

            Utils.sleep();

            // notify main thread, bind finished
            if (STATE_Start == mState) {
                synchronized (this) {
                    if (STATE_Start == mState) {
                        stub.notifyBindFinish();
                    }
                }
            }
        }
    }

    private void doMeasure(StubView c, int widthMeasureSpec, int heightMeasureSpec) {
        // asyncMeasure and layout
//        int width = View.MeasureSpec.getSize(widthMeasureSpec);
//        int height = View.MeasureSpec.getSize(heightMeasureSpec);
//
//        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
//        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
//
//        LogHelper.d(TAG, "doMeasure width:" + width + "  widthMode:" + widthMode + "  height:" + height + "  heightMode:" + heightMode);

        Utils.sleep();

//        LogHelper.d(TAG, "measure component:" + c.getPageContext().getStringTab().getString(c.getComponentId()));
//        long s = System.currentTimeMillis();
        Container container = c.getContainer();
        if (null != container) {
            IView view = container.getView();
            ViewGroup.LayoutParams lp = view.getVLayoutParams();
            container.vMeasure(getMeasureSpec(lp.width, widthMeasureSpec), getMeasureSpec(lp.height, heightMeasureSpec));
//            container.setVMeasuredDimension(view.getVMeasuredWidth(), view.getVMeasuredHeight());
            view.vLayout(0, 0, view.getVMeasuredWidth(), view.getVMeasuredHeight());

            if (STATE_Start == mState) {
                synchronized (this) {
                    if (STATE_Start == mState) {
                        c.notifyMeasureFinish();
                    }
                }
            }
        }
//        LogHelper.d(TAG, "asyncMeasure time:" + (System.currentTimeMillis() - s));
    }

    public static int getMeasureSpec(int size, int measureSpec) {
        int s = View.MeasureSpec.getSize(measureSpec);
//        int mode = View.MeasureSpec.getMode(measureSpec);
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
        } else {
            return View.MeasureSpec.makeMeasureSpec(size, View.MeasureSpec.EXACTLY);
        }
    }

    public synchronized void asyncCreate(StubView c, int comType) {
        if (STATE_Start == mState) {
            mHandler.obtainMessage(MSG_CREATE, comType, 0, c).sendToTarget();
        }
    }

    public synchronized void asyncBind(StubView c, Object obj) {
        if (STATE_Start == mState) {
            mHandler.obtainMessage(MSG_BIND, new BindData(c, obj)).sendToTarget();
        }
    }

    public synchronized void asyncMeasure(StubView c, int widthMeasureSpec, int heightMeasureSpec) {
        if (STATE_Start == mState) {
            mHandler.obtainMessage(MSG_MEASURE, widthMeasureSpec, heightMeasureSpec, c).sendToTarget();
        }
    }

    private static class BindData {
        public StubView mStub;
        public Object mData;

        public BindData(StubView stub, Object data) {
            mStub = stub;
            mData = data;
        }
    }
}
