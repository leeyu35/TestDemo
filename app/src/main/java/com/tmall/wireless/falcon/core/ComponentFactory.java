package com.tmall.wireless.falcon.core;

import android.view.ViewGroup;

import com.libra.common.Common;
import com.libra.common.LogHelper;
import com.libra.common.StringTab;
import com.libra.common.io.reader.Reader;
import com.tmall.wireless.falcon.PageContext;
import com.tmall.wireless.falcon.common.Utils;
import com.tmall.wireless.falcon.layout.ILayout;
import com.tmall.wireless.falcon.layout.LayoutBase;
import com.tmall.wireless.falcon.layout.android.VFrameLayout;
import com.tmall.wireless.falcon.layout.android.VLinearLayout;
import com.tmall.wireless.falcon.layout.android.VRelativeLayout;
import com.tmall.wireless.falcon.layout.flexbox.VFlexboxLayout;
import com.tmall.wireless.falcon.layout.libra.AbsoluteLayout;
import com.tmall.wireless.falcon.layout.libra.LinearLayout;
import com.tmall.wireless.falcon.layout.libra.VAbsoluteLayout;
import com.tmall.wireless.falcon.layout.libra.VHLayout;
import com.tmall.wireless.falcon.layout.libra.VJustifyLayout;
import com.tmall.wireless.falcon.layout.libra.VRatioLayout;
import com.tmall.wireless.falcon.view.Grid;
import com.tmall.wireless.falcon.view.Image;
import com.tmall.wireless.falcon.view.Line;
import com.tmall.wireless.falcon.view.Page;
import com.tmall.wireless.falcon.view.Scroller;
import com.tmall.wireless.falcon.view.Slot;
import com.tmall.wireless.falcon.view.Text;
import com.tmall.wireless.falcon.view.ViewCache;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * Created by gujicheng on 17/5/10.
 */

public class ComponentFactory {
    private final static String TAG = "ComponentFactory";

    private static final int STATE_continue = 0;
    private static final int STATE_successful = 1;
    private static final int STATE_failed = 2;

    private List<Stack<IView>> mViewStackCache = new LinkedList<>();
    private Map<String, IBuilder> mBuilders = new HashMap<>();

    public ComponentFactory() {
        registerBuilder(new Text.Builder());
        registerBuilder(new Image.Builder());
        registerBuilder(new Scroller.Builder());
        registerBuilder(new Slot.Builder());
        registerBuilder(new Grid.Builder());
        registerBuilder(new Page.Builder());
        registerBuilder(new Line.Builder());
        registerBuilder(new AbsoluteLayout.Builder());
        registerBuilder(new VAbsoluteLayout.Builder());
        registerBuilder(new LinearLayout.Builder());
        registerBuilder(new VHLayout.Builder());
        registerBuilder(new VJustifyLayout.Builder());
        registerBuilder(new VRatioLayout.Builder());
        registerBuilder(new VRelativeLayout.Builder());
        registerBuilder(new VFrameLayout.Builder());
        registerBuilder(new VLinearLayout.Builder());
        registerBuilder(new VFlexboxLayout.Builder());
    }

    public void registerBuilder(IBuilder viewBuilder) {
        if (null != viewBuilder) {
            mBuilders.put(viewBuilder.getName(), viewBuilder);
        }
    }

    public void release() {
        if (null != mBuilders) {
            mBuilders.clear();
            mBuilders = null;
        }
        releaseViewStack();
    }

    private synchronized Stack<IView> mallocViewStack() {
        if (mViewStackCache.size() > 0 && null != mViewStackCache) {
            return mViewStackCache.remove(0);
        }
        return new Stack<>();
    }

    private synchronized void freeViewStack(Stack<IView> viewStack) {
        if (null != viewStack && null != mViewStackCache) {
            viewStack.clear();
            mViewStackCache.add(viewStack);
        }
    }

    private synchronized void releaseViewStack() {
        if (null != mViewStackCache) {
            mViewStackCache.clear();
            mViewStackCache = null;
        }
    }

    public IView newComponent(Reader reader, PageContext pageContext) {
        IView ret = null;
        if (null != reader) {
            Stack<IView> comArr = mallocViewStack();
            IView curView = null;
            StringTab stringTab = pageContext.getStringTab();
            ViewCache viewCache = new ViewCache();
            int state = STATE_continue;
            int tag = reader.readByte();
            while (true) {
                switch (tag) {
                    case Common.TAG_VIEW_START:
                        String comName = stringTab.getString(reader.readShort());
                        IView view = createView(pageContext, comName, viewCache);
                        if (null != view) {
                            ViewGroup.MarginLayoutParams p;
                            if (null != curView) {
                                p = ((ILayout)curView).generateLayoutParams();
                                comArr.push(curView);
                            } else {
                                p = LayoutBase.generateRootLayoutParams();
                            }
                            view.setVLayoutParams(p);
                            curView = view;
                            IView viewBase = view;

                            IAttr pAttr = (IAttr)p;
                            // int
                            byte attrCount = reader.readByte();
                            while (attrCount > 0) {
                                short key = reader.readShort();
                                int value = reader.readInt();
                                if (!viewBase.setIntValue(key, value)) {
                                    pAttr.setIntValue(key, value);
                                }

                                if (key == StringTab.STR_ID_id) {
                                    viewCache.put(value, viewBase);
                                }

                                --attrCount;
                            }

                            // WP int
                            attrCount = reader.readByte();
                            while (attrCount > 0) {
                                short key = reader.readShort();
                                int value = Utils.wp2px(reader.readInt());
                                if (!viewBase.setIntValue(key, value)) {
                                    pAttr.setIntValue(key, value);
                                }
                                --attrCount;
                            }

                            // DP int
                            attrCount = reader.readByte();
                            while (attrCount > 0) {
                                short key = reader.readShort();
                                int value = Utils.dp2px(reader.readInt());
                                if (!viewBase.setIntValue(key, value)) {
                                    pAttr.setIntValue(key, value);
                                }
                                --attrCount;
                            }

                            // SP int
                            attrCount = reader.readByte();
                            while (attrCount > 0) {
                                short key = reader.readShort();
                                int value = reader.readInt();
                                if (!viewBase.setIntValue(key, value)) {
                                    pAttr.setIntValue(key, value);
                                }
                                --attrCount;
                            }

                            // float
                            attrCount = reader.readByte();
                            while (attrCount > 0) {
                                short key = reader.readShort();
                                float value = Float.intBitsToFloat(reader.readInt());
                                if (!viewBase.setFloatValue(key, value)) {
                                    pAttr.setFloatValue(key, value);
                                }
                                --attrCount;
                            }

                            // WP float
                            attrCount = reader.readByte();
                            while (attrCount > 0) {
                                short key = reader.readShort();
                                float value = Utils.wp2px_f(Float.intBitsToFloat(reader.readInt()));
                                if (!viewBase.setFloatValue(key, value)) {
                                    pAttr.setFloatValue(key, value);
                                }
                                --attrCount;
                            }

                            // DP float
                            attrCount = reader.readByte();
                            while (attrCount > 0) {
                                short key = reader.readShort();
                                float value = Utils.dp2px_f(Float.intBitsToFloat(reader.readInt()));
                                if (!viewBase.setFloatValue(key, value)) {
                                    pAttr.setFloatValue(key, value);
                                }
                                --attrCount;
                            }

                            // SP float
                            attrCount = reader.readByte();
                            while (attrCount > 0) {
                                short key = reader.readShort();
                                float value = Float.intBitsToFloat(reader.readInt());
                                if (!viewBase.setFloatValue(key, value)) {
                                    pAttr.setFloatValue(key, value);
                                }
                                --attrCount;
                            }

                            // str
                            attrCount = reader.readByte();
                            while (attrCount > 0) {
                                short key = reader.readShort();
                                String value = stringTab.getString(reader.readInt());
                                if (!viewBase.setStrValue(key, value)) {
                                    pAttr.setStrValue(key, value);
                                }
                                --attrCount;
                            }

                            viewBase.onLoadFinished();
                        } else {
                            state = STATE_failed;
                            LogHelper.e(TAG, "can not find view id:" + comName);
                        }
                        break;

                    case Common.TAG_VIEW_END:
                        if (comArr.size() > 0) {
                            IView c = comArr.pop();
                            if (c instanceof ILayout) {
                                ((ILayout)c).addChild(curView);
                            } else {
                                state = STATE_failed;
                                LogHelper.e(TAG, "com can not contain sub component");
                            }
                            curView = c;
                        } else {
                            // can break;
                            state = STATE_successful;
                        }
                        break;

                    default:
                        LogHelper.e(TAG, "invalidate tag type:" + tag);
                        state = STATE_failed;
                        break;
                }

                if (STATE_continue != state) {
                    break;
                } else {
                    tag = reader.readByte();
                }
            }

            freeViewStack(comArr);

            if (STATE_successful == state) {
                ret = curView;
            }
        } else {
            LogHelper.e(TAG, "can not find component");
        }

        return ret;
    }

    private IView createView(PageContext pageContext, String comName, ViewCache viewCache) {
        IBuilder builder = mBuilders.get(comName);
        if (null != builder) {
            return builder.build(pageContext, viewCache);
        }
        return null;
    }

}
