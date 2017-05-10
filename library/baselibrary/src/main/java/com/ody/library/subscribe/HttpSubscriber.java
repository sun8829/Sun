package com.ody.library.subscribe;

import io.reactivex.subscribers.ResourceSubscriber;

/**
 * 用于在Http请求开始时，自动显示一个ProgressDialog
 * 在Http请求结束是，关闭ProgressDialog
 * 调用者自己对请求数据进行处理
 * Created by 孙华辉 on 16/3/10.
 */
public class HttpSubscriber<T> extends ResourceSubscriber<T> {

    private HttpCallback mSubscriberListener;

    public HttpSubscriber(HttpCallback mSubscriberListener) {
        this.mSubscriberListener = mSubscriberListener;
    }

    /**
     * 订阅开始时调用
     * 显示ProgressDialog
     */
    @Override
    public void onStart() {
        //showProgressDialog();
    }

    /**
     * 对错误进行统一处理
     * 隐藏ProgressDialog
     *
     * @param e
     */
    @Override
    public void onError(Throwable e) {
        if (mSubscriberListener != null) {
            mSubscriberListener.onError(e != null ? e.getMessage() : "程序开小差了!!");
        }
    }

    @Override
    public void onComplete() {
        if (mSubscriberListener != null) {
            mSubscriberListener.onCompleted();
        }
    }

    /**
     * 将onNext方法中的返回结果交给Activity或Fragment自己处理
     *
     * @param t 创建Subscriber时的泛型类型
     */
    @Override
    public void onNext(T t) {
        if (mSubscriberListener != null) {
            mSubscriberListener.onNext(t);
        }
    }
}