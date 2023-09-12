package tm.store.meninki.activity;

import static tm.store.meninki.adapter.AdapterMediaAddPost.PICK_VIDEO_REQUEST;
import static tm.store.meninki.adapter.AdapterPostPager.lastExoPlayer;
import static tm.store.meninki.utils.Const.mainFragmentManager;
import static tm.store.meninki.utils.FragmentHelper.hideAdd;
import static tm.store.meninki.utils.StaticMethods.dpToPx;
import static tm.store.meninki.utils.StaticMethods.hideSoftKeyboard;
import static tm.store.meninki.utils.StaticMethods.initSystemUIViewListeners;
import static tm.store.meninki.utils.StaticMethods.navigationBarHeight;
import static tm.store.meninki.utils.StaticMethods.setBackgroundDrawable;
import static tm.store.meninki.utils.StaticMethods.setMargins;
import static tm.store.meninki.utils.StaticMethods.setPadding;
import static tm.store.meninki.utils.StaticMethods.statusBarHeight;
import static tm.store.meninki.utils.StaticMethods.transparentStatusAndNavigation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleRegistry;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.security.ProviderInstaller;
import com.gowtham.library.utils.LogMessage;
import com.gowtham.library.utils.TrimVideo;

import tm.store.meninki.R;
import tm.store.meninki.adapter.AdapterMediaAddPost;
import tm.store.meninki.data.MediaLocal;
import tm.store.meninki.data.SelectedMedia;
import tm.store.meninki.databinding.ActivityMainBinding;
import tm.store.meninki.fragment.FragmentBasket;
import tm.store.meninki.fragment.FragmentCategory;
import tm.store.meninki.fragment.FragmentMain;
import tm.store.meninki.fragment.FragmentPost;
import tm.store.meninki.fragment.FragmentProfile;
import tm.store.meninki.fragment.FragmentProfileViewPager;
import tm.store.meninki.interfaces.OnBackPressedFragment;
import tm.store.meninki.shared.Account;
import tm.store.meninki.utils.StaticMethods;


public class ActivityMain extends AppCompatActivity implements View.OnClickListener, LifecycleObserver {
    private ActivityMainBinding b;
    private static ActivityMain activityMain;

    private final ActivityResultLauncher<Intent> videoTrimResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
            Uri uri = Uri.parse(TrimVideo.getTrimmedVideoPath(result.getData()));

            if (AdapterMediaAddPost.getInstance() != null) {
                SelectedMedia.getProductImageList().add(new MediaLocal(-1, uri.getPath(), 3));
                AdapterMediaAddPost.getInstance().notifyDataSetChanged();
            }

        } else {
            LogMessage.v("videoTrimResultLauncher data is null");
        }
    });

    public static ActivityMain getInstance() {
        return activityMain;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transparentStatusAndNavigation(this);
        updateAndroidSecurityProvider(this);
        b = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        getLifecycle().addObserver(this);
        activityMain = this;
        mainFragmentManager = getSupportFragmentManager();

        setBackgrounds();
        setNavListeners();

    }

    private void setNavListeners() {
        b.navMain.setOnClickListener(this);
        b.navCategory.setOnClickListener(this);
        b.navCard.setOnClickListener(this);
        b.navProfile.setOnClickListener(this);

        mainFragmentManager.addOnBackStackChangedListener(() -> {
            if (getCurrentFragmentTag().equals(FragmentProfileViewPager.class.getName())) {
                if (lastExoPlayer != null) lastExoPlayer.play();

                StaticMethods.setNavBarIconsWhite(this);
                StaticMethods.setClearLightStatusBar(this);
            } else {
                if (lastExoPlayer != null) lastExoPlayer.pause();
                StaticMethods.setNavBarIconsBlack(this);
                StaticMethods.setLightStatusBar(this);
            }
        });
    }

    public String getCurrentFragmentTag() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        int backStackEntryCount = fragmentManager.getBackStackEntryCount();
        if (backStackEntryCount == 0) {
            return "";
        }
        return fragmentManager.getBackStackEntryAt(backStackEntryCount - 1).getName();
    }

    private void setBackgrounds() {
        if (getIntent().getBooleanExtra("is_language_changed", false)) {
            setNav(R.id.nav_profile);
        } else setNav(R.id.nav_main);
    }

    @SuppressLint("NonConstantResourceId")
    private void setNav(int id) {
        navInactive();

        switch (id) {
            case R.id.nav_main:
                setBackgroundDrawable(this, b.navMain, R.color.contrast, 0, 0, true, 0);
                b.navMain.setImageResource(R.drawable.main_active);

                hideAdd(FragmentMain.newInstance(false), FragmentMain.class.getSimpleName(), mainFragmentManager, R.id.content_with_nav);
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
                hideAdd(FragmentProfile.newInstance(FragmentProfile.MY_PROFILE_WITH_NAV, Account.newInstance(ActivityMain.this).getPrefUserUUID()), FragmentProfile.class.getSimpleName(), mainFragmentManager, R.id.content_with_nav);
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
            GooglePlayServicesUtil.getErrorDialog(e.getConnectionStatusCode(), callingActivity, 0);
        } catch (GooglePlayServicesNotAvailableException e) {
            Log.e("SecurityException", "Google Play Services not available.");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initSystemUIViewListeners(b.main);
        new Handler().postDelayed(() -> setMargins(b.nav, dpToPx(60, ActivityMain.this), 0, dpToPx(60, ActivityMain.this), dpToPx(20, ActivityMain.this) + navigationBarHeight), 30);

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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_VIDEO_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri selectedVideoUri = data.getData();
            Log.e("OnVideoSelected", "onActivityResult: " + selectedVideoUri.toString());

            TrimVideo.activity(selectedVideoUri.toString()).start(this, videoTrimResultLauncher);
            // Do something with the selected video URI
        }
    }

    @Override
    public void onClick(View v) {
        setNav(v.getId());
    }
}