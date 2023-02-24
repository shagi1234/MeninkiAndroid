package tm.store.meninki.fragment;

import static tm.store.meninki.utils.Const.mainFragmentManager;
import static tm.store.meninki.utils.FragmentHelper.addFragment;
import static tm.store.meninki.utils.StaticMethods.navigationBarHeight;
import static tm.store.meninki.utils.StaticMethods.setBackgroundDrawable;
import static tm.store.meninki.utils.StaticMethods.setPadding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import tm.store.meninki.R;
import tm.store.meninki.adapter.AdapterStore;
import tm.store.meninki.adapter.AdapterViewPager;
import tm.store.meninki.data.FragmentPager;
import tm.store.meninki.databinding.FragmentHomeBinding;

import java.util.ArrayList;

public class FragmentFeed extends Fragment {
    private FragmentHomeBinding b;
    private AdapterViewPager adapterFeedPager;
    private AdapterStore adapterStore;

    public static FragmentFeed newInstance() {
        FragmentFeed fragment = new FragmentFeed();
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
        setPadding(b.containerProfileId, 0, 0, 0, navigationBarHeight);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentHomeBinding.inflate(inflater, container, false);

        setBackground();
        setRecycler();
        setViewPager();
        initListeners();

        adapterStore.setStories(null);

        return b.getRoot();
    }

    private void initListeners() {
        b.filter.setOnClickListener(view -> addFragment(mainFragmentManager, R.id.fragment_container_main, FragmentFilterAndSort.newInstance()));
    }

    private void setViewPager() {
        ArrayList<FragmentPager> mFragment = new ArrayList<>();

        mFragment.add(new FragmentPager(FragmentListGrid.newInstance(FragmentListGrid.VERTICAL_GRID, "", FragmentListGrid.POPULAR), "Домашняя"));

        adapterFeedPager = new AdapterViewPager(getChildFragmentManager(), mFragment);
        b.viewPager.setAdapter(adapterFeedPager);
    }

    private void setBackground() {
        setBackgroundDrawable(getContext(), b.backgroundSearch, R.color.white, 0, 10, false, 0);
        setBackgroundDrawable(getContext(), b.edtSearch, R.color.white, 0, 10, false, 0);
    }

    private void setRecycler() {
        adapterStore = new AdapterStore(getContext(), getActivity());
        b.recStores.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        b.recStores.setAdapter(adapterStore);
    }

}