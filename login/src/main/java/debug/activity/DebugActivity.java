package debug.activity;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.ody.library.base.BaseActivity;
import com.ody.library.views.RefreshHeadView;
import com.ody.login.R;

public class DebugActivity extends BaseActivity {
    private TwinklingRefreshLayout mRefreshLayout;
    private RecyclerView mProductRv;

    @Override
    protected int bindLayout() {
        return R.layout.activity_debug;
    }

    @Override
    protected void initView() {
        super.initView();
        mRefreshLayout = findViewFromId(R.id.refreshLayout);
        mRefreshLayout.setHeaderView(new RefreshHeadView(this));
        mRefreshLayout.setEnableLoadmore(false);

    }

    @Override
    protected void initListener() {
        super.initListener();
        mRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshLayout.finishRefreshing();
                    }
                }, 5000);
                super.onRefresh(refreshLayout);
            }
        });
    }
}
