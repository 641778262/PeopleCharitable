package com.jihao.baselibrary.utils;

import android.util.Log;

import com.jihao.baselibrary.http.OkHttpUtils;
import com.jihao.baselibrary.http.callback.StringCallback;

import java.util.HashMap;

/**
 * Created by pandayu on 16/6/1.
 */
public class LuncherNet {
    private static LuncherNet instance = new LuncherNet();
    private LuncherNet() {}

    public static LuncherNet getInstance() {
        return instance;
    }

    public void luncher() {
        OkHttpUtils.post().url("/device").params(new HashMap<String, String>())
                .build().execute(new StringCallback() {
            @Override
            public void onResponse(String response) {
                Log.e("Test","device response: " + response);
            }
        });
    }

}
