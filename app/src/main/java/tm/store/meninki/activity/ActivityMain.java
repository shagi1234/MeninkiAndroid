package tm.store.meninki.activity;

import static tm.store.meninki.utils.Const.mainFragmentManager;
import static tm.store.meninki.utils.FragmentHelper.addFragment;
import static tm.store.meninki.utils.StaticMethods.initSystemUIViewListeners;
import static tm.store.meninki.utils.StaticMethods.transparentStatusAndNavigation;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import tm.store.meninki.R;
import tm.store.meninki.fragment.FragmentFlow;
import tm.store.meninki.interfaces.OnBackPressedFragment;


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