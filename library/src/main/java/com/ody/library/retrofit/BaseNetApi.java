package com.ody.library.retrofit;

import com.ody.library.commonbean.AdBean;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by Administrator on 2016/11/30.
 */

public interface BaseNetApi {
    @GET("/api/dolphin/list?&platform=3&platformId=0&pageCode=APP_HOME&adCode=ad_banner&areaCode=310115")
    Observable<AdBean> getAd(@QueryMap Map<String, String> params);
}