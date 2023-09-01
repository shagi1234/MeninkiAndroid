package tm.store.meninki.activity;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static tm.store.meninki.utils.Const.mainFragmentManager;
import static tm.store.meninki.utils.FragmentHelper.addFragment;
import static tm.store.meninki.utils.StaticMethods.navigationBarHeight;
import static tm.store.meninki.utils.StaticMethods.setPadding;
import static tm.store.meninki.utils.StaticMethods.statusBarHeight;
import static tm.store.meninki.utils.StaticMethods.transparentStatusAndNavigation;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import java.util.Locale;
import java.util.Map;

import tm.store.meninki.R;
import tm.store.meninki.databinding.ActivitySettingsBinding;
import tm.store.meninki.fragment.FragmentEditProfile;
import tm.store.meninki.shared.Account;
import tm.store.meninki.utils.StaticMethods;

public class ActivitySettings extends AppCompatActivity {
    private ActivitySettingsBinding b;
    private boolean isLangChanged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transparentStatusAndNavigation(this);
        b = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        initClickListeners();
        setBackgrounds();
    }

    @Override
    public void onResume() {
        super.onResume();
        setPadding(b.layHeader, 0, statusBarHeight, 0, 0);
        setPadding(b.getRoot(), 0, 0, 0, navigationBarHeight);
    }

    private void setBackgrounds() {
        StaticMethods.setBackgroundDrawable(this, b.layChooseLanguage, R.color.on_bg_ls, 0, 10, false, 0);
        StaticMethods.setBackgroundDrawable(this, b.layDeleteAccount, R.color.on_bg_ls, 0, 10, false, 0);
        StaticMethods.setBackgroundDrawable(this, b.layEdtProfile, R.color.on_bg_ls, 0, 10, false, 0);
        StaticMethods.setBackgroundDrawable(this, b.layLogOut, R.color.on_bg_ls, 0, 10, false, 0);
    }

    private void initClickListeners() {
        b.clickEdtProfile.setOnClickListener(view -> {
            wait(view);

            addFragment(getSupportFragmentManager(), R.id.fragment_container_main, FragmentEditProfile.newInstance());
        });

        b.layLogOut.setOnClickListener(view -> new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.logout)).setMessage(R.string.do_you_really_want_sign_out).setPositiveButton(getResources().getString(R.string.continuee), (dialog, which) -> {

            Account.newInstance(this).clear();
            ActivityMain.getInstance().finish();
            Intent intent = new Intent(this, ActivityLoginRegister.class);
            startActivity(intent);
            this.finish();

        }).setNegativeButton(getResources().getString(R.string.cancel), null).setIcon(R.drawable.baseline_logout_24).show());

        b.layDeleteAccount.setOnClickListener(view -> new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.delete_account)).setMessage(R.string.do_you_really_want_delete_account).setPositiveButton(getResources().getString(R.string.continuee), (dialog, which) -> {

            //api for deleting account
            ActivityMain.getInstance().finish();
            Account.newInstance(this).clear();
            Intent intent = new Intent(this, ActivityLoginRegister.class);
            startActivity(intent);
            this.finish();

        }).setNegativeButton(getResources().getString(R.string.cancel), null).setIcon(R.drawable.ic_trash).show());

        b.icBack.setOnClickListener(view -> onBackPressed());

        b.layChooseLanguage.setOnClickListener(view -> showDialog());


    }

    private void wait(View view) {
        view.setEnabled(false);
        new Handler().postDelayed(() -> view.setEnabled(true), 200);
    }

    private void showDialog() {
        android.app.Dialog dialog1 = new android.app.Dialog(this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.bottomsheet_choose_language);

        ImageView checkEn = dialog1.findViewById(R.id.ic_check_en);
        ImageView checkRu = dialog1.findViewById(R.id.ic_check_ru);

        String currentLanguage = Account.newInstance(this).getLanguage();

        if (currentLanguage.equals("en")) {
            checkEn.setVisibility(View.VISIBLE);
            checkRu.setVisibility(View.GONE);
        } else {
            checkEn.setVisibility(View.GONE);
            checkRu.setVisibility(View.VISIBLE);
        }

        dialog1.findViewById(R.id.lang_ru).setOnClickListener(v -> {
            if (currentLanguage.equals("ru")) {
                dialog1.dismiss();
                return;
            }
            Account.newInstance(this).setLanguage("ru");
            setLocale();
            dialog1.dismiss();
        });

        dialog1.findViewById(R.id.lay_en).setOnClickListener(v -> {
            if (currentLanguage.equals("en")) {
                dialog1.dismiss();
                return;
            }
            Account.newInstance(this).setLanguage("en");
            setLocale();
            dialog1.dismiss();
        });

        dialog1.getWindow().setLayout(MATCH_PARENT, WRAP_CONTENT);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog1.getWindow().getAttributes().windowAnimations = R.style.DialogAnim;
        dialog1.getWindow().setGravity(Gravity.BOTTOM);
        dialog1.show();
    }

    private void resetTexts() {
        b.tvDeleteAccount.setText(R.string.delete_account);
        b.tvEditProfile.setText(R.string.edit_profile);
        b.tvHeader.setText(R.string.settings);
        b.tvLogout.setText(R.string.logout);
        b.tvLanguage.setText(R.string.change_language);
    }

    private void setLocale() {
        // Create a new Configuration object
        Configuration configuration = new Configuration();

        // Set the desired locale/language
        configuration.setLocale(new Locale(Account.newInstance(this).getLanguage()));

        // Apply the configuration to the resources
        Resources resources = getResources();
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

        isLangChanged = true;
        resetTexts();
    }

    @Override
    public void onBackPressed() {
        if (isLangChanged) {
            ActivityMain.getInstance().finish();
            Intent intent = new Intent(this, ActivityMain.class);
            intent.putExtra("is_language_changed", true);
            startActivity(intent);
            finish();
        } else
            super.onBackPressed();
    }
}