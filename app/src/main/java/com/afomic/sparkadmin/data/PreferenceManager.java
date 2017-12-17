package com.afomic.sparkadmin.data;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by afomic on 11/21/17.
 *
 */

public class PreferenceManager {
    private SharedPreferences mSharedPreferences;
    private static final String PREFERENCE_FILE_NAME="com.afomic.spark-admin";
    private static final String PREF_USER_LOGIN="login";
    private static final String PREF_USER_ID="id";
    private static final String PREF_USER_NAME="username";
    private static final String PREF_ICON_URL="icon_url";
    private static final String PREF_ASSOCIATION_NAME="association";
    private static final String PREF_ADMIN ="admin";
    private String emptyString="";


    public PreferenceManager(Context context){
        mSharedPreferences=context.getSharedPreferences(PREFERENCE_FILE_NAME,Context.MODE_PRIVATE);
    }
    public void setUserLogin(Boolean isLogin){
        SharedPreferences.Editor mEditor=mSharedPreferences.edit();
        mEditor.putBoolean(PREF_USER_LOGIN,isLogin);
        mEditor.apply();
    }

    public void setUserId(String userId){
        SharedPreferences.Editor mEditor=mSharedPreferences.edit();
        mEditor.putString(PREF_USER_ID,userId);
        mEditor.apply();
    }
    public void setUsername(String username){
        SharedPreferences.Editor mEditor=mSharedPreferences.edit();
        mEditor.putString(PREF_USER_NAME,username);
        mEditor.apply();
    }
    public void setIconUrl(String iconUrl){
        SharedPreferences.Editor mEditor=mSharedPreferences.edit();
        mEditor.putString(PREF_ICON_URL,iconUrl);
        mEditor.apply();
    }
    public void setAssociationName(String associationName){
        SharedPreferences.Editor mEditor=mSharedPreferences.edit();
        mEditor.putString(PREF_ASSOCIATION_NAME,associationName);
        mEditor.apply();
    }
    public void setAdmin(boolean isAdmin){
        SharedPreferences.Editor mEditor=mSharedPreferences.edit();
        mEditor.putBoolean(PREF_ADMIN,isAdmin);
        mEditor.apply();
    }
    public String getUserId(){
        return mSharedPreferences.getString(PREF_USER_ID,emptyString);
    }
    public boolean isUserLogin(){
        return mSharedPreferences.getBoolean(PREF_USER_LOGIN,false);
    }
    public boolean isAdmin(){
        return mSharedPreferences.getBoolean(PREF_ADMIN,false);
    }
    public String getUsername(){
        return mSharedPreferences.getString(PREF_USER_NAME,emptyString);
    }
    public String getIconUrl(){
        return mSharedPreferences.getString(PREF_ICON_URL,emptyString);
    }
    public String getAssociationName(){
        return mSharedPreferences.getString(PREF_ASSOCIATION_NAME,emptyString);
    }
}
