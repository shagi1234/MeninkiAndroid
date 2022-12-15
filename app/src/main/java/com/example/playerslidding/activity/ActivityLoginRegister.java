package com.example.playerslidding.activity;

import static com.example.playerslidding.utils.Const.mainFragmentManager;
import static com.example.playerslidding.utils.FragmentHelper.addFragment;
import static com.example.playerslidding.utils.StaticMethods.transparentStatusAndNavigation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.playerslidding.R;
import com.example.playerslidding.fragment.FragmentFirstLaunch;

public class ActivityLoginRegister extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transparentStatusAndNavigation(this);
        setContentView(R.layout.activity_login_register);
        mainFragmentManager=getSupportFragmentManager();

        addFragment(mainFragmentManager,R.id.container_login, FragmentFirstLaunch.newInstance());
    }
}