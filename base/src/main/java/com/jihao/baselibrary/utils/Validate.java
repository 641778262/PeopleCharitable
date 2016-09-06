package com.jihao.baselibrary.utils;

/**
 * Created by jiayonghua on 16/6/20.
 * 输入校验帮助类
 */
public class Validate {
    public static boolean isEmpty(String content) {
        if (content == null || "null".equalsIgnoreCase(content) || content.trim().length() == 0) {
            return true;
        }
        return false;
    }
}
