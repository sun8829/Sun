package com.ody.pay.wxpay;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * 微信支付请求
 *
 * @author Administrator
 */
public class WechatPayReq {

    private static final String TAG = WechatPayReq.class.getSimpleName();

    private Activity mActivity;

    private static PayListener mPayListener;

    //微信支付AppID
    private String appId;
    //微信支付商户号
    private String partnerId;
    //预支付码（重要）
    private String prepayId;
    //"Sign=WXPay"
    private String packageValue;
    private String nonceStr;
    //时间戳
    private String timeStamp;
    //签名
    private String sign;

    //微信支付核心api
    IWXAPI mWXApi;

    public WechatPayReq() {
        super();
    }


    /**
     * 发送微信支付请求
     */
    public boolean send() {
        mWXApi = WXAPIFactory.createWXAPI(mActivity, null);
//        mWXApi.handleIntent(mActivity.getIntent(), this);
        mWXApi.registerApp(this.appId);

        PayReq request = new PayReq();

        request.appId = this.appId;
        request.partnerId = this.partnerId;
        request.prepayId = this.prepayId;
        request.packageValue = this.packageValue != null ? this.packageValue : "Sign=WXPay";
        request.nonceStr = this.nonceStr;
        request.timeStamp = this.timeStamp;
        request.sign = this.sign;

//        PayBroadcastReceiver receiver = new PayBroadcastReceiver();
//        IntentFilter filter = new IntentFilter();
//        filter.addAction("com.ody.pay.wxpay.WechatPayReq.PayBroadcastReceiver");
//        mActivity.registerReceiver(receiver, filter);
        return mWXApi.sendReq(request);
    }

    public static class Builder {
        //上下文
        private Activity activity;

        private PayListener listener;
        //微信支付AppID
        private String appId;
        //微信支付商户号
        private String partnerId;
        //预支付码（重要）
        private String prepayId;
        //"Sign=WXPay"
        private String packageValue = "Sign=WXPay";
        private String nonceStr;
        //时间戳
        private String timeStamp;
        //签名
        private String sign;

        public Builder() {
            super();
        }

        public Builder with(Activity activity) {
            this.activity = activity;
            return this;
        }

        public Builder setOnPayListener(PayListener listener) {
            this.listener = listener;
            return this;
        }

        /**
         * 设置微信支付AppID
         *
         * @param appId
         * @return
         */
        public Builder setAppId(String appId) {
            this.appId = appId;
            return this;
        }

        /**
         * 微信支付商户号
         *
         * @param partnerId
         * @return
         */
        public Builder setPartnerId(String partnerId) {
            this.partnerId = partnerId;
            return this;
        }

        /**
         * 设置预支付码（重要）
         *
         * @param prepayId
         * @return
         */
        public Builder setPrepayId(String prepayId) {
            this.prepayId = prepayId;
            return this;
        }


        /**
         * 设置
         *
         * @param packageValue
         * @return
         */
        public Builder setPackageValue(String packageValue) {
            this.packageValue = packageValue;
            return this;
        }


        /**
         * 设置
         *
         * @param nonceStr
         * @return
         */
        public Builder setNonceStr(String nonceStr) {
            this.nonceStr = nonceStr;
            return this;
        }

        /**
         * 设置时间戳
         *
         * @param timeStamp
         * @return
         */
        public Builder setTimeStamp(String timeStamp) {
            this.timeStamp = timeStamp;
            return this;
        }

        /**
         * 设置签名
         *
         * @param sign
         * @return
         */
        public Builder setSign(String sign) {
            this.sign = sign;
            return this;
        }


        public WechatPayReq create() {
            WechatPayReq wechatPayReq = new WechatPayReq();

            wechatPayReq.mActivity = this.activity;

            wechatPayReq.mPayListener = this.listener;
            //微信支付AppID
            wechatPayReq.appId = this.appId;
            //微信支付商户号
            wechatPayReq.partnerId = this.partnerId;
            //预支付码（重要）
            wechatPayReq.prepayId = this.prepayId;
            //"Sign=WXPay"
            wechatPayReq.packageValue = this.packageValue;
            wechatPayReq.nonceStr = this.nonceStr;
            //时间戳
            wechatPayReq.timeStamp = this.timeStamp;
            //签名
            wechatPayReq.sign = this.sign;

            return wechatPayReq;
        }
    }

    /**
     * 微信支付监听
     *
     * @author Administrator
     */
    public interface PayListener {
        public void onPaySuccess();

        public void onPayFailure(int errorCode, String errStr);
    }

    public static class PayBroadcastReceiver extends BroadcastReceiver {

        public PayBroadcastReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                int code = intent.getIntExtra("code", -1);
                String errStr = intent.getStringExtra("errStr");

                if (code == BaseResp.ErrCode.ERR_OK) {
                    mPayListener.onPaySuccess();
                } else {
                    mPayListener.onPayFailure(code, errStr);
                }
            }
        }
    }
}
