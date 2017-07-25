package com.ody.library.subscribe;

import com.ody.library.base.BaseBean;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Samuel on 2017/5/4.
 */

public class RxSchedulers {
    public static <T> ObservableTransformer<T, T> compose() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> observable) {
                return observable
                        .filter(new Predicate<T>() {
                            @Override
                            public boolean test(T t) throws Exception {
                                if (t instanceof BaseBean) {
                                    BaseBean bean = (BaseBean) t;
                                    if (bean.getCode().equals("0")) {
                                        return true;
                                    }
                                }
                                return false;
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
}
