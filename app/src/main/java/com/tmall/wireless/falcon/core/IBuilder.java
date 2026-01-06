package com.tmall.wireless.falcon.core;

import com.tmall.wireless.falcon.PageContext;
import com.tmall.wireless.falcon.view.ViewCache;

/**
 * Created by gujicheng on 17/5/10.
 */

public interface IBuilder {
    String getName();
    IView build(PageContext pageContext, ViewCache viewCache);
}
