package tm.store.meninki.fragment;

import static tm.store.meninki.fragment.FragmentListGrid.VERTICAL_GRID;
import static tm.store.meninki.utils.StaticMethods.navigationBarHeight;
import static tm.store.meninki.utils.StaticMethods.setBackgroundDrawable;
import static tm.store.meninki.utils.StaticMethods.setPadding;
import static tm.store.meninki.utils.StaticMethods.statusBarHeight;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import tm.store.meninki.R;
import tm.store.meninki.adapter.AdapterViewPager;
import tm.store.meninki.api.request.RequestAllAdvertisement;
import tm.store.meninki.data.FragmentPager;
import tm.store.meninki.databinding.FragmentMainBinding;
import tm.store.meninki.interfaces.GetAllAdvertisement;
import tm.store.meninki.interfaces.OnUpdateFragment;
import tm.store.meninki.utils.StaticMethods;

public class FragmentMain extends Fragment implements OnUpdateFragment {
    private FragmentMainBinding b;
    private AdapterViewPager adapterFeedPager;
    private boolean isSearch;
    public static EditText editSearch;

    public static FragmentMain newInstance(boolean isSearch) {
        FragmentMain fragment = new FragmentMain();
        Bundle args = new Bundle();
        args.putBoolean("is_search", isSearch);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("TAG_fragment", "onCreate: main");
        if (getArguments() != null) {
            isSearch = getArguments().getBoolean("is_search", isSearch);
        }

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentMainBinding.inflate(inflater, container, false);
        editSearch = b.edtSearch;
        isSearched();
        setViewPager();
        setBackgrounds();
        return b.getRoot();
    }

    private void isSearched() {
        if (isSearch) {
            b.backgroundSearch.setVisibility(View.VISIBLE);
            StaticMethods.showKeyboard(getContext(), b.edtSearch);
        } else {
            b.backgroundSearch.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isSearch) {
            setPadding(b.getRoot(), 0, statusBarHeight, 0, navigationBarHeight);
        }
    }

    private void setBackgrounds() {
        setBackgroundDrawable(getContext(), b.tabLayout, R.color.on_bg_ls, 0, 100, false, 0);
        setBackgroundDrawable(getContext(), b.bgTab, R.color.on_bg_ls, 0, 100, false, 0);
    }

    private void setViewPager() {

        b.tabLayout.setupWithViewPager(b.viewPager);
        b.tabLayout.setTabRippleColor(null);

        b.viewPager.setOffscreenPageLimit(2);
        ArrayList<FragmentPager> mFragment = new ArrayList<>();
        if (isSearch) {
            mFragment.add(new FragmentPager(FragmentFeed.newInstance(true), getString(R.string.posts)));
            mFragment.add(new FragmentPager(FragmentListGrid.newInstance(VERTICAL_GRID, FragmentListGrid.SEARCH_PRODUCT, 20, null, new int[]{0, 1, 2, 3}), getString(R.string.products)));
            mFragment.add(new FragmentPager(FragmentAdvertisements.newInstance(true), getString(R.string.Ads)));
        } else {
            mFragment.add(new FragmentPager(FragmentFeed.newInstance(false), getString(R.string.feed)));
            mFragment.add(new FragmentPager(FragmentHome.newInstance(), getString(R.string.home)));
            mFragment.add(new FragmentPager(FragmentAdvertisements.newInstance(false), getString(R.string.Ads)));
        }

        adapterFeedPager = new AdapterViewPager(getChildFragmentManager(), mFragment);
        b.viewPager.setAdapter(adapterFeedPager);

    }

    @Override
    public void onUpdated(RequestAllAdvertisement requestAllAdvertisement) {
        Fragment fragment = adapterFeedPager.getItem(2);
        if (fragment instanceof GetAllAdvertisement) {
            ((GetAllAdvertisement) fragment).getAllAds(requestAllAdvertisement);
        }
    }
}