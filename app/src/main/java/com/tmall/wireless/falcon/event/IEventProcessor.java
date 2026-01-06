package com.tmall.wireless.falcon.event;

/**
 * Created by gujicheng on 16/12/28.
 */

public interface IEventProcessor {
    boolean process(EventData data);
}
