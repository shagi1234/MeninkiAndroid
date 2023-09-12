package tm.store.meninki.activity;

import static tm.store.meninki.utils.Const.mainFragmentManager;
import static tm.store.meninki.utils.FragmentHelper.addFragment;
import static tm.store.meninki.utils.StaticMethods.initSystemUIViewListeners;
import static tm.store.meninki.utils.StaticMethods.transparentStatusAndNavigation;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import tm.store.meninki.R;
import tm.store.meninki.fragment.FragmentCountryAndNumber;
import tm.store.meninki.interfaces.OnBackPressedFragment;

public class ActivityLoginRegister extends AppCompatActivity {
    private FrameLayout root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transparentStatusAndNavigation(this);
        setContentView(R.layout.activity_login_register);
        root = findViewById(R.id.root);
        mainFragmentManager = getSupportFragmentManager();

        addFragment(mainFragmentManager, R.id.container_login, FragmentCountryAndNumber.newInstance());
    }

    @Override
    protected void onResume() {
        super.onResume();
        initSystemUIViewListeners(root);
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = mainFragmentManager.findFragmentById(R.id.container_login);
        if (!(fragment instanceof OnBackPressedFragment) || !((OnBackPressedFragment) fragment).onBackPressed()) {
            if (mainFragmentManager.getBackStackEntryCount() == 1) {
                finish();
            } else {
                super.onBackPressed();
            }
        }
    }
}