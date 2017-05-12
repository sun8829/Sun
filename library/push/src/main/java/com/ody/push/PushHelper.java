package com.ody.push;

import android.content.Context;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Samuel on 2017/5/11.
 */

public class PushHelper {
    public static void init(Context context, boolean isDebug) {
        JPushInterface.setDebugMode(isDebug);
        JPushInterface.init(context);
    }
}
