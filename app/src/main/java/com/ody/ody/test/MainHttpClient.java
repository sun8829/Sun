package com.ody.ody.test;

import com.ody.library.commonbean.AdBean;
import com.ody.library.retrofit.BaseHttpClient;
import com.ody.library.retrofit.SingletonNet;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by Samuel on 2017/5/4.
 */

public class MainHttpClient extends BaseHttpClient {

    private static class SingletonHolder {
        private static final MainApi API = SingletonNet.getSingleton().getNetService(MainApi.class);
    }

    public static Observable<AdBean> get() {
        Map<String, String> params = new HashMap<>();
        return SingletonHolder.API.get(params);
    }
}
