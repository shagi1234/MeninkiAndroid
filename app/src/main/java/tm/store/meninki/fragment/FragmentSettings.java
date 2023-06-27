package tm.store.meninki.fragment;

import static tm.store.meninki.utils.Const.mainFragmentManager;
import static tm.store.meninki.utils.FragmentHelper.addFragment;
import static tm.store.meninki.utils.StaticMethods.navigationBarHeight;
import static tm.store.meninki.utils.StaticMethods.setPadding;
import static tm.store.meninki.utils.StaticMethods.statusBarHeight;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tm.store.meninki.R;
import tm.store.meninki.databinding.FragmentSettingsBinding;
import tm.store.meninki.utils.FragmentHelper;
import tm.store.meninki.utils.StaticMethods;

public class FragmentSettings extends Fragment {
    private FragmentSettingsBinding b;

    public static FragmentSettings newInstance() {
        FragmentSettings fragment = new FragmentSettings();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentSettingsBinding.inflate(inflater, container, false);
        initClickListeners();
        setBackgrounds();
        return b.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        setPadding(b.layHeader, 0, statusBarHeight, 0, 0);
        setPadding(b.getRoot(), 0, 0, 0, navigationBarHeight);
    }

    private void setBackgrounds() {
        StaticMethods.setBackgroundDrawable(requireContext(), b.layChooseLanguage, R.color.on_bg_ls, 0, 10, false, 0);
        StaticMethods.setBackgroundDrawable(requireContext(), b.layDeleteAccount, R.color.on_bg_ls, 0, 10, false, 0);
        StaticMethods.setBackgroundDrawable(requireContext(), b.layEdtProfile, R.color.on_bg_ls, 0, 10, false, 0);
        StaticMethods.setBackgroundDrawable(requireContext(), b.layLogOut, R.color.on_bg_ls, 0, 10, false, 0);
    }

    private void initClickListeners() {
        b.clickEdtProfile.setOnClickListener(view -> {
            wait(view);

            addFragment(mainFragmentManager, R.id.fragment_container_main, FragmentEditProfile.newInstance());
        });
    }

    private void wait(View view) {
        view.setEnabled(false);
        new Handler().postDelayed(() -> view.setEnabled(true), 200);
    }
}