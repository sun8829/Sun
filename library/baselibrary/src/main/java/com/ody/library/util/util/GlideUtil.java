package com.ody.library.util.util;

import android.app.Activity;
import android.app.Fragment;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by Samuel on 2017/5/4.
 */

public class GlideUtil {
    private final static String TAG = "GlideUtil";

    public static <T> void display(T context, ImageView imgView, String url) {
        if (context instanceof Activity) {
            Glide.with((Activity) context).load(url).into(imgView);
        } else if (context instanceof Fragment) {
            Glide.with((Fragment) context).load(url).into(imgView);
        } else if (context instanceof android.support.v4.app.Fragment) {
            Glide.with((android.support.v4.app.Fragment) context).load(url).into(imgView);
        } else {
            LogUtils.e(TAG, "context disable（Activity or Fragment）");
        }
    }

    public static <T> void display(T context, ImageView imgView, int resImg) {
        if (context instanceof Activity) {
            Glide.with((Activity) context).load(resImg).into(imgView);
        } else if (context instanceof Fragment) {
            Glide.with((Fragment) context).load(resImg).into(imgView);
        } else if (context instanceof android.support.v4.app.Fragment) {
            Glide.with((android.support.v4.app.Fragment) context).load(resImg).into(imgView);
        } else {
            LogUtils.e(TAG, "context disable（Activity or Fragment）");
        }
    }

    public static <T> void display(T context, ImageView imgView, String url, int placeholderId) {
        if (context instanceof Activity) {
            if (placeholderId > 0) {
                Glide.with((Activity) context).load(url).asBitmap().atMost().placeholder(placeholderId).into(imgView);
            } else {
                Glide.with((Activity) context).load(url).into(imgView);
            }
        } else if (context instanceof Fragment) {
            Glide.with((Fragment) context).load(url).into(imgView);
        } else if (context instanceof android.support.v4.app.Fragment) {
            Glide.with((android.support.v4.app.Fragment) context).load(url).into(imgView);
        } else {
            LogUtils.e(TAG, "context disable（Activity or Fragment）");
        }

    }

    public static void clear(View view) {
        Glide.clear(view);
    }
}
