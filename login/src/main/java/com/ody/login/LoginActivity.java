package com.ody.login;

import android.widget.Button;

import com.github.mzule.activityrouter.annotation.Router;
import com.ody.library.base.BaseActivity;
import com.ody.library.util.util.JumpUtils;

@Router(JumpUtils.LOGIN_URL)
public class LoginActivity extends BaseActivity {
    private Button mButton;

    @Override
    protected int bindLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        super.initView();
        mButton = findViewFromId(R.id.button);
    }
}
