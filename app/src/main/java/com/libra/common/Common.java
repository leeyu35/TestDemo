package com.libra.common;

/**
 * Created by gujicheng on 17/5/10.
 */

public class Common {
    public final static String PREFIX_ASSET = "asset";
    public final static String PREFIX_SDCARD = "sdcard";
    public final static String PREFIX_BUFFER = "buffer";
    public final static String PREFIX_SEPARATOR = "@";
    public final static String BUFFER_LOCATION = PREFIX_BUFFER + PREFIX_SEPARATOR + PREFIX_BUFFER;

    public final static String MAGIC = "FREND";

    public final static int MAJOR_VERSION = 1;
    public final static int MINOR_VERSION = 0;

    public final static int TAG_VIEW_START = 0;
    public final static int TAG_VIEW_END = 1;

    public final static int LINE_STYLE_SOLID = 1;
    public final static int LINE_STYLE_DASH = 2;

    public final static int ORIENTATION_VERTICAL = 0;
    public final static int ORIENTATION_HORIZONTAL = 1;

    public final static int TEXT_STYLE_BOLD = 0;
    public final static int TEXT_STYLE_ITALIC = 1;
    public final static int TEXT_STYLE_STRIKE = 2;

    public final static int TEXT_ELLIPSIZE_START = 0;
    public final static int TEXT_ELLIPSIZE_MIDDLE = 1;
    public final static int TEXT_ELLIPSIZE_END = 2;
    public final static int TEXT_ELLIPSIZE_MARQUEE = 3;

    public final static int JUSTIFY_LAYOUT_ORIENTATION_START = 1;
    public final static int JUSTIFY_LAYOUT_ORIENTATION_END = 2;

    public final static int RATIO_LAYOUT_ORIENTATION_VERTICAL = 1;
    public final static int RATIO_LAYOUT_ORIENTATION_HORIZONTAL = 2;

    public final static int SCROLLER_MODE_StaggeredGrid = 1;
    public final static int SCROLLER_MODE_Linear = 2;

    public final static String SUPPORT_WATERFALL = "supportWaterfall";

    public final static String UNIT_STR_WP = "wp";
    public final static String UNIT_STR_DP = "dp";
    public final static String UNIT_STR_SP = "sp";

    public final static int ATTR_ITEM_UNIT_NONE = 0;
    public final static int ATTR_ITEM_UNIT_WP = 1;
    public final static int ATTR_ITEM_UNIT_DP = 2;
    public final static int ATTR_ITEM_UNIT_SP = 3;

    public final static int ATTR_ITEM_TYPE_NONE = -1;
    public final static int ATTR_ITEM_TYPE_INT = 0;
    public final static int ATTR_ITEM_TYPE_INT_WP = 1;
    public final static int ATTR_ITEM_TYPE_INT_DP = 2;
    public final static int ATTR_ITEM_TYPE_INT_SP = 3;
    public final static int ATTR_ITEM_TYPE_FLOAT = 4;
    public final static int ATTR_ITEM_TYPE_FLOAT_WP = 5;
    public final static int ATTR_ITEM_TYPE_FLOAT_DP = 6;
    public final static int ATTR_ITEM_TYPE_FLOAT_SP = 7;
    public final static int ATTR_ITEM_TYPE_STRING = 8;

    public final static int SCALE_TYPE_MATRIX = 0;
    public final static int SCALE_TYPE_FIT_XY = 1;
    public final static int SCALE_TYPE_FIT_START = 2;
    public final static int SCALE_TYPE_FIT_CENTER = 3;
    public final static int SCALE_TYPE_FIT_END = 4;
    public final static int SCALE_TYPE_CENTER = 5;
    public final static int SCALE_TYPE_CENTER_CROP = 6;
    public final static int SCALE_TYPE_CENTER_INSIDE = 7;


    public final static int AUTO_DIM_DIRECTION_NONE = 0;
    public final static int AUTO_DIM_DIRECTION_X = 1;
    public final static int AUTO_DIM_DIRECTION_Y = 2;
}
