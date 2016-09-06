package com.jihao.baselibrary.view.loading;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jihao.baselibrary.R;
import com.jihao.baselibrary.utils.NetworkUtils;
import com.jihao.baselibrary.utils.ToastUtil;


public class LoadingHelper {

    private View mContentView;
    private LinearLayout mLoadingLayout;
    private LinearLayout mErrorLayout;
    private TextView mErrorTx;
    private OnClickRetryListener mListener;
    private View mLoadingView;

    public LoadingHelper(OnClickRetryListener listener) {
        mListener = listener;
    }

    public void onCreateView(Context context, View contentView) {
        ViewGroup parent = (ViewGroup) contentView.getParent();
        addViews(LayoutInflater.from(context), parent, parent.indexOfChild(contentView));
    }

    private void addViews(LayoutInflater inflater, ViewGroup parent, int position) {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mContentView = parent.getChildAt(position);
        mLoadingView = inflater.inflate(R.layout.loading_layout, null, false);
        mLoadingLayout = (LinearLayout) mLoadingView.findViewById(R.id.ll_loading);
        mErrorLayout = (LinearLayout) mLoadingView.findViewById(R.id.ll_error);
        mErrorTx = (TextView) mLoadingView.findViewById(R.id.tv_error);
        mErrorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    if (NetworkUtils.NETWORK_TYPE == NetworkUtils.NETWORK_NONE) {
                        mErrorTx.setText(R.string.net_error);
                        ToastUtil.showToast(mContentView.getContext(), R.string.net_error);
                        return;
                    }
                    showLoadingView();
                    mListener.onClickRetry();
                }

            }
        });
        parent.addView(mLoadingView, position, params);
        showContentView();
    }

    public boolean isContentViewVisible() {
        return mContentView.getVisibility() == View.VISIBLE;
    }


    public void showContentView() {
        mContentView.setVisibility(View.VISIBLE);
        mLoadingView.setVisibility(View.GONE);
    }

    public void showLoadingView() {
        mContentView.setVisibility(View.GONE);
        mLoadingView.setVisibility(View.VISIBLE);
        mLoadingLayout.setVisibility(View.VISIBLE);
        mErrorLayout.setVisibility(View.GONE);
    }

    public boolean isRetryViewVisible() {
        return mErrorLayout.getVisibility() == View.VISIBLE;
    }

    public void showRetryView() {
        mContentView.setVisibility(View.GONE);
        mLoadingView.setVisibility(View.VISIBLE);
        mLoadingLayout.setVisibility(View.GONE);
        mErrorLayout.setVisibility(View.VISIBLE);
        if (NetworkUtils.NETWORK_TYPE == NetworkUtils.NETWORK_NONE) {
            mErrorTx.setText(R.string.net_error);
        } else {
            mErrorTx.setText(R.string.loading_data_error);
        }
    }

    public interface OnClickRetryListener {
        void onClickRetry();
    }

}

