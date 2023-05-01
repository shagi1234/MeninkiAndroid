package tm.store.meninki.activity;

import static tm.store.meninki.utils.Const.mainFragmentManager;
import static tm.store.meninki.utils.FragmentHelper.hideAdd;
import static tm.store.meninki.utils.StaticMethods.dpToPx;
import static tm.store.meninki.utils.StaticMethods.initSystemUIViewListeners;
import static tm.store.meninki.utils.StaticMethods.navigationBarHeight;
import static tm.store.meninki.utils.StaticMethods.setBackgroundDrawable;
import static tm.store.meninki.utils.StaticMethods.setMargins;
import static tm.store.meninki.utils.StaticMethods.setPadding;
import static tm.store.meninki.utils.StaticMethods.statusBarHeight;
import static tm.store.meninki.utils.StaticMethods.transparentStatusAndNavigation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.security.ProviderInstaller;

import tm.store.meninki.R;
import tm.store.meninki.databinding.ActivityMainBinding;
import tm.store.meninki.fragment.FragmentBasket;
import tm.store.meninki.fragment.FragmentCategory;
import tm.store.meninki.fragment.FragmentMain;
import tm.store.meninki.fragment.FragmentProfile;
import tm.store.meninki.interfaces.OnBackPressedFragment;
import tm.store.meninki.shared.Account;


public class ActivityMain extends AppCompatActivity implements View.OnClickListener {
    private ActivityMainBinding b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transparentStatusAndNavigation(this);
        updateAndroidSecurityProvider(this);
        b = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        mainFragmentManager = getSupportFragmentManager();
        setBackgrounds();
        setNavListeners();

        hideAdd(FragmentMain.newInstance(), FragmentMain.class.getSimpleName(), mainFragmentManager, R.id.content_with_nav);
    }

    private void setNavListeners() {
        b.navMain.setOnClickListener(this);
        b.navCategory.setOnClickListener(this);
        b.navCard.setOnClickListener(this);
        b.navProfile.setOnClickListener(this);
    }

    private void setBackgrounds() {
        setBackgroundDrawable(this, b.nav, R.color.bg, 0, 50, false, 0);
        setNav(R.id.nav_main);
    }

    @SuppressLint("NonConstantResourceId")
    private void setNav(int id) {
        setBackgroundDrawable(this, b.nav, R.color.bg, 0, 50, false, 0);
        navInactive();

        switch (id) {
            case R.id.nav_main:
                setBackgroundDrawable(this, b.navMain, R.color.contrast, 0, 0, true, 0);
                b.navMain.setImageResource(R.drawable.main_active);

                hideAdd(FragmentMain.newInstance(), FragmentMain.class.getSimpleName(), mainFragmentManager, R.id.content_with_nav);
                break;
            case R.id.nav_category:
                setBackgroundDrawable(this, b.navCategory, R.color.contrast, 0, 0, true, 0);
                b.navCategory.setImageResource(R.drawable.category_active);

                hideAdd(FragmentCategory.newInstance(), FragmentCategory.class.getSimpleName(), mainFragmentManager, R.id.content_with_nav);
                break;
            case R.id.nav_card:
                setBackgroundDrawable(this, b.navCard, R.color.contrast, 0, 0, true, 0);
                b.navCard.setImageResource(R.drawable.card_active);

                hideAdd(FragmentBasket.newInstance(), FragmentBasket.class.getSimpleName(), mainFragmentManager, R.id.content_with_nav);
                break;
            case R.id.nav_profile:
                setBackgroundDrawable(this, b.navProfile, R.color.contrast, 0, 0, true, 0);

                hideAdd(FragmentProfile.newInstance(FragmentProfile.TYPE_USER, Account.newInstance(ActivityMain.this).getPrefUserUUID()), FragmentProfile.class.getSimpleName(), mainFragmentManager, R.id.content_with_nav);
                break;

        }
    }

    private void navInactive() {
        setBackgroundDrawable(this, b.navMain, R.color.on_bg_ls, 0, 0, true, 0);
        b.navMain.setImageResource(R.drawable.main_inactive);

        setBackgroundDrawable(this, b.navCategory, R.color.on_bg_ls, 0, 0, true, 0);
        b.navCategory.setImageResource(R.drawable.category_inactive);

        setBackgroundDrawable(this, b.navCard, R.color.on_bg_ls, 0, 0, true, 0);
        b.navCard.setImageResource(R.drawable.card_inactive);

        setBackgroundDrawable(this, b.navProfile, R.color.on_bg_ls, 0, 0, true, 0);
    }

    private void updateAndroidSecurityProvider(Activity callingActivity) {
        try {
            ProviderInstaller.installIfNeeded(this);
        } catch (GooglePlayServicesRepairableException e) {

//             Thrown when Google Play Services is not installed, up-to-date, or enabled
//             Show dialog to allow users to install, update, or otherwise enable Google Play services.

            GooglePlayServicesUtil.getErrorDialog(e.getConnectionStatusCode(), callingActivity, 0);
        } catch (GooglePlayServicesNotAvailableException e) {
            Log.e("SecurityException", "Google Play Services not available.");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initSystemUIViewListeners(b.main);
        new Handler().postDelayed(() -> setMargins(b.nav,
                dpToPx(60, ActivityMain.this),
                0,
                dpToPx(60, ActivityMain.this),
                dpToPx(20, ActivityMain.this) + navigationBarHeight), 30);

        new Handler().postDelayed(() -> setPadding(b.contentWithNav, 0, statusBarHeight, 0, navigationBarHeight), 30);
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = mainFragmentManager.findFragmentById(R.id.fragment_container_main);
        if (!(fragment instanceof OnBackPressedFragment) || !((OnBackPressedFragment) fragment).onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        setNav(v.getId());
    }
}