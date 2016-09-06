package com.jihao.baselibrary.http.utils;

import com.jihao.baselibrary.http.OkHttpUtils;
import com.jihao.baselibrary.http.callback.Callback;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jiayonghua on 16/6/25.
 */
public class PostUtil {

    public static void post(String url,Map<String,String> map,Callback callback) {
        OkHttpUtils.post().url(url).params(map).build().execute(callback);
    }

    public static <K,V> HashMap<K,V> mapInstance() {
        return new HashMap<K,V>();
    }
}
