package com.digitfellas.ac.data.pref;


import com.digitfellas.ac.data.model.request.Login;

import java.util.Map;

/**
 * Created by yasar on 22/3/18.
 */

public interface Pref {


    boolean isLoggedIn();

    void saveSession(String accessToken, String tokenType, String role, String roleId, long userId,String name, String email, String mobileNo);
//    void saveUserInfo(String name, String email, String mobileNo);

    String getRoleType();

    String getName();

    String getMobileNo();

    String getEmail();

    String getAccessToken();

    Map getUserDetails();

    Map<String, String> getAuthendicate();

    void saveTokenAndDeviceID(String token,String deviceId);

    Map<String,String> getTokenAndDeviceID();

    void clear();



}
