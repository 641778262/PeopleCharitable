package com.jihao.baselibrary.utils;

import android.content.Context;

import com.umeng.analytics.MobclickAgent;

/**
 * Created by jiayonghua on 16/6/16.
 * 友盟统计
 */
public class UmentUtil {
    /**
     * 进入界面统计
     */
    public static void onResume(Context context, Class clazz) {
        MobclickAgent.onResume(context);
        MobclickAgent.onPageStart(clazz.getSimpleName());
    }

    /**
     * 离开界面统计
     */
    public static void onPause(Context context,Class clazz) {
        MobclickAgent.onPause(context);
        MobclickAgent.onPageEnd(clazz.getSimpleName());
    }

    /**
     * 事件统计
     * @param context
     * @param eventCode
     */
    public static void statistic(Context context,String eventCode) {
        MobclickAgent.onEvent(context,eventCode);
    }

}
