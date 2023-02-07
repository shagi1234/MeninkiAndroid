package com.example.playerslidding.activity;

import static com.example.playerslidding.utils.Const.mainFragmentManager;
import static com.example.playerslidding.utils.FragmentHelper.addFragment;
import static com.example.playerslidding.utils.StaticMethods.initSystemUIViewListeners;
import static com.example.playerslidding.utils.StaticMethods.transparentStatusAndNavigation;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.playerslidding.R;
import com.example.playerslidding.fragment.FragmentFlow;
import com.example.playerslidding.interfaces.OnBackPressedFragment;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.security.ProviderInstaller;


public class ActivityMain extends AppCompatActivity {
    private ConstraintLayout root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transparentStatusAndNavigation(this);
        setContentView(R.layout.activity_main);
        root = findViewById(R.id.main);
//        updateAndroidSecurityProvider(this);
        mainFragmentManager = getSupportFragmentManager();
        addFragment(mainFragmentManager, R.id.fragment_container_main, FragmentFlow.newInstance());
    }

    @Override
    protected void onResume() {
        super.onResume();
        initSystemUIViewListeners(root);
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = mainFragmentManager.findFragmentById(R.id.fragment_container_main);
        if (!(fragment instanceof OnBackPressedFragment) || !((OnBackPressedFragment) fragment).onBackPressed()) {
            if (mainFragmentManager.getBackStackEntryCount() == 1) {
                finish();
            } else {
                super.onBackPressed();
            }
        }
    }
}