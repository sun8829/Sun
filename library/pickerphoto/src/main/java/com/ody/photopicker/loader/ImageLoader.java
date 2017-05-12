package com.ody.photopicker.loader;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Samuel on 2017/5/10.
 */

public interface ImageLoader {
    void displayImage(ImageView imageView, String path);

    void displayImage(ImageView imageView, Uri uri);

    void clear(View imageView);
}
