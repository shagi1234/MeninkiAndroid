package com.example.playerslidding.activity;

import static com.example.playerslidding.utils.StaticMethods.setClearLightStatusBar;
import static com.example.playerslidding.utils.StaticMethods.transparentStatusAndNavigation;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class ActivitySplashScreen extends AppCompatActivity {

    private com.example.playerslidding.shared.Account accountPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transparentStatusAndNavigation(this);
        androidx.core.splashscreen.SplashScreen splashScreen = androidx.core.splashscreen.SplashScreen.installSplashScreen(this);
        splashScreen.setKeepOnScreenCondition(() -> true);
        setClearLightStatusBar(this);

        accountPreferences = com.example.playerslidding.shared.Account.newInstance(this);
        Intent intent;

        if (accountPreferences.getUserIsLoggedIn()) {
            intent = new Intent(this, ActivityMain.class);
        } else {
            intent = new Intent(this, ActivityLoginRegister.class);
        }
        startActivity(intent);
        finish();
    }
}