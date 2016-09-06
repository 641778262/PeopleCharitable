package com.jihao.baselibrary.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.jihao.baselibrary.http.OkHttpUtils;


/**
 * Created by hzxuwen on 2015/4/13.
 */
public class Preferences {
    public static final String KEY_IM_ACCOUNT = "im_account";
    public static final String KEY_IM_TOKEN = "im_token";
    public static final String KEY_VISITOR_IM_ACCOUNT = "visitor_im_account";
    public static final String KEY_VISITOR_IM_TOKEN = "visitor_im_token";
    public static final String KEY_APP_SID = "app_sid";
    public static final String KEY_APP_TOKEN = "app_token";
    public static final String SP_NAME = "user_info";
    public static final String HAS_LOAD_RECENT_CONTACTS = "has_load_recent_contact";
    public static final String HAS_SET_AVATAR_NAME = "has_set_avatar_name";
    public static final String XIAO_QU_ACCID = "xiao_qu_accid";
    public static final String ROBOTS_ACCID = "robots_accid";
    public static final String USER_ACCOUNT_ID = "userid";
    public static final String KEY_USER_ID = "id";//助手端id
    public static final String ROBOTS_HEADER_URL = "robots_header_url";
    public static final String VERIFIED = "verified";
    public static final String COURSE_ROBOTS_ACCID = "course_robots_accid";  // 病程管理 accid
    public static final String COURSE_TITLE = "course_title"; //course title
    public static final String COURSE_SITE_ID = "course_site_id"; //科室id
    public static final String CODE_MENU = "CodeMenu";
    public static final String HAS_SCAN = "has_course";//病程管理是否扫码



    public static void saveId(String id) {
        saveString(KEY_USER_ID,id);
    }

    public static String getId() {
        return getString(KEY_USER_ID);
    }

    public static void saveSid(String sid) {
        saveString(KEY_APP_SID,sid);
    }

    public static String getSid() {
        return getString(KEY_APP_SID);
    }

    public static void saveToken(String token) {
        saveString(KEY_APP_TOKEN,token);
    }

    public static String getToken() {
        return getString(KEY_APP_TOKEN);
    }

    public static void saveXiaoQuAccId(String accid) {
        PreferenceManager.getDefaultSharedPreferences(OkHttpUtils.mContext).edit()
                .putString(XIAO_QU_ACCID,accid).commit();
    }

    public static String getXiaoQuAccid() {
        return PreferenceManager.getDefaultSharedPreferences(OkHttpUtils.mContext)
                .getString(XIAO_QU_ACCID, null);
    }

    public static void saveRobotsAccid(String accid) {
        PreferenceManager.getDefaultSharedPreferences(OkHttpUtils.mContext).edit()
                .putString(ROBOTS_ACCID,accid).commit();
    }

    public static String getRobotsAccid() {
        return PreferenceManager.getDefaultSharedPreferences(OkHttpUtils.mContext)
                .getString(ROBOTS_ACCID,null);
    }

    public static void saveCodeMenu(String codeMenu) {
        saveString(CODE_MENU, codeMenu);
    }

    public static String getCodeMenu() {
        return getString(CODE_MENU);
    }


    /***
     * 云信相关
     * @return
     */

    public static void saveIMAccount(String account) {
        saveString(KEY_IM_ACCOUNT, account);
    }

    public static String getIMAccount() {
        return getString(KEY_IM_ACCOUNT);
    }

    public static void saveIMToken(String token) {
        saveString(KEY_IM_TOKEN, token);
    }

    public static String getIMToken() {
        return getString(KEY_IM_TOKEN);
    }

    public static void saveVisitorIMAccount(String account) {
        saveString(KEY_VISITOR_IM_ACCOUNT, account);
    }

    public static String getVisitorIMAccount() {
        return getString(KEY_VISITOR_IM_ACCOUNT);
    }

    public static void saveVisitorIMToken(String token) {
        saveString(KEY_VISITOR_IM_TOKEN, token);
    }

    public static String getVisitorIMToken() {
        return getString(KEY_VISITOR_IM_TOKEN);
    }

    public static void saveHasLoadRecentContact(boolean hasLoad) {
        saveBoolean(HAS_LOAD_RECENT_CONTACTS,hasLoad);
    }

    public static boolean hasLoadRecentContact() {
        return getBoolean(HAS_LOAD_RECENT_CONTACTS);
    }



    public static void saveString(String key, String value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getString(String key) {
        return getSharedPreferences().getString(key, "");
    }

    public static void saveBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean getBoolean(String key) {
        return getSharedPreferences().getBoolean(key, false);
    }

    public static int getInt(String key) {
        return getSharedPreferences().getInt(key, 0);
    }

    public static void saveInt(String key,int value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putInt(key, value);
        editor.commit();
    }

    static SharedPreferences getSharedPreferences() {
        return OkHttpUtils.mContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    public static boolean containsKey(String key) {
        return getSharedPreferences().contains(key);
    }

    public static void removeKey(String key) {
        getSharedPreferences().edit().remove(key).commit();
    }

    public static void clearData() {
        getSharedPreferences().edit().clear().commit();
    }

    public static boolean isLogin() {
        return !TextUtils.isEmpty(getIMAccount()) && !TextUtils.isEmpty(getIMToken());
    }

    public static boolean isVerified() {
        return getString(VERIFIED).equals("1");
    }

}
