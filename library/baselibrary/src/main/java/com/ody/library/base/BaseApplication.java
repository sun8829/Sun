package com.ody.library.base;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.os.StrictMode;

import com.github.mzule.activityrouter.router.RouterCallback;
import com.github.mzule.activityrouter.router.RouterCallbackProvider;
import com.github.mzule.activityrouter.router.SimpleRouterCallback;
import com.ody.library.BuildConfig;
import com.ody.library.util.util.LogUtils;
import com.ody.library.util.util.Utils;
import com.ody.push.PushHelper;

/**
 * Created by Samuel on 2017/5/4.
 */

public class BaseApplication extends Application implements RouterCallbackProvider {

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);

        //推送初始化
//        PushHelper.init(this, BuildConfig.IS_DEBUG);

        LogUtils.Builder logBuilder = new LogUtils.Builder();
        if (BuildConfig.IS_DEBUG) {
            logBuilder.setLogSwitch(true);
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .penaltyDialog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
//                    .penaltyDeath()
                    .build());
        } else {
            logBuilder.setLogSwitch(true);
        }
    }

    @Override
    public RouterCallback provideRouterCallback() {
        return new SimpleRouterCallback() {
            @Override
            public boolean beforeOpen(Context context, Uri uri) {
                // 是否拦截，true 拦截，false 不拦截
                return false;
            }

            @Override
            public void afterOpen(Context context, Uri uri) {
            }

            @Override
            public void notFound(Context context, Uri uri) {
            }

            @Override
            public void error(Context context, Uri uri, Throwable e) {
            }
        };
    }
}
