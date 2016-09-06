package com.jihao.baselibrary.utils;

import android.util.Base64;

import java.io.UnsupportedEncodingException;

/**
 * Created by blue on 16/7/22.
 */

public class StringUtils {

    /**
     * BASE64 解密
     * @param str
     * @return
     */
    public static  String decryptBASE64(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }
        try {
            byte[] encode = str.getBytes("UTF-8");
            // base64 解密
            return new String(Base64.decode(encode, 0, encode.length, Base64.DEFAULT), "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }
}
