package com.tmall.wireless.falcon.event;

import com.libra.common.LogHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gujicheng on 16/12/28.
 */

public class EventManager {
    private final static String TAG = "EventManager_TMTEST";

    public final static int TYPE_Click = 0;
    public final static int TYPE_Exposure = 1;
    public final static int TYPE_Load = 2;
    public final static int TYPE_FlipPage = 3;
    public final static int TYPE_COUNT = 4;

    private Object[] mProcessor = new Object[TYPE_COUNT];

    public void register(int type, IEventProcessor processor) {
        if (null != processor && type >= 0 && type < TYPE_COUNT) {
            List<IEventProcessor> pList = (List<IEventProcessor>)mProcessor[type];
            if (null == pList) {
                pList = new ArrayList<>();
                mProcessor[type] = pList;
            }

            pList.add(processor);
        } else {
            LogHelper.e(TAG, "register failed type:" + type + "  processor:" + processor);
        }
    }

    public void unregister(int type, IEventProcessor processor) {
        if (null != processor && type >= 0 && type < TYPE_COUNT) {
            List<IEventProcessor> pList = (List<IEventProcessor>)mProcessor[type];
            if (null != pList) {
                pList.remove(processor);
            } else {
                LogHelper.e(TAG, "unregister failed");
            }
        } else {
            LogHelper.e(TAG, "unregister failed type:" + type + "  processor:" + processor);
        }
    }

    public boolean emitEvent(int type, EventData data) {
        boolean ret = false;
        if (type >= 0 & type < TYPE_COUNT) {
            List<IEventProcessor> pList = (List<IEventProcessor>)mProcessor[type];
            if (null != pList) {
                for (IEventProcessor p : pList) {
                    ret = p.process(data);
                }
            }
        }

        if (null != data) {
            data.recycle();
        }

        return ret;
    }
}
