package com.ody.ody.test;

import com.ody.library.commonbean.AdBean;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by Samuel on 2017/5/4.
 */

public interface MainApi {
    @GET("/api/dolphin/list?&platform=3&platformId=0&pageCode=APP_HOME&adCode=ad_banner&areaCode=310115")
    Observable<AdBean> get(@QueryMap Map<String, String> params);
}
