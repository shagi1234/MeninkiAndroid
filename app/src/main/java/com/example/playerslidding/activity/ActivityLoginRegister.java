package com.example.playerslidding.activity;

import static com.example.playerslidding.utils.Const.mainFragmentManager;
import static com.example.playerslidding.utils.FragmentHelper.addFragment;
import static com.example.playerslidding.utils.StaticMethods.initSystemUIViewListeners;
import static com.example.playerslidding.utils.StaticMethods.transparentStatusAndNavigation;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.playerslidding.R;
import com.example.playerslidding.fragment.FragmentFirstLaunch;

public class ActivityLoginRegister extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transparentStatusAndNavigation(this);
        setContentView(R.layout.activity_login_register);
        initSystemUIViewListeners(findViewById(R.id.root));
        mainFragmentManager = getSupportFragmentManager();

        addFragment(mainFragmentManager, R.id.container_login, FragmentFirstLaunch.newInstance());
    }
}