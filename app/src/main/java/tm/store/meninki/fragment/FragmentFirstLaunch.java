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

import androidx.fragment.app.Fragment;

import tm.store.meninki.R;
import tm.store.meninki.databinding.FragmentFirstLaunchBinding;
import tm.store.meninki.utils.StaticMethods;

public class FragmentFirstLaunch extends Fragment {
    private FragmentFirstLaunchBinding b;

    public static FragmentFirstLaunch newInstance() {
        FragmentFirstLaunch fragment = new FragmentFirstLaunch();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentFirstLaunchBinding.inflate(inflater, container, false);
        setBackgrounds();
        initListeners();
        return b.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        new Handler().postDelayed(() -> StaticMethods.setPadding(b.getRoot(), 0, statusBarHeight, 0, navigationBarHeight), 100);
    }

    private void initListeners() {
        b.nextBtn.setOnClickListener(v -> {
            b.nextBtn.setEnabled(false);
            addFragment(mainFragmentManager, R.id.container_login, FragmentLanguage.newInstance());
            new Handler().postDelayed(() -> b.nextBtn.setEnabled(true), 200);
        });
    }

    private void setBackgrounds() {
        setBackgroundDrawable(getContext(), b.nextBtn, R.color.accent, 0, 10, false, 0);
    }
}