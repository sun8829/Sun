package com.ody.library.retrofit;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2016/11/30.
 */

public class SingletonNet {
    public static final String BASE_URL = "http://api.laiyifen.com";
    private static final int DEFAULT_TIMEOUT = 30;
    private Retrofit retrofit;
    private volatile static SingletonNet INSTANCE = null;

    private SingletonNet() {
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder
                //添加公共header
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request.Builder builder = chain.request().newBuilder();
                        builder.addHeader("token", "123");
                        return chain.proceed(builder.build());
                    }
                })
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        Retrofit.Builder b = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASE_URL);

        retrofit = b.build();
    }

    public static SingletonNet getSingleton() {
        if (INSTANCE == null) {
            synchronized (SingletonNet.class) {
                if (INSTANCE == null) {
                    INSTANCE = new SingletonNet();
                }
            }
        }
        return INSTANCE;
    }

    public <T> T getNetService(Class<T> t) {
        return retrofit.create(t);
    }

}
