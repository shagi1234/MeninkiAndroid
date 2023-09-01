package tm.store.meninki.shared;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class Account {

    private final SharedPreferences pref;
    public SharedPreferences.Editor editor;
    private final Context _context;
    int PRIVATE_MODE = 0;
    private static Account accountPreferences;

    private static final String PREF_NAME = "meninki_account";
    private static final String PREF_VALID_TO_TOKEN = "pref_valid_to";
    private static final String PREF_COLOR_CODE = "color_code";
    private static final String PREF_TOKEN = "token";
    private static final String PREF_REFRESH_TOKEN = "refresh_token";
    private static final String PREF_SMS_TOKEN = "sms_token";
    private static final String PREF_CHECK_SMS_TOKEN = "check_sms_token";
    private static final String PREF_REGISTER_NAME = "register_name";
    private static final String PREF_REGISTER_IMAGE = "register_image";
    private static final String PREF_USER_UUID = "uuid";
    private static final String PREF_USER_TYPE = "user_role";
    private static final String PREF_USER_PHONE_NUMBER = "user_phone_num";
    private static final String PREF_USER_IS_LOGGED_IN = "user_is_logged_in";
    private static final String PREF_MY_SHOP_ID = "__my_shops";
    private static final String PREF_LANGUAGE = "_language";

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

    public void saveAccessToken(String token) {
        editor.putString(PREF_TOKEN, token);
        editor.commit();
    }

    public void setMyShops(String ids) {
        editor.putString(PREF_MY_SHOP_ID, ids);
        editor.commit();
    }

    public String getMyShop() {
        if (_context == null) {
            return "";
        } else
            return pref.getString(PREF_MY_SHOP_ID, "");
    }

    public String getAccessToken() {
        if (_context == null) {
            return "";
        } else
            return "Bearer " + pref.getString(PREF_TOKEN, "");
    }


    public void saveRefreshToken(String token) {
        editor.putString(PREF_REFRESH_TOKEN, token);
        editor.commit();
    }

    public void saveUserPhoneNumber(String token) {
        editor.putString(PREF_USER_PHONE_NUMBER, token);
        editor.commit();
    }

    public String getUserPhoneNumber() {
        if (_context == null) {
            return "";
        } else
            return pref.getString(PREF_USER_PHONE_NUMBER, "");
    }

    public void saveValidToToken(String token) {
        editor.putString(PREF_VALID_TO_TOKEN, token);
        editor.commit();
    }

    public String getPrefValidToToken() {
        if (_context == null) {
            return "";
        } else
            return pref.getString(PREF_VALID_TO_TOKEN, "");
    }

    public String getRefreshToken() {
        if (_context == null) {
            return "";
        } else
            return pref.getString(PREF_REFRESH_TOKEN, "");
    }

    public void saveSendSmsId(String token) {
        editor.putString(PREF_SMS_TOKEN, token);
        editor.commit();
    }

    public String getSendSmsId() {
        if (_context == null) {
            return "";
        } else
            return pref.getString(PREF_SMS_TOKEN, "");
    }

    public String getCheckSmsId() {
        if (_context == null) {
            return "";
        } else
            return pref.getString(PREF_CHECK_SMS_TOKEN, "");
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

    public void setLanguage(String key) {
        editor.putString(PREF_LANGUAGE, key);
        editor.commit();
    }

    public String getLanguage() {
        if (_context == null) {
            return "ru";
        } else
            return pref.getString(PREF_LANGUAGE, "ru");
    }

    @SuppressLint("NewApi")
    public void clear() {
        editor.clear();
        editor.commit();
    }
}

