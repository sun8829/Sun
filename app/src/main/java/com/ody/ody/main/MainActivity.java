package com.ody.ody.main;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.ody.share.ShareHelper;

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
        ShareHelper.init(this, "1dcb3e19d1c45");
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
        findViewFromId(R.id.share).setOnClickListener(this);
        findViewFromId(R.id.hybrid).setOnClickListener(this);
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
            case R.id.share:
                ShareHelper.builder(ShareHelper.QQ_NAME)
                        .setTitle("测试分享的标题")
                        .setTitleUrl("http://sharesdk.cn")
                        .setText("测试分享的文本")
                        .setImageUrl("http://www.someserver.com/测试图片网络地址.jpg")
                        .setSite("发布分享的网站名称")
                        .setSiteUrl("发布分享网站的地址")
                        .setShareListener(new ShareHelper.ShareListener() {
                            @Override
                            public void onError() {

                            }

                            @Override
                            public void onComplete() {
                                Toast.makeText(mContext, "success", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onCancel() {
                                Toast.makeText(mContext, "cancel", Toast.LENGTH_LONG).show();
                            }
                        })
                        .share();
                break;
            case R.id.hybrid:
                startActivity(new Intent(MainActivity.this, WebActivity.class));
                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
