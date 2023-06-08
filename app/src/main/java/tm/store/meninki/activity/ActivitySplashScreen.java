package tm.store.meninki.activity;

import static tm.store.meninki.utils.StaticMethods.setClearLightStatusBar;
import static tm.store.meninki.utils.StaticMethods.transparentStatusAndNavigation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import tm.store.meninki.shared.Account;

public class ActivitySplashScreen extends AppCompatActivity {

    private Account accountPreferences;
    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transparentStatusAndNavigation(this);
        androidx.core.splashscreen.SplashScreen splashScreen = androidx.core.splashscreen.SplashScreen.installSplashScreen(this);
        splashScreen.setKeepOnScreenCondition(() -> true);
        setClearLightStatusBar(this);
        context = this;
        accountPreferences = Account.newInstance(this);
        Intent intent;

        if (accountPreferences.getUserIsLoggedIn()) {
            intent = new Intent(this, ActivityMain.class);
        } else {
            intent = new Intent(this, ActivityLoginRegister.class);
        }
        startActivity(intent);
        finish();
    }

    public static Context getContext() {
        return context;
    }
}