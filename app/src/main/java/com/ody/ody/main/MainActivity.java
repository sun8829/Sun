package com.ody.ody.main;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ody.library.base.BaseActivity;
import com.ody.library.commonbean.AdBean;
import com.ody.library.subscribe.HttpObserver;
import com.ody.library.subscribe.RxSchedulers;
import com.ody.library.util.util.GlideUtil;
import com.ody.library.util.util.JumpUtils;
import com.ody.ody.R;
import com.ody.ody.test.MainHttpClient;
import com.ody.photopicker.PhotoPicker;
import com.ody.photopicker.loader.ImageLoader;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private TextView mJumpTxt;

    @Override
    protected int bindLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        mJumpTxt = findViewFromId(R.id.jump1);

    }

    @Override
    protected void initData() {
        super.initData();
        MainHttpClient.get()
                .compose(RxSchedulers.<AdBean>compose())
                .compose(this.<AdBean>bindToLifecycle())
                .subscribe(new HttpObserver<AdBean>(mContext) {

                    @Override
                    protected void success(AdBean bean) {
                        super.success(bean);
                    }

                });

    }

    @Override
    protected void initListener() {
        super.initListener();
        mJumpTxt.setOnClickListener(this);
        findViewFromId(R.id.select).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.jump1:
                JumpUtils.open(this, JumpUtils.LOGIN_URL);
                break;
            case R.id.select:
                PhotoPicker.builder()
                        .setGridColumnCount(2)
                        .setPhotoCount(9)
                        .setImageLoader(new ImageLoader() {
                            @Override
                            public void displayImage(ImageView imageView, String path) {
                                Glide.with(imageView.getContext()).load(path).thumbnail(0.3f).into(imageView);
                            }

                            @Override
                            public void displayImage(ImageView imageView, Uri uri) {
                                Glide
                                        .with(imageView.getContext()).load(uri)
                                        .thumbnail(0.1f)
                                        .dontAnimate()
                                        .dontTransform()
                                        .override(800, 800)
//                                        .placeholder(R.drawable.__picker_ic_photo_black_48dp)
//                                        .error(R.drawable.__picker_ic_broken_image_black_48dp)
                                        .into(imageView);
                            }

                            @Override
                            public void clear(View view) {
                                GlideUtil.clear(view);
                            }
                        })
                        .start(MainActivity.this);
                break;
        }

    }
}
