package com.ody.library.retrofit;

import com.ody.library.commonbean.AdBean;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by Samuel on 2017/5/4.
 */

public abstract class BaseHttpClient {
    private static class BaseSingletonHolder {
        private static final BaseNetApi BASE_API = SingletonNet.getSingleton().getNetService(BaseNetApi.class);
    }

    public static Observable<AdBean> getAd() {
        Map<String, String> params = new HashMap<>();
        return BaseSingletonHolder.BASE_API.getAd(params);
    }
}
