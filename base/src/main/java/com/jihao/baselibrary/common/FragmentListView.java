package com.jihao.baselibrary.common;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jihao.baselibrary.R;
import com.jihao.baselibrary.http.OkHttpUtils;
import com.jihao.baselibrary.http.builder.PostFormBuilder;
import com.jihao.baselibrary.http.callback.Callback;
import com.jihao.baselibrary.utils.NetworkUtils;
import com.jihao.baselibrary.utils.ToastUtil;
import com.jihao.baselibrary.view.loading.LoadingHelper;
import com.jihao.baselibrary.view.pulltorefresh.PullToRefreshBase;
import com.jihao.baselibrary.view.pulltorefresh.PullToRefreshListView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by json on 15/7/21.
 * 带ListView的Fragment
 */
public abstract class FragmentListView<T, V extends List<T>> extends Fragment implements
        AdapterView.OnItemClickListener, PullToRefreshBase.OnRefreshListener, AbsListView
        .OnScrollListener, AdapterView.OnItemLongClickListener {

    public static final String LIMIT = "limit";
    public static final String SKIP = "skip";

    public static final int MODE_PULL_TO_REFRESH_ONLY = -1;//上拉不加载

    protected Activity mActivity;
    protected PullToRefreshListView mPullToRefreshListView;
    protected LinearLayout mContentLayout;
    protected ListView mListView;
    protected List<T> mList = new ArrayList<>();
    protected BaseAdapter mAdapter;
    protected LoadingHelper mLoadingHelper;
    protected int mCurrentPage;
    protected View footerView;
    protected View mFooterTipsView;
    protected View mLoadingView;
    protected View mRetryView;
    protected boolean hasAddFooterView;
    public static final int COUNT_PER_PAGE = 20;
    protected boolean isLast = false;//是否是最后一页
    protected boolean hasCachedData;
    protected boolean isLoading;
    protected boolean hasToastData;
    protected boolean showFooterTips;
    protected boolean hasFooterTipsView;
    protected boolean shouldFirstLoad = true;
    protected int mode;

    public FragmentListView() {

    }


    protected Callback<V> mCallback = new Callback<V>() {
        @Override
        public V parseNetworkResponse(Response response) throws Exception {
            return handleResponse(response, getTypeToken());
        }

        @Override
        public void onResponse(V bean) {
            isLoading = false;
            if (mPullToRefreshListView != null) {
                mPullToRefreshListView.onRefreshComplete();
            }
            if (mCurrentPage == 0) {
                mList.clear();
            }
            if (bean != null) {
                if (bean.size() > 0) {
                    mList.addAll(bean);
                    if (bean.size() < COUNT_PER_PAGE) {
                        addFooterTipsView();
                        removeLoadingFooterView();
                        isLast = true;
                    } else {
                        isLast = false;
                        removeFooterTipsView();
                        addLoadingFooterView();
                    }
                } else if (bean.size() == 0) {
                    removeLoadingFooterView();
                    isLast = true;
                }
            }
            dataProcessing();
            mAdapter.notifyDataSetChanged();
            if (mLoadingHelper != null && !mLoadingHelper.isContentViewVisible()) {
                mLoadingHelper.showContentView();
            }
//            if(mCurrentPage == 0) {
//                removeLoadingFooterView();
//            } else {
//                try {
//                    if(mListView.getChildCount() > mListView.getHeaderViewsCount()) {
//                        int height = mListView.getChildAt(mListView.getHeaderViewsCount()).getHeight();
//                        if(height * mList.size() < ScreenUtil.getScreenHeight(mActivity)) {
//                            removeLoadingFooterView();
//                        }
//                    }
//                }catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
            if (mode != MODE_PULL_TO_REFRESH_ONLY) {
                mCurrentPage++;
            }
        }

        @Override
        public void onError(Call call, Exception e) {
            super.onError(call, e);
            isLoading = false;
            if(mList.isEmpty()) {
                removeLoadingFooterView();
            } else {
                switchToRetryState();
            }
            if (mPullToRefreshListView != null) {
                mPullToRefreshListView.onRefreshComplete();
            }
            if (mCurrentPage == 0 && (mList == null || mList.size() == 0)) {
                if (mLoadingHelper != null) {
                    mLoadingHelper.showRetryView();
                }
            }
            if (null != mLoadingHelper && !mLoadingHelper.isRetryViewVisible()) {
                if (NetworkUtils.NETWORK_TYPE == NetworkUtils.NETWORK_NONE) {
                    ToastUtil.showToast(mActivity, R.string.net_error);
                }
            }
        }
    };


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity_listview, container, false);
        mContentLayout = (LinearLayout) view.findViewById(R.id.ll_content);
        mPullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.list_view);
        footerView = inflater.inflate(R.layout.view_footer_loading_listview,
                null);
        mLoadingView = footerView.findViewById(R.id.pulldown_loading);
        mRetryView = footerView.findViewById(R.id.pulldown_footer_retry);
        mRetryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadPageData(false,true);
                switchToLoadingState();
            }
        });
        mPullToRefreshListView.setPullToRefreshEnabled(true);
        mPullToRefreshListView
                .setMode(PullToRefreshListView.MODE_PULL_DOWN_TO_REFRESH);
        mPullToRefreshListView.setOnRefreshListener(this);
        mListView = mPullToRefreshListView.getRefreshableView();
        mListView.setDivider(null);
        mListView.setOnItemClickListener(this);
        mListView.setOnScrollListener(this);
        mListView.setOnItemLongClickListener(this);

        addTopView();
        setRefreshMode();
        setEmptyView();
        addLoadingFooterView();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        initAdapter();
        mListView.setAdapter(mAdapter);
        initCachedList();
        if (mList != null && mList.size() > 0) {
            removeLoadingFooterView();
            hasCachedData = true;
            mAdapter.notifyDataSetChanged();
        }
        if (shouldFirstLoad) {
            initFirstLoad();
        }
        super.onActivityCreated(savedInstanceState);
    }

    public void initFirstLoad() {
        if (hasCachedData) {// 有缓存
            mPullToRefreshListView.setRefreshing();
            loadPageData(true, true);
        } else {
            loadPageData(true, false);
        }
    }

    public void loadPageData(final boolean first, boolean hasCache) {
        if (first) {
            mCurrentPage = 0;
            hasToastData = false;
        }
        if (hasCache) {
            loadData();
        } else {
            initLoadingHelper();
            loadData();
        }
    }

    private void initLoadingHelper() {
        if (mLoadingHelper == null) {
            mLoadingHelper = new LoadingHelper(new LoadingHelper.OnClickRetryListener() {
                @Override
                public void onClickRetry() {
                    loadData();
                }
            });
            setCheckShouldLoad();
            mLoadingHelper.onCreateView(mActivity, mPullToRefreshListView);
        }
        mLoadingHelper.showLoadingView();
    }

    protected void setCheckShouldLoad() {

    }

    protected abstract void initAdapter();

    /**
     * 未登录的时候不去请求数据
     */
    protected void loadData() {
        loadDataFromNet();
    }

    protected void addTopView() {

    }


    protected abstract void loadDataFromNet();

    protected void initCachedList() {

    }

    protected void dataProcessing() {

    }

    protected void setEmptyView() {
        View emptyView = View.inflate(mActivity, R.layout.listview_empty_view, null);
        mListView.setEmptyView(emptyView);

    }

    protected void onClickEmptyBtn() {

    }

    protected void setEmptyData(ImageView emptyImage, TextView emptyTx, TextView tipsTx,
                                         Button emptyBtn){

    }

    protected void onItemClick(int position) {
    }

    protected void onItemLongClick(int position) {
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        position = position - (mListView.getHeaderViewsCount());
        if (position< 0 || position >= mList.size()) {
            return;
        }
        onItemClick(position);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
        position = position - (mListView.getHeaderViewsCount());
        if (position< 0 || position >= mList.size()) {
            return true;
        }
        onItemLongClick(position);
        return true;
    }

    /**
     * 设置下拉刷新模式
     */
    protected void setRefreshMode() {
    }

    @Override
    public void onRefresh() {
        if (mPullToRefreshListView.hasPullFromTop()) {// 下拉刷新
            if (!isLoading) {
                loadPageData(true, true);
            }
        }
    }

    public void addLoadingFooterView() {
        if (!hasAddFooterView && mode != MODE_PULL_TO_REFRESH_ONLY) {
            mListView.addFooterView(footerView);
            hasAddFooterView = true;
        }
    }

    public void removeLoadingFooterView() {
        if (hasAddFooterView) {
            mListView.removeFooterView(footerView);
            hasAddFooterView = false;
        }
    }


    protected void onClickFooterTipsBtn() {

    }

    protected void setEmptyFooterData(TextView addTips, Button add) {

    }

    public void addFooterTipsView() {
        if (!hasFooterTipsView && showFooterTips) {
            mListView.addFooterView(mFooterTipsView);
            hasFooterTipsView = true;
        }
    }

    public void removeFooterTipsView() {
        if (hasFooterTipsView && showFooterTips) {
            mListView.removeFooterView(mFooterTipsView);
            hasFooterTipsView = false;
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (view.getLastVisiblePosition() == view.getCount() - 1 && isLast
                && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            if (!hasToastData && mCurrentPage > 1) {
                ToastUtil.showToast(mActivity, R.string.no_more_data);
                hasToastData = true;
            }
            return;
        }
        if (view.getLastVisiblePosition() == view.getCount() - 1 && !isLoading
                && !isLast && !mPullToRefreshListView.isRefreshing() && mode !=
                MODE_PULL_TO_REFRESH_ONLY) {
            if (NetworkUtils.NETWORK_TYPE == NetworkUtils.NETWORK_NONE) {
                ToastUtil.showToast(mActivity, R.string.net_error);
//                removeLoadingFooterView();
                addLoadingFooterView();
                switchToRetryState();
                return;
            }
            addLoadingFooterView();
            switchToLoadingState();
            if (mCurrentPage == 0) {
                isLoading = true;
                loadPageData(true, true);
            } else {
                isLoading = true;
                loadPageData(false, true);
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {


    }

    public void setShouldFirstLoad(boolean shouldFirstLoad) {
        this.shouldFirstLoad = shouldFirstLoad;
    }

    public void loadDataByUrl(String url, HashMap<String,String> params) {
        PostFormBuilder builder = OkHttpUtils.post().url(url)
                .addParams(LIMIT,String.valueOf(COUNT_PER_PAGE))
                .addParams(SKIP,String.valueOf(mCurrentPage * COUNT_PER_PAGE));
        if(params != null && !params.isEmpty()) {
            for(String key : params.keySet()) {
                builder.addParams(key,params.get(key));
            }
        }

        builder.build().execute(mCallback);

    }

    public abstract Type getTypeToken();

    protected void switchToLoadingState() {
        mLoadingView.setVisibility(View.VISIBLE);
        mRetryView.setVisibility(View.GONE);
    }

    protected void switchToRetryState() {
        mLoadingView.setVisibility(View.GONE);
        mRetryView.setVisibility(View.VISIBLE);
    }

}
