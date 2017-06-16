package com.ody.ody.main;

import com.ody.bridge.fragment.SuperWebFragment;
import com.ody.library.base.BaseActivity;
import com.ody.ody.R;

/**
 * Created by Samuel on 2017/5/23.
 */

public class WebActivity extends BaseActivity {
    SuperWebFragment mFragment;

    @Override
    protected int bindLayout() {
        return R.layout.activity_web;
    }

    @Override
    protected void initView() {
        super.initView();
        mFragment = SuperWebFragment.newInstance("file:///android_asset/demo.html");
        getSupportFragmentManager().beginTransaction().replace(R.id.container, mFragment).commit();
    }

    @Override
    public void onBackPressed() {
        if (!mFragment.goBack()) {
            super.onBackPressed();
        }
    }
}
