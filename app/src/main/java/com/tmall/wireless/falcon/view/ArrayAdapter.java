package com.tmall.wireless.falcon.view;

import android.view.View;

import com.libra.common.LogHelper;
import com.libra.common.StringTab;
import com.tmall.wireless.falcon.PageContext;
import com.tmall.wireless.falcon.common.Const;
import com.tmall.wireless.falcon.core.IView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by gujicheng on 16/12/19.
 */

public class ArrayAdapter extends Adapter {
    private final static String TAG = "ArrayAdapter";

    private JSONArray mData;
    private StringTab mStringTab;

    public ArrayAdapter(PageContext context) {
        super(context);
        mStringTab = context.getStringTab();
    }

    @Override
    public void setData(Object str) {
        if (null == str) {
            mData = null;
        } else if (str instanceof JSONArray) {
            mData = (JSONArray) str;
        } else {
            LogHelper.e(TAG, "setData failed:" + str);
        }
    }

    @Override
    public int getItemCount() {
        if (null != mData) {
            return mData.length();
        }
        return 0;
    }

    @Override
    public int getType(int pos) {
        if (null != mData) {
            try {
                JSONObject obj = mData.getJSONObject(pos);
                int type = obj.optInt(Const.TEMPLATE_INT, -1);
                if (type <= -1) {
                    type = mStringTab.getStringId(obj.optString(Const.TEMPLATE));
                    obj.put(Const.TEMPLATE_INT, type);
                }
                return type;
            } catch (JSONException e) {
            }
        }
        return 0;
    }

    @Override
    public void onBindViewHolder(Adapter.ViewHolder vh, int pos) {
        try {
            Object obj = mData.get(pos);

            if (obj instanceof JSONObject && vh.mItemView instanceof IView) {
                ((IView)vh.mItemView).setComponentData(obj);
            } else {
                LogHelper.e(TAG, "failed");
            }
        } catch (JSONException e) {
            LogHelper.e(TAG, "failed" + e);
            e.printStackTrace();
        }
    }

    @Override
    public Adapter.ViewHolder onCreateViewHolder(int viewType) {
        View v = (View)mPageContext.newComponent(viewType);
        if (null == v) {
            v = new View(mPageContext.getContext());
        }
        return new ViewHolder(v);
    }
}
