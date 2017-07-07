package com.ody.library.retrofit;

import com.ody.library.util.util.AppUtils;
import com.ody.library.util.util.LogUtils;
import com.ody.library.util.util.PhoneUtils;
import com.ody.library.util.util.SPUtils;
import com.ody.library.util.util.StringUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
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
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        Request.Builder builder = request.newBuilder();
                        if (request.body() == null || !request.body().contentType().subtype().equalsIgnoreCase("json")) {
                            return chain.proceed(builder.build());
                        }

                        //添加公共header
                        builder.addHeader("xxx", "xxxx");
                        builder.addHeader("TOKEN", "token");//token

                        //动态获取请求参数，并对请求参数加密放入header
                        RequestBody requestBody = request.body();
                        Buffer buffer = new Buffer();
                        requestBody.writeTo(buffer);
                        Charset charset = Charset.forName("UTF-8");

                        String paramsStr = buffer.readString(charset);
                        String sign = sign(paramsStr);
                        builder.addHeader("SIGN", sign);

                        return chain.proceed(builder.build());
                    }
                })
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Response response = chain.proceed(chain.request());
                        String timestamp = response.header("TIMESTAMP");
                        updateTimeStamp(timestamp);//记录本机时间与服务器时间差值
                        return response;
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

    //通过私钥对请求参数加密
    private String sign(String p) {
        return p;
    }

    private void updateTimeStamp(String timeStamp){

    }
}
