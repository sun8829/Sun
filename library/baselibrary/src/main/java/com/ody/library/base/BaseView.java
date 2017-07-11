package com.ody.library.base;

import com.trello.rxlifecycle2.LifecycleTransformer;

/**
 * Created by sunhuahui on 2017/7/11.
 */

public interface BaseView {
    <T> LifecycleTransformer<T> bindToLifecycle();
}
