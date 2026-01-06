package com.tmall.wireless.falcon.view;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.libra.common.Common;
import com.libra.common.LogHelper;
import com.libra.common.StringTab;
import com.tmall.wireless.falcon.PageContext;
import com.tmall.wireless.falcon.core.IView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.tmall.wireless.falcon.common.Const.TEMPLATE;
import static com.tmall.wireless.falcon.common.Const.TEMPLATE_INT;

/**
 * Created by gujicheng on 16/8/24.
 */
public class ScrollerAdapter extends RecyclerView.Adapter<ScrollerAdapter.ViewHolder> {
    private final static String TAG = "ScrollerAdapter";

    private JSONArray mData;
    private PageContext mPageContext;
    private Scroller mScroller;
    private StringTab mStringTab;

    public ScrollerAdapter(PageContext pageContext, Scroller scroller) {
        mPageContext = pageContext;
        mScroller = scroller;
        mStringTab = pageContext.getStringTab();
    }

    public void destroy() {
        mData = null;
        mPageContext = null;
        mScroller = null;
        mStringTab = null;
    }

    public void setData(Object data) {
        if (null != data && data instanceof JSONArray) {
            mData = (JSONArray) data;
        } else {
            LogHelper.e(TAG, "setData failed:" + data);
        }
    }

    public void appendData(Object data) {
        if (data instanceof JSONArray) {
            JSONArray arr = (JSONArray) data;

            if (null == mData) {
                mData = arr;
                this.notifyDataSetChanged();
            } else {
                int startPos = mData.length();
                int len = arr.length();
                for (int i = 0; i < len; ++i) {
                    try {
                        mData.put(arr.get(i));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                this.notifyItemRangeChanged(startPos, len);
            }
        } else {
            LogHelper.e(TAG, "appendData failed:" + data);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        IView v = mPageContext.newComponent(viewType);

        if (null == v) {
            return new ViewHolder(new View(mScroller.getContext()));
        } else {
            StaggeredGridLayoutManager.LayoutParams lp = null;
            if (Common.SCROLLER_MODE_StaggeredGrid == mScroller.mMode) {
                ViewGroup.MarginLayoutParams p = v.getVLayoutParams();
                if (null != p) {
                    lp = new StaggeredGridLayoutManager.LayoutParams(p.width, p.height);
                } else {
                    lp = new StaggeredGridLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                }
                v.setVLayoutParams(lp);
            }
            return new ViewHolder((View)v);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int pos) {
        try {
            Object obj = mData.get(pos);
            viewHolder.itemView.setTag(pos);
//            LogHelper.d(TAG, "onBindViewHolder:" + obj);
            if (obj instanceof JSONObject && viewHolder.itemView instanceof IView) {
                JSONObject jObj = (JSONObject)obj;
                if (Common.SCROLLER_MODE_StaggeredGrid == mScroller.mMode) {
                    StaggeredGridLayoutManager.LayoutParams clp1 = (StaggeredGridLayoutManager.LayoutParams) viewHolder.itemView.getLayoutParams();
                    if (jObj.optInt(Common.SUPPORT_WATERFALL, -1) <= 0) {
                        clp1.setFullSpan(true);
                    } else {
                        clp1.setFullSpan(false);
                    }
                }

                ((IView)viewHolder.itemView).setComponentData(obj);
//            } else {
//                LogHelper.e(TAG, "failed");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            LogHelper.e(TAG, "onBindViewHolder:" + e);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (null != mData) {
            JSONObject obj = null;
            try {
                obj = mData.getJSONObject(position);
                int type = obj.optInt(TEMPLATE_INT, -1);
                if (type <= -1) {
                    type = mStringTab.getStringId(obj.optString(TEMPLATE));
                    obj.put(TEMPLATE_INT, type);
                }
//                LogHelper.d(TAG, "getItemViewType:" + type);
                return type;
            } catch (JSONException e) {
                LogHelper.e(TAG, "getItemViewType:" + e + "  obj:" + obj);
            }
        } else {
            LogHelper.e(TAG, "getItemViewType data is null");
        }

        return -1;
    }

    @Override
    public int getItemCount() {
        if (null != mData) {
//            LogHelper.d(TAG, "getItemCount:" + mData.length());
            return mData.length();
        }
        return 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
