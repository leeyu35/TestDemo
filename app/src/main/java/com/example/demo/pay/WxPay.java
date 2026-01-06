package com.example.demo.pay;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.JumpToOfflinePay;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.mm.opensdk.utils.ILog;

public class WxPay {
    private static final String TAG = "WxPay";
    private static final String APP_ID = "wxbe3521a0fad2fe85";
    private final IWXAPI api;
    private final Activity activity;

    private static WxPay sInstance;

    public static WxPay newInstance(@NonNull Activity activity) {
        sInstance = new WxPay(activity);
        return sInstance;
    }

    public static WxPay getInstance() {
        if (sInstance == null) {
            throw new NullPointerException("请先进行初始化");
        }
        return sInstance;
    }

    public WxPay(Activity activity) {
        this.activity = activity;
        api = WXAPIFactory.createWXAPI(activity, null);
        api.registerApp(APP_ID);
        api.setLogImpl(new ILog() {
            @Override
            public void v(String s, String s1) {
                Log.v(TAG, "wxapi log: " + s + s1);
            }

            @Override
            public void d(String s, String s1) {
                Log.d(TAG, "wxapi log: " + s + s1);
            }

            @Override
            public void i(String s, String s1) {
                Log.i(TAG, "wxapi log: " + s + s1);
            }

            @Override
            public void w(String s, String s1) {
                Log.w(TAG, "wxapi log: " + s + s1);
            }

            @Override
            public void e(String s, String s1) {
                Log.e(TAG, "wxapi log: " + s + s1);
            }
        });
        handleIntent(activity.getIntent());

        getTokenFromWX(activity,APP_ID);
    }



    public void pay(String params) {
//        JumpToOfflinePay.Req req = new JumpToOfflinePay.Req();
//        api.sendReq(req);

        wxPay(params);
//        initiateWeChatPay();
    }


    @RequiresApi(api = android.os.Build.VERSION_CODES.M)
    private String getTokenFromWX(Context var1, String appId) {
        if (var1.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Log.e("yunbo","有权限！");
            // 查询没有数据或权限不足
            Toast.makeText(var1, "有权限！", Toast.LENGTH_SHORT).show();
        } else {
//            requestPermissions((Activity) var1,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
            Log.e("yunbo","没有权限！");
            Toast.makeText(var1, "没有权限不足！", Toast.LENGTH_SHORT).show();
        }

        ContentResolver var10000 = var1.getContentResolver();
        Uri var10001 = Uri.parse("content://com.tencent.mm.sdk.comm.provider/genTokenForOpenSdk");
        String[] var3;
        String[] var10002 = var3 = new String[2];
        var10002[0] = appId;
        var10002[1] = "638066176";

        try (Cursor cursor = var10000.query(var10001, null, null, null, null)) {
            if (cursor != null && cursor.getCount() > 0) {
                // 查询成功并有数据
                Toast.makeText(var1, "数据查询成功！", Toast.LENGTH_SHORT).show();
            } else {
                // 查询没有数据或权限不足
                Toast.makeText(var1, "没有数据或权限不足！", Toast.LENGTH_SHORT).show();
            }
        } catch (SecurityException e) {
            // 捕获权限异常
            Toast.makeText(var1, "权限不足，无法访问内容提供者！", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            // 处理其他异常
            Toast.makeText(var1, "查询失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        Cursor var2;
        if ((var2 = var10000.query(var10001, (String[])null, (String)null, var3, (String)null)) != null && var2.moveToFirst()) {
            String var4;
            String var5 = var4 = var2.getString(0);
            com.tencent.mm.opensdk.utils.Log.i("MicroMsg.SDK.WXApiImplV10", "getTokenFromWX token is " + var4);
            var2.close();
            return var5;
        } else {
            com.tencent.mm.opensdk.utils.Log.e("MicroMsg.SDK.WXApiImplV10", "getTokenFromWX , token is null , if your app targetSdkVersion >= 30, include 'com.tencent.mm' in a set of <package> elements inside the <queries> element");
            return null;
        }
    }

    /**
     * 是否安装了微信
     */
    public boolean isWXAppInstalled() {
        return api.isWXAppInstalled();
    }

    /**
     * 是否微信app支持支付能力
     */
    public boolean isWXPaySupported() {
        return api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
    }


    private void initiateWeChatPay() {
        //"{\"timeStamp\":1722574845,\"package\":\"Sign=WXPay\",\"appId\":\"wx1b3e70d72b592e00\",\"sign\":\"KVnquHGo9YbjQ/Q2QTCXDtrdVzPzBOIdMWPmPnv1D6TRodAW5QyS0c9KOw9t7HFGGvN507YUqQ7mjsPz1N5120sqtevipQFl0wEirJX5UKPL898mJB2zPlcV3wGWydsXlWjWtfUvCvVWYDMP737BIB01Qb9NqBM7fpgei1QyEQ1FbRdzgzv+Oat48JSxREjlVQshn+7odlnszw78bgAs6fp6W1KzGyaoX9vClhRXF1NK4FDc75sWFQW2Jiau7rz90j92d0OZpSzTVIkvGhm1tugdcox/KX1ffnlrd8sPn9JT4KM10uYpg3pRC2sNofeBlpMTH8o+LlcSxampZkSILw==\",\"signType\":\"RSA\",\"prepayid\":\"up_wx021300455403591fc20029378fb8e10001\",\"partnerid\":\"1680558744\",\"nonceStr\":\"XJEReIsHGHXGlyrtonffEVEOizFQXuCW\"}"
        PayReq request = new PayReq();
        request.appId = APP_ID;
        request.partnerId = "1680558744"; // 替换为你的微信支付商户号
        request.prepayId = "up_wx021300455403591fc20029378fb8e10001"; // 替换为服务器端获取的预支付ID
        request.packageValue = "Sign=WXPay";
        request.nonceStr = "XJEReIsHGHXGlyrtonffEVEOizFQXuCW"; // 随机字符串
        request.timeStamp = "1722574845"; // 时间戳
        request.sign = "KVnquHGo9YbjQ/Q2QTCXDtrdVzPzBOIdMWPmPnv1D6TRodAW5QyS0c9KOw9t7HFGGvN507YUqQ7mjsPz1N5120sqtevipQFl0wEirJX5UKPL898mJB2zPlcV3wGWydsXlWjWtfUvCvVWYDMP737BIB01Qb9NqBM7fpgei1QyEQ1FbRdzgzv+Oat48JSxREjlVQshn+7odlnszw78bgAs6fp6W1KzGyaoX9vClhRXF1NK4FDc75sWFQW2Jiau7rz90j92d0OZpSzTVIkvGhm1tugdcox/KX1ffnlrd8sPn9JT4KM10uYpg3pRC2sNofeBlpMTH8o+LlcSxampZkSILw=="; // 签名，详见微信支付文档

        boolean isReqSent = api.sendReq(request);
        if (!isReqSent) {
            Toast.makeText(activity, "支付请求发送失败", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 唤起微信支付
     */
    private void wxPay(String params) {
//        if (!isWXAppInstalled()) {
//            Log.e(TAG, "未安装微信");
//            return;
//        }
//
//        if (!isWXPaySupported()) {
//            Log.e(TAG, "您的微信版本不支持支付，请安装最新版");
//            return;
//        }

        PayReq request = new PayReq();
        try {
            JSONObject signJSON = JSONObject.parseObject(params);
            request.packageValue = signJSON.getString("package");
            request.appId = signJSON.getString("appId");
            request.sign = signJSON.getString("sign");
            request.signType = signJSON.getString("signType");
            request.partnerId = signJSON.getString("partnerid");
            request.prepayId = signJSON.getString("prepayid");
            request.nonceStr = signJSON.getString("nonceStr");
            request.timeStamp = signJSON.getString("timeStamp");
            if (!api.sendReq(request)) {
                Log.e(TAG, "支付参数错误");
            }
        } catch (Exception e) {
            Log.e(TAG, "支付参数错误");
        }
    }

    /**
     * 处理微信sdk回调
     */
    public boolean handleIntent(Intent intent) {
        Log.e(TAG, "---handleIntent---");

        return api.handleIntent(intent, new IWXAPIEventHandler() {
            @Override
            public void onResp(BaseResp resp) {
                Log.e(TAG, "---[handleIntent.onResp]----------------------------------------------");
                Log.e(TAG, "---[handleIntent.onResp]---resp.openId--------" + resp.openId);
                Log.e(TAG, "---[handleIntent.onResp]---resp.transaction---" + resp.transaction);
                Log.e(TAG, "---[handleIntent.onResp]---resp.getType-------" + resp.getType());
                Log.e(TAG, "---[handleIntent.onResp]---resp.errCode-------" + resp.errCode);
                Log.e(TAG, "---[handleIntent.onResp]---resp.errStr--------" + resp.errStr);

                handleResponse(resp);
            }

            @Override
            public void onReq(BaseReq req) {
                Log.e(TAG, "---[handleIntent.onReq]-----------------------------------------------");
                Log.e(TAG, "---[handleIntent.onReq]---req.openId--------" + req.openId);
                Log.e(TAG, "---[handleIntent.onReq]---req.transaction---" + req.transaction);
                Log.e(TAG, "---[handleIntent.onReq]---req.getType-------" + req.getType());
            }
        });
    }

    private void handleResponse(final BaseResp resp) {
        switch (resp.getType()) {
            case ConstantsAPI.COMMAND_PAY_BY_WX:
                if (BaseResp.ErrCode.ERR_OK == resp.errCode) {
                    Log.e(TAG, "支付成功");
                    return;
                } else if (BaseResp.ErrCode.ERR_USER_CANCEL == resp.errCode) {
                    Log.e(TAG,  "用户取消");
                } else {
                    Log.e(TAG, "支付失败");
                }
                break;
            default:
                break;
        }
    }
}
