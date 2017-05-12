package com.ody.library.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.view.View;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

/**
 * Created by Samuel on 2017/5/3.
 */

public abstract class BaseActivity extends RxAppCompatActivity {

    protected Context mContext;

    /**
     * 初始化
     */
    protected void preCreate() {

    }

    /**
     * 绑定布局文件
     *
     * @return
     */
    protected abstract int bindLayout();


    /**
     * 初始化View
     */
    protected void initView() {

    }

    /**
     * 数据初始化
     */
    protected void initData() {

    }

    /**
     * 添加监听
     */
    protected void initListener() {
    }

    public <T extends View> T findViewFromId(@IdRes int id) {
        return (T) findViewById(id);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        preCreate();

        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(bindLayout());
        initView();
        initData();
        initListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
