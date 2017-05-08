package com.ody.library.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lcodecore.tkrefreshlayout.IHeaderView;
import com.lcodecore.tkrefreshlayout.OnAnimEndListener;
import com.ody.library.R;

/**
 * Created by Samuel on 2017/5/5.
 */

public class RefreshHeadView extends FrameLayout implements IHeaderView {
    private ImageView mRefreshImg;
    private TextView mRefreshTxt;
    private String pullDownStr = "下拉刷新";
    private String releaseRefreshStr = "释放刷新";
    private String refreshingStr = "正在刷新";

    public RefreshHeadView(Context context) {
        this(context, null);
    }

    public RefreshHeadView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshHeadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View rootView = View.inflate(getContext(), R.layout.refresh_head, null);
        mRefreshImg = (ImageView) rootView.findViewById(R.id.refresh_img);
        mRefreshTxt = (TextView) rootView.findViewById(R.id.refresh_txt);
        Glide.with(getContext()).load(R.drawable.loading).asGif().into(mRefreshImg);
        addView(rootView);
    }


    @Override
    public View getView() {
        return this;
    }

    @Override
    public void onPullingDown(float fraction, float maxHeadHeight, float headHeight) {
        if (fraction < 1f) mRefreshTxt.setText(pullDownStr);
        if (fraction > 1f) mRefreshTxt.setText(releaseRefreshStr);
    }

    @Override
    public void onPullReleasing(float fraction, float maxHeadHeight, float headHeight) {
        mRefreshImg.setVisibility(VISIBLE);
    }

    @Override
    public void startAnim(float maxHeadHeight, float headHeight) {
        mRefreshTxt.setText(refreshingStr);
        mRefreshImg.setVisibility(VISIBLE);
    }

    @Override
    public void onFinish(OnAnimEndListener animEndListener) {
        animEndListener.onAnimEnd();
    }

    @Override
    public void reset() {
        mRefreshImg.setVisibility(VISIBLE);
        mRefreshTxt.setText(pullDownStr);
    }
}
