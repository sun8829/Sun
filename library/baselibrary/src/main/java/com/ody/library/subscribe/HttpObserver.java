package com.ody.library.subscribe;

import android.content.Context;
import android.os.VibrationEffect;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by Samuel on 2017/5/4.
 */

public abstract class HttpObserver<T> implements Observer<T> {

    protected HttpObserver() {
    }

    @Override
    public final void onSubscribe(Disposable d) {
        start();
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

    protected void start() {

    }

    protected void success(T t) {
    }

    protected void error(String msg) {
    }

    protected void complete() {

    }
}
