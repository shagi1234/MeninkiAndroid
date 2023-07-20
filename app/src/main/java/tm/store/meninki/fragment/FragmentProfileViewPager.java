package tm.store.meninki.fragment;

import static tm.store.meninki.utils.StaticMethods.navigationBarHeight;
import static tm.store.meninki.utils.StaticMethods.statusBarHeight;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import tm.store.meninki.R;
import tm.store.meninki.adapter.AdapterViewPager;
import tm.store.meninki.adapter.SlidePagerAdapter;
import tm.store.meninki.api.data.ResponsePostGetAllItem;
import tm.store.meninki.data.FragmentPager;
import tm.store.meninki.databinding.FragmentProfileViewPagerBinding;
import tm.store.meninki.interfaces.OnPostSlided;
import tm.store.meninki.interfaces.OnProfileOpened;
import tm.store.meninki.utils.Const;
import tm.store.meninki.utils.FragmentHelper;
import tm.store.meninki.utils.StaticMethods;


public class FragmentProfileViewPager extends Fragment implements OnPostSlided {
    private FragmentProfileViewPagerBinding b;
    private SlidePagerAdapter adapterViewPager;

    private ArrayList<ResponsePostGetAllItem> posts;
    private String userId;
    int adapterPosition;

    @Override
    public void onResume() {
        super.onResume();
        new Handler().post(() -> StaticMethods.setMargins(b.getRoot(), 0, statusBarHeight, 0, navigationBarHeight));
    }

    public static FragmentProfileViewPager newInstance(ArrayList<ResponsePostGetAllItem> posts, int adapterPosition, String userId) {
        FragmentProfileViewPager fragment = new FragmentProfileViewPager();
        Bundle args = new Bundle();
        args.putInt("pos", adapterPosition);
        args.putString("posts_json_", new Gson().toJson(posts));
        args.putString("user_id_", userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            adapterPosition = getArguments().getInt("pos");
            Type type = new TypeToken<ArrayList<ResponsePostGetAllItem>>() {
            }.getType();
            posts = new Gson().fromJson(getArguments().getString("posts_json_"), type);
            userId = getArguments().getString("user_id_");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        b = FragmentProfileViewPagerBinding.inflate(inflater, container, false);
        setViewPager();
        return b.getRoot();
    }

    private void setViewPager() {
        adapterViewPager = new SlidePagerAdapter(getChildFragmentManager());
        adapterViewPager.addFragment(FragmentPost.newInstance(posts, adapterPosition));
        adapterViewPager.addFragment(FragmentProfile.newInstance(FragmentProfile.TYPE_USER, userId));
        b.viewPager.setAdapter(adapterViewPager);

        b.viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position != 0) {
                    Fragment fragment=getFragmentAtPosition(position);

                    if (fragment instanceof OnProfileOpened) {
                        ((OnProfileOpened) fragment).onOpen(userId);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    private Fragment getFragmentAtPosition(int position) {
        SlidePagerAdapter adapter = (SlidePagerAdapter) b.viewPager.getAdapter();
        return adapter.getItem(position);
    }

    @Override
    public void onPostSlided(int adapterPosition, String userId) {
        this.userId = userId;
        Log.e("USER ID", "onPostSlided: " + userId);
    }
}