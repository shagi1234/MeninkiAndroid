package tm.store.meninki.fragment;

import static tm.store.meninki.utils.StaticMethods.setBackgroundDrawable;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import tm.store.meninki.R;
import tm.store.meninki.adapter.AdapterViewPager;
import tm.store.meninki.data.FragmentPager;
import tm.store.meninki.databinding.FragmentMainBinding;

public class FragmentMain extends Fragment {
    private FragmentMainBinding b;
    private AdapterViewPager adapterFeedPager;

    public static FragmentMain newInstance() {
        FragmentMain fragment = new FragmentMain();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("TAG_fragment", "onCreate: main");

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentMainBinding.inflate(inflater, container, false);
        setViewPager();
        setBackgrounds();
        initListeners();

        return b.getRoot();
    }

    private void setBackgrounds() {
        setBackgroundDrawable(getContext(), b.tabLayout, R.color.on_bg_ls, 0, 100, false, 0);
        setBackgroundDrawable(getContext(), b.bgTab, R.color.on_bg_ls, 0, 100, false, 0);
    }


    private void initListeners() {
    }

    private void setViewPager() {

        b.tabLayout.setupWithViewPager(b.viewPager);
        b.tabLayout.setTabRippleColor(null);

        b.viewPager.setOffscreenPageLimit(2);
        ArrayList<FragmentPager> mFragment = new ArrayList<>();

        mFragment.add(new FragmentPager(FragmentFeed.newInstance(), getString(R.string.feed)));
        mFragment.add(new FragmentPager(FragmentHome.newInstance(), getString(R.string.home)));
        mFragment.add(new FragmentPager(FragmentAdvertisements.newInstance(), getString(R.string.Ads)));

        adapterFeedPager = new AdapterViewPager(getChildFragmentManager(), mFragment);
        b.viewPager.setAdapter(adapterFeedPager);

    }
}