package com.szch.data;

import android.content.Context;
import android.content.SharedPreferences;


public class PreferencesData {

    public static final String SHARE_PRE = "SharedPreferences";

    public static final String CONSTRUCTION_NAME = "construction_name";

    public static final String TEST_AREA_NUMBER = "test_area_number";

    public static final String TEST_ANGLE = "test_angle";

    public static final String TEST_POSTION = "test_postion";

    public static final String IS_MACHINE = "is_machine";

    public static final String DESGIN_STRENGTH = "desgin_strength";

    public static final String CARBONIZE = "carbonize";

    public static final String FIX_STRENGTH = "fix_strength";

    public static final String DATE = "date";

    public static SharedPreferences mSharedPreferences;
    
    public static SharedPreferences.Editor mEditor;

    public static void setStringData(Context c, String key, String value) {
        mSharedPreferences = c.getSharedPreferences( SHARE_PRE, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        mEditor.putString(key, value);
        mEditor.commit();
    }
    
    public static String getStringData(Context c, String key, String defaulValue) {
        mSharedPreferences = c.getSharedPreferences( SHARE_PRE, Context.MODE_PRIVATE);
        String value = mSharedPreferences.getString(key, defaulValue);
        return value;
    }

    public static int getIntData(Context c, String key, int defaulValue) {
        mSharedPreferences = c.getSharedPreferences( SHARE_PRE, Context.MODE_PRIVATE);
        int value = mSharedPreferences.getInt(key, defaulValue);
        return value;
    }

    public static void setIntData(Context c, String key, int value) {
        mSharedPreferences = c.getSharedPreferences( SHARE_PRE, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        mEditor.putInt(key, value);
        mEditor.commit();
    }

    public static boolean getBooleanData(Context c, String key, boolean defaulValue) {
        mSharedPreferences = c.getSharedPreferences( SHARE_PRE, Context.MODE_PRIVATE);
        boolean value = mSharedPreferences.getBoolean(key, defaulValue);
        return value;
    }

    public static void setBooleanData(Context c, String key, boolean value) {
        mSharedPreferences = c.getSharedPreferences( SHARE_PRE, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        mEditor.putBoolean(key, value);
        mEditor.commit();
    }

    public static void setfloatData(Context c, String key, float value) {
        mSharedPreferences = c.getSharedPreferences( SHARE_PRE, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        mEditor.putFloat(key, value);
        mEditor.commit();
    }

    public static float getfloatData(Context c, String key, float defaulValue) {
        mSharedPreferences = c.getSharedPreferences( SHARE_PRE, Context.MODE_PRIVATE);
        float value = mSharedPreferences.getFloat(key, defaulValue);
        return value;
    }
}
