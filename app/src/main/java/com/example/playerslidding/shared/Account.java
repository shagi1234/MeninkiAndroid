package com.example.playerslidding.shared;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.Date;

public class Account {

    private final SharedPreferences pref;
    public SharedPreferences.Editor editor;
    private final Context _context;
    int PRIVATE_MODE = 0;
    private static Account accountPreferences;

    private static final String PREF_NAME = "meninki_account";
    private static final String PREF_COLOR_CODE = "color_code";
    private static final String PREF_FIREBASE_TOKEN = "firebase_token";
    private static final String PREF_TOKEN = "token";
    private static final String PREF_SMS_TOKEN = "sms_token";
    private static final String PREF_CHECK_SMS_TOKEN = "check_sms_token";
    private static final String PREF_CURRENT_TIME = "current_time";
    private static final String PREF_CURRENT_CHECK_SMS_TIME = "current_time_check_sms";
    private static final String PREF_REGISTER_NAME = "register_name";
    private static final String PREF_REGISTER_IMAGE = "register_image";
    private static final String PREF_USER_UUID = "uuid";
    private static final String PREF_USER_BIRTHDAY = "birthday";
    private static final String PREF_USER_TYPE = "user_role";
    private static final String PREF_USER_ABOUT = "user_desc";
    private static final String PREF_USER_NICKNAME = "user_nickname";
    private static final String PREF_USER_IS_LOGGED_IN = "user_is_logged_in";

    public static Account newInstance(Context context) {
        if (accountPreferences == null) {
            accountPreferences = new Account(context);
        }
        return accountPreferences;
    }

    public Account(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public void saveToken(String token) {
        editor.putString(PREF_TOKEN, token);
        editor.commit();
    }

    public void saveSendSmsToken(String token) {
        editor.putString(PREF_SMS_TOKEN, token);
        editor.commit();
    }

    public void saveCheckSmsToken(String token) {
        editor.putString(PREF_CHECK_SMS_TOKEN, token);
        editor.commit();
    }


    public String getSendSmsToken() {
        if (_context == null) {
            return "";
        } else
            return pref.getString(PREF_SMS_TOKEN, "");
    }

    public String getCheckSmsToken() {
        if (_context == null) {
            return "";
        } else
            return pref.getString(PREF_CHECK_SMS_TOKEN, "");
    }

    public String getToken() {
        if (_context == null) {
            return "";
        } else
            return pref.getString(PREF_TOKEN, "");
    }

    public void saveCurrentTime(long t) {
        if (t != 0) {
            long c = new Date().getTime();
            editor.putLong(PREF_CURRENT_TIME, c);
        } else editor.putLong(PREF_CURRENT_TIME, 0);

        editor.commit();
    }

    public void saveCurrentCheckSmsTime(long time) {
        if (time != 0) {
            long c = new Date().getTime();
            editor.putLong(PREF_CURRENT_CHECK_SMS_TIME, c);
        } else {
            editor.putLong(PREF_CURRENT_CHECK_SMS_TIME, time);
        }
        editor.commit();
    }

    public long getCurrentTime() {
        if (_context == null) {
            return 0;
        } else
            return pref.getLong(PREF_CURRENT_TIME, 0);
    }

    public long getCheckSmsTime() {
        if (_context == null) {
            return 0;
        } else
            return pref.getLong(PREF_CURRENT_CHECK_SMS_TIME, 0);
    }


    public void saveRegisterName(String registerName) {
        editor.putString(PREF_REGISTER_NAME, registerName);
        editor.commit();
    }

    public String getPrefRegisterName() {
        if (_context == null) {
            return "";
        } else
            return pref.getString(PREF_REGISTER_NAME, "");
    }

    public void saveNickname(String name) {
        editor.putString(PREF_USER_NICKNAME, name);
        editor.commit();
    }

    public String getPrefUserNickname() {
        if (_context == null) {
            return "";
        } else
            return pref.getString(PREF_USER_NICKNAME, "");
    }

    public void saveUserUUID(String userUUID) {
        editor.putString(PREF_USER_UUID, userUUID);
        editor.commit();
    }


    public String getPrefUserUUID() {
        if (_context == null) {
            return "";
        } else
            return pref.getString(PREF_USER_UUID, "");
    }


    public void saveRegisterImage(String registerImage) {
        editor.putString(PREF_REGISTER_IMAGE, registerImage);
        editor.commit();
    }

    public void saveColorCode(String registerImage) {
        editor.putString(PREF_COLOR_CODE, registerImage);
        editor.commit();
    }

    public String getPrefColorCode() {
        if (_context == null) {
            return "";
        } else
            return pref.getString(PREF_COLOR_CODE, "");
    }

    public String getPrefRegisterImage() {
        if (_context == null) {
            return "";
        } else
            return pref.getString(PREF_REGISTER_IMAGE, "");
    }

    public boolean getUserIsLoggedIn() {
        if (_context == null) {
            return false;
        } else
            return pref.getBoolean(PREF_USER_IS_LOGGED_IN, false);
    }


    public void saveUserBirthday(String userBirthday) {

        editor.putString(PREF_USER_BIRTHDAY, userBirthday);
        editor.commit();
    }

    public String getPrefUserBirthday() {
        if (_context == null) {
            return "";
        } else
            return pref.getString(PREF_USER_BIRTHDAY, "");
    }


    public void saveUserType(int userType) {
        editor.putInt(PREF_USER_TYPE, userType);
        editor.commit();
    }

    public int getPrefUserType() {
        if (_context == null) {
            return -1;
        } else
            return pref.getInt(PREF_USER_TYPE, -1);
    }

    public void saveUserIsLoggedIn() {
        editor.putBoolean(PREF_USER_IS_LOGGED_IN, true);
        editor.commit();
    }

    public void saveUserAbout(String userAbout) {
        editor.putString(PREF_USER_ABOUT, userAbout);
        editor.commit();
    }

    public String getPrefUserAbout() {
        if (_context == null) {
            return "";
        } else
            return pref.getString(PREF_USER_ABOUT, "");
    }

    public void logout() {
        saveToken("");
        editor.putBoolean(PREF_USER_IS_LOGGED_IN, false);
        editor.commit();
    }

    @SuppressLint("NewApi")
    public void clear() {
        editor.clear();
        editor.commit();
    }
}

