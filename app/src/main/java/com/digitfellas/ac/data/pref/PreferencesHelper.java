package com.digitfellas.ac.data.pref;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yasar on 7/3/18.
 */

public class PreferencesHelper implements Pref {

    private static PreferencesHelper preferences;

    private SharedPreferences sharedPreferences;

    public static final String KEY_ACCESS_TOKEN = "access_token";
    public static final String KEY_TOKEN_TYPE = "token_type";
    public static final String KEY_ROLE = "role";
    public static final String KEY_ROLE_ID = "role_id";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_STATUS = "status";
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_MOBILE_NO = "mobile_no";
    public static final String KEY_FCM_TOKEN = "fcmtoken";
    public static final String KEY_DEVICE_ID = "deviceid";


    private PreferencesHelper(Context context) {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

    }

    public static PreferencesHelper getPreferencesInstance(Context context) {

        if (preferences == null) {
            preferences = new PreferencesHelper(context);
        }

        return preferences;
    }

    @Override
    public void saveSession(String accessToken, String tokenType, String role, String roleId, long userId, String name, String email, String mobileNo) {

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KEY_ACCESS_TOKEN, accessToken);
        editor.putString(KEY_TOKEN_TYPE, tokenType);
        editor.putString(KEY_ROLE, role);
        editor.putString(KEY_ROLE_ID, roleId);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_MOBILE_NO, mobileNo);
        editor.putLong(KEY_USER_ID, userId);
        editor.putBoolean(KEY_STATUS, true);
        editor.apply();

    }


    @Override
    public String getRoleType() {
        return sharedPreferences.getString(KEY_ROLE, null);
    }

    @Override
    public String getName() {
        return sharedPreferences.getString(KEY_NAME, "");
    }

    @Override
    public String getMobileNo() {
        return sharedPreferences.getString(KEY_MOBILE_NO, "");
    }

    @Override
    public String getEmail() {
        return sharedPreferences.getString(KEY_EMAIL, "");
    }

    @Override
    public String getAccessToken() {
        return sharedPreferences.getString(KEY_ACCESS_TOKEN, null);
    }

    @Override
    public Map<Object, Object> getUserDetails() {
        return getSession();
    }

    @Override
    public Map<String, String> getAuthendicate() {


        if (isLoggedIn()) {
            Map<String, String> hashMap = new HashMap<>();

            String auth = sharedPreferences.getString(KEY_TOKEN_TYPE, "") + " " + sharedPreferences.getString(KEY_ACCESS_TOKEN, "");


            hashMap.put("Authorization", auth);
            hashMap.put("Accept", "application/json");

            return hashMap;
        }

        return null;

    }

    @Override
    public void saveTokenAndDeviceID(String token, String deviceId) {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_FCM_TOKEN, token);
        editor.putString(KEY_DEVICE_ID, deviceId);

        editor.apply();

    }

    @Override
    public Map<String, String> getTokenAndDeviceID() {

        Map<String, String> map = new HashMap<>();

        map.put(KEY_FCM_TOKEN, sharedPreferences.getString(KEY_FCM_TOKEN, ""));
        map.put(KEY_DEVICE_ID, sharedPreferences.getString(KEY_DEVICE_ID, ""));


        return map;
    }


    public Map<Object, Object> getSession() {

        Map<Object, Object> hashMap = new HashMap<>();

        hashMap.put(KEY_ACCESS_TOKEN, sharedPreferences.getString(KEY_ACCESS_TOKEN, null));
        hashMap.put(KEY_TOKEN_TYPE, sharedPreferences.getString(KEY_TOKEN_TYPE, null));
        hashMap.put(KEY_ROLE, sharedPreferences.getString(KEY_ROLE, ""));
        hashMap.put(KEY_ROLE_ID, sharedPreferences.getString(KEY_ROLE_ID, ""));
        hashMap.put(KEY_NAME, sharedPreferences.getString(KEY_NAME, ""));
        hashMap.put(KEY_EMAIL, sharedPreferences.getString(KEY_EMAIL, ""));
        hashMap.put(KEY_MOBILE_NO, sharedPreferences.getString(KEY_MOBILE_NO, ""));
        hashMap.put(KEY_USER_ID, sharedPreferences.getLong(KEY_USER_ID, 0));
        hashMap.put(KEY_STATUS, sharedPreferences.getBoolean(KEY_STATUS, false));

        return hashMap;
    }

    @Override
    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_STATUS, false);
    }

    @Override
    public void clear() {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

    }


}
