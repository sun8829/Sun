package com.ody.library.subscribe;

import android.content.Context;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by Samuel on 2017/5/4.
 */

public abstract class HttpObserver<T> implements Observer<T> {
    private Context mContext;

    protected HttpObserver(Context context) {
        mContext = context.getApplicationContext();
    }

    @Override
    public final void onSubscribe(Disposable d) {

    }

    @Override
    public final void onNext(T value) {
        success(value);
    }

    @Override
    public final void onError(Throwable e) {
        error(e.toString());
    }

    @Override
    public final void onComplete() {
        complete();
    }


    protected void success(T t) {
    }

    protected void error(String msg) {
    }

    protected void complete() {

    }
}
