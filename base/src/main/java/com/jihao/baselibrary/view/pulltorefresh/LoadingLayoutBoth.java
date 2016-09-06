package com.jihao.baselibrary.view.pulltorefresh;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jihao.baselibrary.R;

import java.text.SimpleDateFormat;
import java.util.Date;


public class LoadingLayoutBoth extends FrameLayout {

    private ImageView refreshImage;
    private TextView refreshText;
    private TextView timeText = null;
    private String pullLabel;
    private String refreshingLabel;
    private String releaseLabel;
    //    private AnimationDrawable mAnimationDrawable;
    private ProgressBar refreshProgress;

    public LoadingLayoutBoth(Context context, final int mode, String releaseLabel, String
            pullLabel, String refreshingLabel) {
        super(context);
        getView(context, mode);
        this.releaseLabel = releaseLabel;
        this.pullLabel = pullLabel;
        this.refreshingLabel = refreshingLabel;

    }

    protected ViewGroup getView(Context context, int mode) {
//        mAnimationDrawable = (AnimationDrawable)getResources().getDrawable(R.drawable
// .loading_header);
        ViewGroup header = null;

        switch (mode) {
            case PullToRefreshBase.MODE_PULL_UP_TO_REFRESH:
                header = (ViewGroup) LayoutInflater.from(context).inflate(R.layout
                        .base_pull_to_refresh_footer, this);
                refreshText = (TextView) header.findViewById(R.id.pull_to_refresh_text_list_footer);
                refreshImage = (ImageView) header.findViewById(R.id
                        .pull_to_refresh_image_list_footer);
                refreshProgress = (ProgressBar) header.findViewById(R.id
                        .pull_to_refresh_progress_list_footer);
                timeText = (TextView) header.findViewById(R.id.pull_to_refresh_time_list_footer);
                refreshImage.setImageResource(R.drawable.pulltorefresh_down_arrow);
                break;
            case PullToRefreshBase.MODE_PULL_DOWN_TO_REFRESH:
            default:
                header = (ViewGroup) LayoutInflater.from(context).inflate(R.layout
                        .base_pull_to_refresh_header, this);
                refreshProgress = (ProgressBar) header.findViewById(R.id.pull_to_refresh_progress_list);
                refreshText = (TextView) header.findViewById(R.id.pull_to_refresh_text_list);
                refreshImage = (ImageView) header.findViewById(R.id.pull_to_refresh_image_list);
                timeText = (TextView) header.findViewById(R.id.pull_to_refresh_time_list);
                refreshImage.setImageResource(R.drawable.pulltorefresh_down_arrow);
                break;
        }
        return header;
    }

    public void reset(boolean isShowUpdateTime) {
        refreshText.setText(pullLabel);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        if (isShowUpdateTime) {
            timeText.setText("最后更新：" + sdf.format(new Date(System.currentTimeMillis())));
            timeText.setVisibility(View.VISIBLE);
        } else {
            timeText.setVisibility(View.GONE);
        }
//        refreshImage.clearAnimation();
        refreshImage.setImageResource(R.drawable.pulltorefresh_down_arrow);
        refreshImage.setVisibility(View.VISIBLE);
        refreshProgress.setVisibility(View.GONE);
//        if(mAnimationDrawable != null && mAnimationDrawable.isRunning()) {
//            mAnimationDrawable.stop();
//        }
    }

    public void releaseToRefresh(int scrollHeight, int headerHeight) {
        int height = Math.abs(scrollHeight);
        if (height >= headerHeight / 2 && height <= headerHeight * 2 / 3) {
            System.out.println("change image");
        }
    }

    public void releaseToRefresh() {
        refreshText.setText(releaseLabel);
//        refreshImage.clearAnimation();
        refreshImage.setImageResource(R.drawable.pulltorefresh_up_arrow);
//        if(mAnimationDrawable != null && mAnimationDrawable.isRunning()) {
//            mAnimationDrawable.stop();
//        }
    }


    public void setPullLabel(String pullLabel) {
        this.pullLabel = pullLabel;
    }

    public void refreshing() {
        refreshText.setText(refreshingLabel);
        refreshImage.setVisibility(View.GONE);
        refreshProgress.setVisibility(View.VISIBLE);
//        refreshImage.setImageResource(R.drawable.loading_header);
//        mAnimationDrawable = (AnimationDrawable) refreshImage.getDrawable();
//        if(!mAnimationDrawable.isRunning()) {
//            refreshImage.post(new Runnable() {
//                @Override
//                public void run() {
//                    mAnimationDrawable.start();
//                }
//            });
//        }
    }

    public void setRefreshingLabel(String refreshingLabel) {
        this.refreshingLabel = refreshingLabel;
    }

    public void setReleaseLabel(String releaseLabel) {
        this.releaseLabel = releaseLabel;
    }

    public void pullToRefresh() {
        refreshText.setText(pullLabel);
//        refreshImage.clearAnimation();
        refreshImage.setImageResource(R.drawable.pulltorefresh_down_arrow);
//        if(mAnimationDrawable != null && mAnimationDrawable.isRunning()) {
//            mAnimationDrawable.stop();
//        }
    }


    public void setTextColor(int color) {
        refreshText.setTextColor(color);
    }

}
