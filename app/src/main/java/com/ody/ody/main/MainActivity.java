package com.ody.ody.main;

import android.view.View;
import android.widget.TextView;

import com.ody.library.base.BaseActivity;
import com.ody.library.commonbean.AdBean;
import com.ody.library.subscribe.HttpObserver;
import com.ody.library.subscribe.RxSchedulers;
import com.ody.library.util.util.JumpUtils;
import com.ody.ody.R;
import com.ody.ody.test.MainHttpClient;

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
        findViewFromId(R.id.jump2).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.jump1:
                JumpUtils.open(this, JumpUtils.LOGIN_URL);
                break;
        }

    }
}
