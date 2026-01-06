package com.tmall.wireless.falcon.event;

import android.text.TextUtils;

import com.tmall.wireless.falcon.ApplicationContext;
import com.tmall.wireless.falcon.common.Const;
import com.tmall.wireless.falcon.core.IView;

import org.json.JSONObject;

/**
 * Created by gujicheng on 17/5/27.
 */

public class DefaultClick implements DefaultClickProcessor.IClick {
    private final static String TAG = "DefaultClick";

    private ApplicationContext mAppContext;

    public DefaultClick(ApplicationContext appContext) {
        mAppContext = appContext;
    }

    @Override
    public boolean onClick(IView v) {
        boolean ret = false;
        Object rawData = v.getRawData();
        if (null != rawData && rawData instanceof JSONObject) {
            JSONObject obj = (JSONObject)rawData;
            JSONObject auxiliary = obj.optJSONObject(Const.DATA_AUXILIARY);
            if (null != auxiliary) {
                String action = auxiliary.optString(Const.DATA_ACTION);
                String actionParams = auxiliary.optString(Const.DATA_ACTION_PARAMS);
                if (!TextUtils.isEmpty(action)) {
                    ret = mAppContext.jump(action, actionParams);
                }
            }
        }
        return ret;
    }
}
