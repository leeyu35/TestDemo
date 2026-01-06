package com.tmall.wireless.falcon.view;

import android.view.View;

import com.tmall.wireless.falcon.PageContext;

/**
 * Created by gujicheng on 16/12/15.
 */

public abstract class Adapter {
    protected final static String TYPE = "vType";

    protected PageContext mPageContext;
    protected boolean mDataIsChange = true;

    public Adapter(PageContext context) {
        mPageContext = context;
    }

    public abstract void setData(Object str);

    public abstract int getItemCount();

    public int getType(int pos) {
        return 0;
    }

    public abstract void onBindViewHolder(ViewHolder vh, int pos);

    public abstract ViewHolder onCreateViewHolder(int viewType);

    public void notifyChange() {
        mDataIsChange = true;
    }

    public static class ViewHolder {
        public ViewHolder(View v) {
            mItemView = v;
            mItemView.setTag(this);
        }

        public View mItemView;
        public int mType;
    }

}
