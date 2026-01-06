package com.tmall.wireless.falcon.core;

import com.libra.common.Common;
import com.libra.common.LogHelper;
import com.libra.common.io.reader.Reader;
import com.tmall.wireless.falcon.PageContext;

/**
 * Created by gujicheng on 17/5/10.
 */

public class PageLoader {
    private final static String TAG = "PageLoader";

    public boolean load(PageContext pageContext, Reader reader) {
        if (null != reader) {
            // check Magic
            byte[] magic = Common.MAGIC.getBytes();
            int i;
            for (i = 0; i < magic.length; ++i) {
                if (magic[i] != reader.readByte()) {
                    break;
                }
            }
            if (i < magic.length) {
                LogHelper.e(TAG, "magic invalidate");
                return false;
            }

            // check version
            int minorVersion = reader.readShort();
            int majorVersion = reader.readShort();
            if (Common.MINOR_VERSION != minorVersion || Common.MAJOR_VERSION != majorVersion) {
                LogHelper.e(TAG, "version invalidate major:" + majorVersion + "  minor:" + minorVersion);
                return false;
            } else {
                pageContext.setVersion(majorVersion, minorVersion);
            }

            // read page
            if (!pageContext.load(reader)) {
                LogHelper.e(TAG, "read page failed");
                return false;
            }
            return  true;
        }

        return false;
    }
}
