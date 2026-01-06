package com.libra.common;

import android.text.TextUtils;

import com.libra.common.io.reader.Reader;
import com.libra.common.io.writer.Writer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gujicheng on 17/5/9.
 */

public class StringTab {
    private final static String TAG = "StringTab";

    private List<String> mTab = new ArrayList<>();
    private Map<String, Integer> mMap = new HashMap<>();

    public StringTab() {
    }

    private void loadSystemString() {
        for (int i = 0; i < SYS_KEYS.length; ++i) {
            mTab.add(SYS_KEYS[i]);
            mMap.put(SYS_KEYS[i], i);
        }
    }

    public void release() {
        reset();
    }

    public void reset() {
        mTab.clear();
        mMap.clear();
        loadSystemString();
    }

    public String getString(int id) {
        if (id > -1 && id < mTab.size()) {
            return mTab.get(id);
        } else {
            LogHelper.e(TAG, "getString, id invalidate:" + id);
        }

        return null;
    }

    public int getStringId(String str) {
        return getStringId(str, true);
    }

    public int getStringId(String str, boolean create) {
        if (!TextUtils.isEmpty(str)) {
            Integer value = mMap.get(str);
            if (null != value) {
                return value;
            } else if (create) {
                int id = mTab.size();
                mTab.add(str);
                mMap.put(str, id);
                return id;
            } else {
                LogHelper.e(TAG, "getStringId, not string:" + str);
            }
        } else {
            LogHelper.e(TAG, "getStringId, str is empty");
        }

        return -1;
    }

    public boolean load(Reader reader) {
        boolean ret = false;

        if (null != reader) {
            int size = reader.readShort();
            for (int i = 0; i < size; ++i) {
                int len = reader.readShort();
                String str = new String(reader.getBuffer(), reader.getPos(), len);
                mTab.add(str);
                mMap.put(str, i);
                reader.seekRel(len);
            }

            ret = true;
        }

        return ret;
    }

    public boolean store(Writer writer) {
        boolean ret = false;

        if (null != writer) {
            int size = mTab.size();
            writer.writeShort(size);
            for (int i = 0; i < size; ++i) {
                String str = mTab.get(i);
                byte[] bs = str.getBytes();
                writer.writeShort(bs.length);
                writer.writeBytes(bs);
            }

            ret = true;
        } else {
            LogHelper.e(TAG, "writer is null");
        }

        return ret;
    }

    protected final static String[] SYS_KEYS = {
            "LinearLayout", "VLinearLayout", "VJustifyLayout", "VRatioLayout", "AbsoluteLayout", "VAbsoluteLayout", "TextView", "ImageView", "Scroller", "Slot", "Grid", "Page", "Line", "VFlexLayout", "VRelativeLayout", "VFrameLayout",
            "padding", "paddingLeft", "paddingTop", "paddingRight", "paddingBottom", "backgroundColor", "visibility", "autoDimDirection", "autoDimX", "autoDimY", "background", "dataSource", "id", "bean",
            "layout_width", "layout_height",
            "layout_weight", "layout_gravity", "layout_marginLeft", "layout_marginTop", "layout_marginRight", "layout_marginBottom", "orientation",
            "layout_orientation",
            "layout_ratio",
            "layout_x", "layout_y",
            "text", "textSize", "textColor", "textStyle", "ellipsize", "gravity", "lines", "lineSpaceMultiplier",
            "src", "scaleType",
            "columnCount", "itemHorizontalMargin", "itemVerticalMargin", "itemHeight",
            "autoSwitch", "canSlide", "stayTime", "animatorTime", "autoSwitchTime",
            "lineOrientation", "lineStyle", "lineColor", "lineWidth",
            "mode",
            "layout_order", "layout_flexGrow", "layout_flexShrink", "layout_alignSelf", "layout_flexBasisPercent", "layout_minWidth", "layout_minHeight", "layout_maxWidth", "layout_maxHeight", "layout_wrapBefore", "flexDirection", "flexWrap", "justifyContent", "alignItems", "alignContent",
            "layout_above", "layout_below", "layout_alignBaseline", "layout_alignBottom", "layout_alignEnd", "layout_alignRight", "layout_alignLeft", "layout_alignStart", "layout_alignTop", "layout_toStartOf", "layout_toEndOf", "layout_toLeftOf", "layout_toRightOf", "layout_centerHorizontal", "layout_centerVertical", "layout_centerInParent", "layout_alignParentStart", "layout_alignParentEnd", "layout_alignParentLeft", "layout_alignParentTop", "layout_alignParentRight", "layout_alignParentBottom"
        };

    public final static int STR_ID_COMPONENT_BASE = 0;
    public final static int STR_ID_LinearLayout = STR_ID_COMPONENT_BASE + 0;
    public final static int STR_ID_VLinearLayout = STR_ID_COMPONENT_BASE + 1;
    public final static int STR_ID_VJustifyLayout = STR_ID_COMPONENT_BASE + 2;
    public final static int STR_ID_VRatioLayout = STR_ID_COMPONENT_BASE + 3;
    public final static int STR_ID_AbsoluteLayout = STR_ID_COMPONENT_BASE + 4;
    public final static int STR_ID_VAbsoluteLayout = STR_ID_COMPONENT_BASE + 5;
    public final static int STR_ID_TextView = STR_ID_COMPONENT_BASE + 6;
    public final static int STR_ID_ImageView = STR_ID_COMPONENT_BASE + 7;
    public final static int STR_ID_Scroller = STR_ID_COMPONENT_BASE + 8;
    public final static int STR_ID_Slot = STR_ID_COMPONENT_BASE + 9;
    public final static int STR_ID_Grid = STR_ID_COMPONENT_BASE + 10;
    public final static int STR_ID_Page = STR_ID_COMPONENT_BASE + 11;
    public final static int STR_ID_Line = STR_ID_COMPONENT_BASE + 12;
    public final static int STR_ID_VFlexLayout = STR_ID_COMPONENT_BASE + 13;
    public final static int STR_ID_VRelativeLayout = STR_ID_COMPONENT_BASE + 14;
    public final static int STR_ID_VFrameLayout = STR_ID_COMPONENT_BASE + 15;
    public final static int STR_ID_COMPONENT_COUNT = STR_ID_COMPONENT_BASE + 16;

    public final static int STR_ID_VIEW_BASE = STR_ID_COMPONENT_COUNT;
    public final static int STR_ID_padding = STR_ID_VIEW_BASE + 0;
    public final static int STR_ID_paddingLeft = STR_ID_VIEW_BASE + 1;
    public final static int STR_ID_paddingTop = STR_ID_VIEW_BASE + 2;
    public final static int STR_ID_paddingRight = STR_ID_VIEW_BASE + 3;
    public final static int STR_ID_paddingBottom = STR_ID_VIEW_BASE + 4;
    public final static int STR_ID_backgroundColor = STR_ID_VIEW_BASE + 5;
    public final static int STR_ID_visibility = STR_ID_VIEW_BASE + 6;
    public final static int STR_ID_autoDimDirection = STR_ID_VIEW_BASE + 7;
    public final static int STR_ID_autoDimX = STR_ID_VIEW_BASE + 8;
    public final static int STR_ID_autoDimY = STR_ID_VIEW_BASE + 9;
    public final static int STR_ID_background = STR_ID_VIEW_BASE + 10;
    public final static int STR_ID_dataSource = STR_ID_VIEW_BASE + 11;
    public final static int STR_ID_id = STR_ID_VIEW_BASE + 12;
    public final static int STR_ID_bean = STR_ID_VIEW_BASE + 13;
    public final static int STR_ID_VIEW_COUNT = STR_ID_VIEW_BASE + 14;

    public final static int STR_ID_LAYOUT_ATTR_BASE = STR_ID_VIEW_COUNT;
    public final static int STR_ID_layout_width = STR_ID_LAYOUT_ATTR_BASE + 0;
    public final static int STR_ID_layout_height = STR_ID_LAYOUT_ATTR_BASE + 1;
    public final static int STR_ID_LAYOUT_ATTR_COUNT = STR_ID_LAYOUT_ATTR_BASE + 2;

    public final static int STR_ID_LINEAR_LAYOUT_ATTR_BASE = STR_ID_LAYOUT_ATTR_COUNT;
    public final static int STR_ID_layout_weight = STR_ID_LINEAR_LAYOUT_ATTR_BASE + 0;
    public final static int STR_ID_layout_gravity = STR_ID_LINEAR_LAYOUT_ATTR_BASE + 1;
    public final static int STR_ID_layout_marginLeft = STR_ID_LINEAR_LAYOUT_ATTR_BASE + 2;
    public final static int STR_ID_layout_marginTop = STR_ID_LINEAR_LAYOUT_ATTR_BASE + 3;
    public final static int STR_ID_layout_marginRight = STR_ID_LINEAR_LAYOUT_ATTR_BASE + 4;
    public final static int STR_ID_layout_marginBottom = STR_ID_LINEAR_LAYOUT_ATTR_BASE + 5;
    public final static int STR_ID_orientation = STR_ID_LINEAR_LAYOUT_ATTR_BASE + 6;
    public final static int STR_ID_LINEAR_LAYOUT_ATTR_COUNT = STR_ID_LINEAR_LAYOUT_ATTR_BASE + 7;

    public final static int STR_ID_JUSTIFY_LAYOUT_ATTR_BASE = STR_ID_LINEAR_LAYOUT_ATTR_COUNT;
    public final static int STR_ID_layout_orientation = STR_ID_JUSTIFY_LAYOUT_ATTR_BASE + 0;
    public final static int STR_ID_JUSTIFY_LAYOUT_ATTR_COUNT = STR_ID_JUSTIFY_LAYOUT_ATTR_BASE + 1;

    public final static int STR_ID_RATIO_LAYOUT_ATTR_BASE = STR_ID_JUSTIFY_LAYOUT_ATTR_COUNT;
    public final static int STR_ID_layout_ratio = STR_ID_RATIO_LAYOUT_ATTR_BASE + 0;
    public final static int STR_ID_RATIO_LAYOUT_ATTR_COUNT = STR_ID_RATIO_LAYOUT_ATTR_BASE + 1;

    public final static int STR_ID_ABSOLUTE_LAYOUT_ATTR_BASE = STR_ID_RATIO_LAYOUT_ATTR_COUNT;
    public final static int STR_ID_layout_x = STR_ID_ABSOLUTE_LAYOUT_ATTR_BASE + 0;
    public final static int STR_ID_layout_y = STR_ID_ABSOLUTE_LAYOUT_ATTR_BASE + 1;
    public final static int STR_ID_ABSOLUTE_LAYOUT_ATTR_COUNT = STR_ID_ABSOLUTE_LAYOUT_ATTR_BASE + 2;

    public final static int STR_ID_TEXT_ATTR_BASE = STR_ID_ABSOLUTE_LAYOUT_ATTR_COUNT;
    public final static int STR_ID_text = STR_ID_TEXT_ATTR_BASE + 0;
    public final static int STR_ID_textSize = STR_ID_TEXT_ATTR_BASE + 1;
    public final static int STR_ID_textColor = STR_ID_TEXT_ATTR_BASE + 2;
    public final static int STR_ID_textStyle = STR_ID_TEXT_ATTR_BASE + 3;
    public final static int STR_ID_ellipsize = STR_ID_TEXT_ATTR_BASE + 4;
    public final static int STR_ID_gravity = STR_ID_TEXT_ATTR_BASE + 5;
    public final static int STR_ID_lines = STR_ID_TEXT_ATTR_BASE + 6;
    public final static int STR_ID_lineSpaceMultiplier = STR_ID_TEXT_ATTR_BASE + 7;
    public final static int STR_ID_TEXT_ATTR_COUNT = STR_ID_TEXT_ATTR_BASE + 8;

    public final static int STR_ID_IMAGE_ATTR_BASE = STR_ID_TEXT_ATTR_COUNT;
    public final static int STR_ID_src = STR_ID_IMAGE_ATTR_BASE + 0;
    public final static int STR_ID_scaleType = STR_ID_IMAGE_ATTR_BASE + 1;
    public final static int STR_ID_IMAGE_ATTR_COUNT = STR_ID_IMAGE_ATTR_BASE + 2;

    public final static int STR_ID_GRID_ATTR_BASE = STR_ID_IMAGE_ATTR_COUNT;
    public final static int STR_ID_columnCount = STR_ID_GRID_ATTR_BASE + 0;
    public final static int STR_ID_itemHorizontalMargin = STR_ID_GRID_ATTR_BASE + 1;
    public final static int STR_ID_itemVerticalMargin = STR_ID_GRID_ATTR_BASE + 2;
    public final static int STR_ID_itemHeight = STR_ID_GRID_ATTR_BASE + 3;
    public final static int STR_ID_GRID_ATTR_COUNT = STR_ID_GRID_ATTR_BASE + 4;

    public final static int STR_ID_PAGE_ATTR_BASE = STR_ID_GRID_ATTR_COUNT;
    public final static int STR_ID_autoSwitch = STR_ID_PAGE_ATTR_BASE + 0;
    public final static int STR_ID_canSlide = STR_ID_PAGE_ATTR_BASE + 1;
    public final static int STR_ID_stayTime = STR_ID_PAGE_ATTR_BASE + 2;
    public final static int STR_ID_animatorTime = STR_ID_PAGE_ATTR_BASE + 3;
    public final static int STR_ID_autoSwitchTime = STR_ID_PAGE_ATTR_BASE + 4;
    public final static int STR_ID_PAGE_ATTR_COUNT = STR_ID_PAGE_ATTR_BASE + 5;

    public final static int STR_ID_LINE_ATTR_BASE = STR_ID_PAGE_ATTR_COUNT;
    public final static int STR_ID_lineOrientation = STR_ID_LINE_ATTR_BASE + 0;
    public final static int STR_ID_lineStyle = STR_ID_LINE_ATTR_BASE + 1;
    public final static int STR_ID_lineColor = STR_ID_LINE_ATTR_BASE + 2;
    public final static int STR_ID_lineWidth = STR_ID_LINE_ATTR_BASE + 3;
    public final static int STR_ID_LINE_ATTR_COUNT = STR_ID_LINE_ATTR_BASE + 4;

    public final static int STR_ID_SCROLLER_ATTR_BASE = STR_ID_LINE_ATTR_COUNT;
    public final static int STR_ID_mode = STR_ID_SCROLLER_ATTR_BASE + 0;
    public final static int STR_ID_SCROLLER_ATTR_COUNT = STR_ID_SCROLLER_ATTR_BASE + 1;

    public final static int STR_ID_VFLEX_LAYOUT_ATTR_BASE = STR_ID_SCROLLER_ATTR_COUNT;
    public final static int STR_ID_layout_order = STR_ID_VFLEX_LAYOUT_ATTR_BASE + 0;
    public final static int STR_ID_layout_flexGrow = STR_ID_VFLEX_LAYOUT_ATTR_BASE + 1;
    public final static int STR_ID_layout_flexShrink = STR_ID_VFLEX_LAYOUT_ATTR_BASE + 2;
    public final static int STR_ID_layout_alignSelf = STR_ID_VFLEX_LAYOUT_ATTR_BASE + 3;
    public final static int STR_ID_layout_flexBasisPercent = STR_ID_VFLEX_LAYOUT_ATTR_BASE + 4;
    public final static int STR_ID_layout_minWidth = STR_ID_VFLEX_LAYOUT_ATTR_BASE + 5;
    public final static int STR_ID_layout_minHeight = STR_ID_VFLEX_LAYOUT_ATTR_BASE + 6;
    public final static int STR_ID_layout_maxWidth = STR_ID_VFLEX_LAYOUT_ATTR_BASE + 7;
    public final static int STR_ID_layout_maxHeight = STR_ID_VFLEX_LAYOUT_ATTR_BASE + 8;
    public final static int STR_ID_layout_wrapBefore = STR_ID_VFLEX_LAYOUT_ATTR_BASE + 9;
    public final static int STR_ID_flexDirection = STR_ID_VFLEX_LAYOUT_ATTR_BASE + 10;
    public final static int STR_ID_flexWrap = STR_ID_VFLEX_LAYOUT_ATTR_BASE + 11;
    public final static int STR_ID_justifyContent = STR_ID_VFLEX_LAYOUT_ATTR_BASE + 12;
    public final static int STR_ID_alignItems = STR_ID_VFLEX_LAYOUT_ATTR_BASE + 13;
    public final static int STR_ID_alignContent = STR_ID_VFLEX_LAYOUT_ATTR_BASE + 14;
    public final static int STR_ID_VFLEX_LAYOUT_ATTR_COUNT = STR_ID_VFLEX_LAYOUT_ATTR_BASE + 15;

    public final static int STR_ID_VRELATIVE_LAYOUT_ATTR_BASE = STR_ID_VFLEX_LAYOUT_ATTR_COUNT;
    public final static int STR_ID_layout_above = STR_ID_VRELATIVE_LAYOUT_ATTR_BASE + 0;
    public final static int STR_ID_layout_below = STR_ID_VRELATIVE_LAYOUT_ATTR_BASE + 1;
    public final static int STR_ID_layout_alignBaseline = STR_ID_VRELATIVE_LAYOUT_ATTR_BASE + 2;
    public final static int STR_ID_layout_alignBottom = STR_ID_VRELATIVE_LAYOUT_ATTR_BASE + 3;
    public final static int STR_ID_layout_alignEnd = STR_ID_VRELATIVE_LAYOUT_ATTR_BASE + 4;
    public final static int STR_ID_layout_alignRight = STR_ID_VRELATIVE_LAYOUT_ATTR_BASE + 5;
    public final static int STR_ID_layout_alignLeft = STR_ID_VRELATIVE_LAYOUT_ATTR_BASE + 6;
    public final static int STR_ID_layout_alignStart = STR_ID_VRELATIVE_LAYOUT_ATTR_BASE + 7;
    public final static int STR_ID_layout_alignTop = STR_ID_VRELATIVE_LAYOUT_ATTR_BASE + 8;
    public final static int STR_ID_layout_toStartOf = STR_ID_VRELATIVE_LAYOUT_ATTR_BASE + 9;
    public final static int STR_ID_layout_toEndOf = STR_ID_VRELATIVE_LAYOUT_ATTR_BASE + 10;
    public final static int STR_ID_layout_toLeftOf = STR_ID_VRELATIVE_LAYOUT_ATTR_BASE + 11;
    public final static int STR_ID_layout_toRightOf = STR_ID_VRELATIVE_LAYOUT_ATTR_BASE + 12;
    public final static int STR_ID_layout_centerHorizontal = STR_ID_VRELATIVE_LAYOUT_ATTR_BASE + 13;
    public final static int STR_ID_layout_centerVertical = STR_ID_VRELATIVE_LAYOUT_ATTR_BASE + 14;
    public final static int STR_ID_layout_centerInParent = STR_ID_VRELATIVE_LAYOUT_ATTR_BASE + 15;
    public final static int STR_ID_layout_alignParentStart = STR_ID_VRELATIVE_LAYOUT_ATTR_BASE + 16;
    public final static int STR_ID_layout_alignParentEnd = STR_ID_VRELATIVE_LAYOUT_ATTR_BASE + 17;
    public final static int STR_ID_layout_alignParentLeft = STR_ID_VRELATIVE_LAYOUT_ATTR_BASE + 18;
    public final static int STR_ID_layout_alignParentTop = STR_ID_VRELATIVE_LAYOUT_ATTR_BASE + 19;
    public final static int STR_ID_layout_alignParentRight = STR_ID_VRELATIVE_LAYOUT_ATTR_BASE + 20;
    public final static int STR_ID_layout_alignParentBottom = STR_ID_VRELATIVE_LAYOUT_ATTR_BASE + 21;
    public final static int STR_ID_VRELATIVE_LAYOUT_ATTR_COUNT = STR_ID_VRELATIVE_LAYOUT_ATTR_BASE + 22;
}
