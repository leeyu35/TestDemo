package com.tmall.wireless.falcon.event;

import com.tmall.wireless.falcon.ApplicationContext;
import com.tmall.wireless.falcon.core.IView;

/**
 * Created by gujicheng on 17/5/27.
 */

public class DefaultClickProcessor {
    private final static String TAG = "DefaultClickProcessor";

    private IClick mClick;

    public DefaultClickProcessor(ApplicationContext appContext) {
        mClick = new DefaultClick(appContext);
    }

    public interface IClick {
        boolean onClick(IView v);
    }

    public void setDefaultClick(IClick click) {
        mClick = click;
    }

    public IClick getDefaultClick() {
        return mClick;
    }
}
