package com.jihao.baselibrary.utils;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by json on 16/7/5.
 */
public class ScreenUtil {

    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

}
