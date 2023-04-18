package tm.store.meninki.fragment;

import static tm.store.meninki.utils.Const.mainFragmentManager;
import static tm.store.meninki.utils.FragmentHelper.addFragment;
import static tm.store.meninki.utils.StaticMethods.navigationBarHeight;
import static tm.store.meninki.utils.StaticMethods.setBackgroundDrawable;
import static tm.store.meninki.utils.StaticMethods.setPadding;
import static tm.store.meninki.utils.StaticMethods.statusBarHeight;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import tm.store.meninki.R;
import tm.store.meninki.databinding.FragmentLanguageBinding;
import tm.store.meninki.interfaces.CountryClickListener;
import tm.store.meninki.utils.StaticMethods;

public class FragmentLanguage extends Fragment implements CountryClickListener {
    private FragmentLanguageBinding b;

    public static FragmentLanguage newInstance() {
        FragmentLanguage fragment = new FragmentLanguage();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        StaticMethods.setPadding(b.getRoot(), 0, statusBarHeight, 0, navigationBarHeight);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentLanguageBinding.inflate(inflater, container, false);
        setBackgrounds();
        initListeners();
        return b.getRoot();
    }

    private void initListeners() {
        b.btnNext.setOnClickListener(v -> {
            b.btnNext.setEnabled(false);
            addFragment(mainFragmentManager, R.id.container_login, FragmentCountryAndNumber.newInstance());
            new Handler().postDelayed(() -> b.btnNext.setEnabled(true), 200);
        });

        b.selectLanguage.setOnClickListener(v -> {
            b.selectLanguage.setEnabled(false);
            addFragment(mainFragmentManager, R.id.container_login, FragmentCountryCode.newInstance(FragmentCountryCode.TYPE_LANGUAGE));
            new Handler().postDelayed(() -> b.selectLanguage.setEnabled(true), 200);

        });
    }

    private void setBackgrounds() {
        setBackgroundDrawable(getContext(), b.btnNext, R.color.accent, 0, 10, false, 0);
        setBackgroundDrawable(getContext(), b.selectLanguage, R.color.neutral_dark, 0, 10, false, 0);
    }

    @Override
    public void countryClick(String name, String code) {
        b.selectLanguage.setText(name);
    }
}