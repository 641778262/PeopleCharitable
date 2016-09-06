package com.jihao.baselibrary.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**加载图片
 * Created by q on 2016/7/12.
 */
public class ImageLoader {

    private ImageLoader() {

    }
    private static class SingletonHolder {
        private static final ImageLoader IMAGE_LOADER = new ImageLoader();
    }

    public static ImageLoader getInstance() {
        return SingletonHolder.IMAGE_LOADER;
    }

    public void loadImage(Context mContext, String url, ImageView mImageView){
        Glide.with(mContext).load(url).into(mImageView);
    }

  /**
   *
   * @param mContext
   * @param url imageurl
   * @param defaultUrlId default image id
   * @param mImageView
   */
    public void loadImage(Context mContext, String url, int defaultUrlId, ImageView mImageView){
        Glide.with(mContext).load(url).placeholder(defaultUrlId).into(mImageView);
    }
}
