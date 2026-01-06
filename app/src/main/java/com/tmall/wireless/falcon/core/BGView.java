package com.tmall.wireless.falcon.core;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by gujicheng on 17/5/26.
 */

public class BGView extends View implements IView {
    private final static String TAG = "BGView";

    private int mVTop;
    private int mVLeft;

    public BGView(Context context) {
        super(context);
    }

    public void setVTop(int top) {
        mVTop = top;
    }

    public void setVLeft(int left) {
        mVLeft = left;
    }

    @Override
    public void reset() {
    }

    @Override
    public int getComponentId() {
        return 0;
    }

    @Override
    public void setComponentId(int id) {

    }

    @Override
    public int getVId() {
        return -1;
    }

    @Override
    public void setVId(int id) {
    }

    @Override
    public void setData(Object data) {

    }

    @Override
    public boolean setStyle(int keyId, Object value) {
        return false;
    }

    @Override
    public void setAuxiliary(Object auxiliary) {

    }

    @Override
    public void setBean(IBean bean) {

    }

    @Override
    public IBean getBean() {
        return null;
    }

    @Override
    public void setRawData(Object data) {

    }

    @Override
    public Object getRawData() {
        return null;
    }

    @Override
    public void setAutoDimDirection(int value) {

    }

    @Override
    public void setAutoDimX(float value) {

    }

    @Override
    public void setAutoDimY(float value) {

    }

    @Override
    public int getVPaddingLeft() {
        return 0;
    }

    @Override
    public int getVPaddingTop() {
        return 0;
    }

    @Override
    public int getVPaddingRight() {
        return 0;
    }

    @Override
    public int getVPaddingBottom() {
        return 0;
    }

    @Override
    public int getVLeft() {
        return mVLeft;
    }

    @Override
    public int getVTop() {
        return mVTop;
    }

    @Override
    public void vMeasure(int widthMeasureSpec, int heightMeasureSpec) {

    }

    @Override
    public int getVMeasuredWidth() {
        return 0;
    }

    @Override
    public int getVMeasuredHeight() {
        return 0;
    }

    @Override
    public int getVMeasuredWidthWithMargin() {
        return 0;
    }

    @Override
    public int getVMeasuredHeightWithMargin() {
        return 0;
    }

    @Override
    public void vLayout(int left, int top, int right, int bottom) {
        layout(left, top, right, bottom);
    }

    @Override
    public boolean isGone() {
        return false;
    }

    public void setVMeasuredDimension(int width, int height) {
        setMeasuredDimension(width, height);
    }

    @Override
    public ViewGroup.MarginLayoutParams getVLayoutParams() {
        return null;
    }

    @Override
    public void setVLayoutParams(ViewGroup.MarginLayoutParams param) {

    }

    @Override
    public void setComponentData(Object data) {

    }

    @Override
    public void onLoadFinished() {

    }

    @Override
    public void release() {

    }

    @Override
    public boolean setIntValue(int key, int value) {
        return false;
    }

    @Override
    public boolean setFloatValue(int key, float value) {
        return false;
    }

    @Override
    public boolean setStrValue(int key, String value) {
        return false;
    }
}
