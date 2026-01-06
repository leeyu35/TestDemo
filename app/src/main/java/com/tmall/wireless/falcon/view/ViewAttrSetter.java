package com.tmall.wireless.falcon.view;

import android.view.View;
import android.view.ViewGroup;

import com.libra.common.LogHelper;
import com.libra.common.StringTab;
import com.tmall.wireless.falcon.PageContext;
import com.tmall.wireless.falcon.common.Const;
import com.tmall.wireless.falcon.common.Utils;
import com.tmall.wireless.falcon.core.IAttr;
import com.tmall.wireless.falcon.core.IView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by gujicheng on 17/5/11.
 */

public class ViewAttrSetter {
    private final static String TAG = "ViewAttrSetter";

    private View mView;
    private ViewCache mViewCache;
    private PageContext mPageContext;

    private int mPaddingLeft = 0;
    private int mPaddingTop = 0;
    private int mPaddingRight = 0;
    private int mPaddingBottom = 0;

    public ViewAttrSetter(PageContext pageContext, View v, ViewCache viewCache) {
        mView = v;
        mPageContext = pageContext;
        mViewCache = viewCache;
    }

    public void setComponentData(Object data) {
        if (null != data && data instanceof JSONObject) {
            JSONObject objData = (JSONObject)data;
            JSONArray itemArr = objData.optJSONArray(Const.DATA_ATTRS);
            if (null != itemArr) {
                StringTab st = mPageContext.getStringTab();

                int size = itemArr.length();
                for (int i = 0; i < size; ++i) {
                    JSONObject item = itemArr.optJSONObject(i);
                    if (null != item) {
                        String rawId = item.optString(Const.DATA_ID);
                        int id = st.getStringId(rawId);
                        if (id > -1) {
                            IView v = mViewCache.get(id);
                            if (null != v) {
                                v.setRawData(item);

                                Object obj = item.opt(Const.DATA_DATA);
                                if (null != obj) {
                                    v.setData(obj);
                                }

                                JSONObject styleObj = item.optJSONObject(Const.DATA_STYLE);
                                if (null != styleObj) {
                                    Iterator<String> it = styleObj.keys();
                                    while (it.hasNext()) {
                                        String key = it.next();
                                        Object value = styleObj.opt(key);
                                        int keyId = st.getStringId(key);
                                        if (!v.setStyle(keyId, value)) {
                                            ViewGroup.MarginLayoutParams lp = v.getVLayoutParams();
                                            if (null != lp && lp instanceof IAttr) {
                                                ((IAttr)lp).setStyle(keyId, value);
                                            }
                                        }
                                    }
                                }

                                obj = item.opt(Const.DATA_AUXILIARY);
                                if (null != obj) {
                                    v.setAuxiliary(obj);
                                }
                            }
                        }
                    }
                }
            }
        } else {
            LogHelper.e(TAG, "data invalidate:"  + data);
        }
    }

    public void release() {
        mView = null;
        mPageContext = null;
    }

    public boolean setIntValue(int key, int value) {
        boolean ret = true;

        switch (key) {
            case StringTab.STR_ID_paddingLeft:
                mPaddingLeft = value;
                mView.setPadding(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom);
                break;

            case StringTab.STR_ID_paddingTop:
                mPaddingTop = value;
                mView.setPadding(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom);
                break;

            case StringTab.STR_ID_paddingRight:
                mPaddingRight = value;
                mView.setPadding(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom);
                break;

            case StringTab.STR_ID_paddingBottom:
                mPaddingBottom = value;
                mView.setPadding(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom);
                break;

            case StringTab.STR_ID_backgroundColor:
                mView.setBackgroundColor(value);
                break;

            case StringTab.STR_ID_visibility:
                mView.setVisibility(value);
                break;

            case StringTab.STR_ID_autoDimDirection:
                ((IView)mView).setAutoDimDirection(value);
                break;
            case StringTab.STR_ID_autoDimX:
                ((IView)mView).setAutoDimX(value);
                break;
            case StringTab.STR_ID_autoDimY:
                ((IView)mView).setAutoDimY(value);
                break;

            case StringTab.STR_ID_id:
                ((IView) mView).setVId(value);
                break;

            default:
                ret = false;
        }
        return ret;
    }

    public boolean setFloatValue(int key, float value) {
        boolean ret = true;

        switch (key) {
            case StringTab.STR_ID_paddingLeft:
                mPaddingLeft = (int)value;
                mView.setPadding(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom);
                break;

            case StringTab.STR_ID_paddingTop:
                mPaddingTop = (int)value;
                mView.setPadding(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom);
                break;

            case StringTab.STR_ID_paddingRight:
                mPaddingRight = (int)value;
                mView.setPadding(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom);
                break;

            case StringTab.STR_ID_paddingBottom:
                mPaddingBottom = (int)value;
                mView.setPadding(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom);
                break;

            case StringTab.STR_ID_autoDimX:
                ((IView)mView).setAutoDimX(value);
                break;
            case StringTab.STR_ID_autoDimY:
                ((IView)mView).setAutoDimY(value);
                break;
            default:
                ret = false;
        }

        return ret;
    }

    public boolean setStrValue(int key, String value) {
        boolean ret = true;
        switch (key) {
            case StringTab.STR_ID_background:
                mPageContext.getApplicationContext().setBackground(value, mView);
                break;

            case StringTab.STR_ID_dataSource:
                mPageContext.getApplicationContext().loadData(value, (IView) mView, mPageContext.getStringTab());
                break;

            case StringTab.STR_ID_bean:
                ((IView) mView).setBean(Utils.parseBean(this.getClass().getClassLoader(), value));
                break;
            default:
                ret = false;
        }
        return ret;
    }

    public boolean setStyle(int key, Object value) {
        boolean ret = true;

        switch (key) {
            case StringTab.STR_ID_padding:
                if (value instanceof JSONArray) {
                    JSONArray arr = (JSONArray)value;
                    if (arr.length() == 4) {
                        mPaddingLeft = arr.optInt(0);
                        mPaddingTop = arr.optInt(1);
                        mPaddingRight = arr.optInt(2);
                        mPaddingBottom = arr.optInt(3);
                        mView.setPadding(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom);
                    }
                }
                break;

            case StringTab.STR_ID_paddingLeft:
                mPaddingLeft = (Integer) value;
                mView.setPadding(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom);
                break;

            case StringTab.STR_ID_paddingTop:
                mPaddingTop = (Integer) value;
                mView.setPadding(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom);
                break;

            case StringTab.STR_ID_paddingRight:
                mPaddingRight = (Integer) value;
                mView.setPadding(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom);
                break;

            case StringTab.STR_ID_paddingBottom:
                mPaddingBottom = (Integer) value;
                mView.setPadding(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom);
                break;

            case StringTab.STR_ID_backgroundColor:
                mView.setBackgroundColor((Integer) value);
                break;

            case StringTab.STR_ID_visibility:
                mView.setVisibility((Integer) value);
                break;

            case StringTab.STR_ID_autoDimDirection:
                ((IView)mView).setAutoDimDirection((Integer)value);
                break;
            case StringTab.STR_ID_autoDimX:
                ((IView)mView).setAutoDimX((Float)value);
                break;
            case StringTab.STR_ID_autoDimY:
                ((IView)mView).setAutoDimY((Float)value);
                break;

            case StringTab.STR_ID_background:
                mPageContext.getApplicationContext().setBackground((String)value, mView);
                break;

            default:
                ret = false;
        }
        return ret;
    }
}
