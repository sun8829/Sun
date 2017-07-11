package com.ody.ody.main;

import com.ody.library.commonbean.AdBean;
import com.ody.library.subscribe.HttpObserver;
import com.ody.library.subscribe.RxSchedulers;
import com.ody.ody.test.MainHttpClient;

/**
 * Created by sunhuahui on 2017/7/11.
 */

public class MainPresenter {
    private MainView mView;

    public MainPresenter(MainView view) {
        mView = view;
    }

    public void getAd() {
        MainHttpClient.get()
                .compose(RxSchedulers.<AdBean>compose())
                .compose(mView.<AdBean>bindToLifecycle())
                .subscribe(new HttpObserver<AdBean>() {

                    @Override
                    protected void success(AdBean bean) {
                        super.success(bean);
                    }

                });
    }

}
