package com.tmall.wireless.falcon.core;

/**
 * Created by gujicheng on 17/5/25.
 */

public interface IBean {
    void init();
    void afterBindData(IView view, Object data);
    void destroy();
    boolean onClick(IView view);
}
