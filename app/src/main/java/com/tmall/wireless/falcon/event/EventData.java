package com.tmall.wireless.falcon.event;

import com.tmall.wireless.falcon.PageContext;
import com.tmall.wireless.falcon.core.IView;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by gujicheng on 16/12/28.
 */

public class EventData {
    protected static List<EventData> sCache = new LinkedList<>();

    public IView mEventSource;
    public PageContext mContext;

    public static void clear() {
        sCache.clear();
    }

    public EventData(PageContext context, IView source) {
        mContext = context;
        mEventSource = source;
    }

    public void recycle() {
        recycleData(this);
        mEventSource = null;
        mContext = null;
    }

    public static EventData obtainData(PageContext context, IView source) {
        EventData ret;
        if (sCache.size() > 0) {
            ret = sCache.remove(0);

            ret.mEventSource = source;
            ret.mContext = context;
        } else {
            ret = new EventData(context, source);
        }
        return ret;
    }

    protected static void recycleData(EventData data) {
        if (null != data) {
            sCache.add(data);
        }
    }
}
