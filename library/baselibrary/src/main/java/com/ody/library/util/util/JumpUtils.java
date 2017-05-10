package com.ody.library.util.util;

import android.content.Context;

import com.github.mzule.activityrouter.router.RouterCallback;
import com.github.mzule.activityrouter.router.Routers;
import com.ody.library.BuildConfig;

/**
 * Created by Samuel on 2017/5/8.
 */

public class JumpUtils {
    public final static String LOGIN_URL = "login";

    public static void open(Context context, String url) {
        Routers.open(context, BuildConfig.SCHEME + "://" + url);
    }

    public static void open(Context context, String url, RouterCallback callback) {
        Routers.open(context, BuildConfig.SCHEME + "://" + url, callback);
    }
}
