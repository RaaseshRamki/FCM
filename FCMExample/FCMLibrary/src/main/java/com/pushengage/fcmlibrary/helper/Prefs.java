package com.pushengage.fcmlibrary.helper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Jai on 11/09/16.
 */

public class Prefs {
    private static final String KEY_DEVICE_TOKEN = "deviceToken";
    private static final String KEY_HASH = "hash";

    private final SharedPreferences mPrefsRead;
    private final SharedPreferences.Editor mPrefsWrite;

    public Prefs(Context context) {
        final String PREFS = "PE";
        mPrefsRead = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        mPrefsWrite = mPrefsRead.edit();
    }

    public String getDeviceToken() {
        return mPrefsRead.getString(KEY_DEVICE_TOKEN, null);
    }

    public void setDeviceToken(String deviceToken) {
        mPrefsWrite.putString(KEY_DEVICE_TOKEN, deviceToken);
        mPrefsWrite.commit();
    }

    public String getHash() {
        return mPrefsRead.getString(KEY_HASH, null);
    }

    public void setHash(String hash) {
        mPrefsWrite.putString(KEY_HASH, hash);
        mPrefsWrite.commit();
    }

    /*public Boolean getPhoneVerified() {
        return mPrefsRead.getBoolean(KEY_PHONE_VERIFIED, false);
    }

    public void setPhoneVerified(Boolean phoneVerified) {
        mPrefsWrite.putBoolean(KEY_PHONE_VERIFIED, phoneVerified);
        mPrefsWrite.commit();
    }*/

}
