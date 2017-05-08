package com.ody.library.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Samuel on 2017/5/4.
 */

public abstract class BaseFragment extends Fragment {
    protected Context mContext;

    protected void preCreate() {

    }

    protected abstract int bindLayout();


    /**
     * 初始化View
     */
    protected void initView(View view) {

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

    @Override
    public void onAttach(Context context) {
        mContext = context;
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        preCreate();
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(bindLayout(), container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initView(view);
        initData();
        initListener();
        super.onViewCreated(view, savedInstanceState);
    }
}
