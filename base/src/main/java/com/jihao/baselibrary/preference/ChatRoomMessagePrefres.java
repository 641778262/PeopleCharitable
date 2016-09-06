package com.jihao.baselibrary.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.jihao.baselibrary.http.OkHttpUtils;

/**
 * Created by json on 16/7/15.
 * 用于存放已读聊天室语音消息
 */
public class ChatRoomMessagePrefres {
    public static final String SP_NAME = "chat_room_audio_msg";

    public static void saveMessage(String msgId) {
        getSharedPreferences().edit().putBoolean(msgId,true).commit();
    }

    public static boolean containsMessage(String msgId) {
        return getSharedPreferences().contains(msgId);
    }

    static SharedPreferences getSharedPreferences() {
        return OkHttpUtils.mContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }
}
