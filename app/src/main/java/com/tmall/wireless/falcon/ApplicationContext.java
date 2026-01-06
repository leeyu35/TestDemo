package com.tmall.wireless.falcon;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.libra.common.LogHelper;
import com.libra.common.StringTab;
import com.libra.common.io.loader.reader.ReaderFactory;
import com.tmall.wireless.falcon.common.Utils;
import com.tmall.wireless.falcon.core.AsyncWorkManager;
import com.tmall.wireless.falcon.core.ComponentFactory;
import com.tmall.wireless.falcon.core.PageLoader;
import com.tmall.wireless.falcon.core.TemplateLibraryManager;
import com.tmall.wireless.falcon.event.DefaultClickProcessor;
import com.tmall.wireless.falcon.core.IView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gujicheng on 17/5/28.
 */

public abstract class ApplicationContext {
    private final static String TAG = "ApplicationContext";

    protected Context mContext;
    protected ReaderFactory mReaderFactory = new ReaderFactory();
    protected PageLoader mPageLoader = new PageLoader();
    protected ComponentFactory mComponentFactory = new ComponentFactory();
    protected TemplateLibraryManager mTemplateLibraryManager = new TemplateLibraryManager();
    protected AsyncWorkManager mAsyncWorkManager = new AsyncWorkManager();
    protected DefaultClickProcessor mDefaultClickProcessor = new DefaultClickProcessor(this);
    protected Map<String, PageContext> mPageContextMap = new HashMap<>();

    public ApplicationContext(Context context) {
        Utils.init(context);
        mContext = context;
    }

    public DefaultClickProcessor getDefaultClickProcessor() {
        return mDefaultClickProcessor;
    }
    public AsyncWorkManager getAsyncWorkManager() {
        return mAsyncWorkManager;
    }
    public TemplateLibraryManager getTemplateManager() {
        return mTemplateLibraryManager;
    }

    public Context getContext() {
        return mContext;
    }

    public abstract boolean jump(String target, String params);
    public abstract int setBackground(final String uri, final View view);
    public abstract int setBackground(final String uri, final View view, final int reqWidth, final int reqHeight);
    public abstract int setImageSrc(final String uri, final ImageView imageView);
    public abstract int setImageSrc(final String uri, final ImageView imageView,
                                    final int reqWidth, final int reqHeight);
    public abstract void loadData(String location, IView view, StringTab stringTab);

    public ComponentFactory getComponentFactory() {
        return mComponentFactory;
    }

    public PageContext getPageContext(String pageName) {
        return mPageContextMap.get(pageName);
    }

    public PageContext loadPage(String location) {
        return loadPage(location, null);
    }

    public PageContext loadPage(String location, Object param) {
        PageContext pageContext = new PageContext(this);
        if (null != pageContext) {
            if (mPageLoader.load(pageContext, mReaderFactory.getReader(location, param))) {
                mPageContextMap.put(pageContext.getMainComName(), pageContext);
            } else {
                pageContext = null;
                LogHelper.e(TAG, "load page failed");
            }
        } else {
            LogHelper.e(TAG, "load page, pageContext is null");
        }
        return pageContext;
    }

    public void removePage(PageContext pageContext) {
        if (null != pageContext) {
            pageContext.release();
            mPageContextMap.remove(pageContext.getMainComName());
        } else {
            LogHelper.e(TAG, "removePage failed");
        }
    }

    public void destroy() {
        if (null != mAsyncWorkManager) {
            mAsyncWorkManager.release();
            mAsyncWorkManager = null;
        }

        if (null != mReaderFactory) {
            mReaderFactory.release();
            mReaderFactory = null;
        }

        mPageLoader = null;

        if (null != mComponentFactory) {
            mComponentFactory.release();
            mComponentFactory = null;
        }

        if (null != mPageContextMap) {
            mPageContextMap.clear();
            mPageContextMap = null;
        }

        if (null != mTemplateLibraryManager) {
            mTemplateLibraryManager.release();
            mTemplateLibraryManager = null;
        }
    }
}

