package tm.store.meninki.fragment;

import static tm.store.meninki.adapter.AdapterPostPager.lastExoPlayer;
import static tm.store.meninki.utils.StaticMethods.dpToPx;
import static tm.store.meninki.utils.StaticMethods.navigationBarHeight;
import static tm.store.meninki.utils.StaticMethods.statusBarHeight;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import tm.store.meninki.adapter.AdapterPostPager;
import tm.store.meninki.api.data.ResponsePostGetAllItem;
import tm.store.meninki.databinding.FragmentPostBinding;
import tm.store.meninki.interfaces.OnBackPressedFragment;
import tm.store.meninki.interfaces.OnPostSlided;
import tm.store.meninki.utils.Const;
import tm.store.meninki.utils.StaticMethods;

public class FragmentPost extends Fragment implements OnBackPressedFragment {
    private FragmentPostBinding b;
    private AdapterPostPager adapterViewPager;
    private int adapterPosition;
    private ArrayList<ResponsePostGetAllItem> posts;
    private String currentUserId = "";

    public static FragmentPost newInstance(ArrayList<ResponsePostGetAllItem> posts, int adapterPosition) {
        FragmentPost fragment = new FragmentPost();
        Bundle args = new Bundle();
        args.putString("posts_json", new Gson().toJson(posts));
        args.putInt("position", adapterPosition);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (lastExoPlayer != null) lastExoPlayer.pause();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (lastExoPlayer != null) lastExoPlayer.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (lastExoPlayer != null) lastExoPlayer.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (lastExoPlayer != null) lastExoPlayer.play();

        new Handler().post(() -> StaticMethods.setMargins(b.viewPager2, 0, statusBarHeight, 0, navigationBarHeight));

        new Handler().postDelayed(() -> StaticMethods.setMargins(b.bgBack, dpToPx(14, getContext()), dpToPx(14, getContext()) + statusBarHeight, 0, 0),100);
    }

    @Override
    public boolean onBackPressed() {
        if (lastExoPlayer != null) {
            lastExoPlayer.pause();
            lastExoPlayer.release();
            lastExoPlayer = null;
        }

        return false;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Type type = new TypeToken<ArrayList<ResponsePostGetAllItem>>() {
            }.getType();
            posts = new Gson().fromJson(getArguments().getString("posts_json"), type);
            adapterPosition = getArguments().getInt("position");

            Log.e("TAG_video", "onCreate: " + new Gson().toJson(posts.get(adapterPosition)));
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentPostBinding.inflate(inflater, container, false);
        setViewPager(posts);
        b.backBtn.setOnClickListener(view -> getActivity().onBackPressed());
        return b.getRoot();
    }


    private void setViewPager(ArrayList<ResponsePostGetAllItem> videos) {
        b.viewPager2.setOffscreenPageLimit(1);
        adapterViewPager = new AdapterPostPager(getContext(), getActivity(), videos, b.viewPager2);
        b.viewPager2.setAdapter(adapterViewPager);
//        b.viewPager2.setPageTransformer(new ZoomOutPageTransformer());
        passId(0, videos.get(0).getUser().getId());

        b.viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                new Handler().postDelayed(() -> {
                    adapterViewPager.setPosition(position);
                    adapterViewPager.releasePlayer();
                }, 300);

                passId(position, videos.get(position).getUser().getId());
            }
        });

        b.viewPager2.setCurrentItem(adapterPosition, false);
    }

    private void passId(int position, String id) {
        Fragment fragment = Const.mainFragmentManager.findFragmentByTag(FragmentProfileViewPager.class.getName());

        if (fragment instanceof OnPostSlided) {
            ((OnPostSlided) fragment).onPostSlided(position, id);
        }
    }

    public class ZoomOutPageTransformer implements ViewPager2.PageTransformer {

        private final float MIN_SCALE = 0.97f;
        private final float MIN_ALPHA = 1;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0f);

            } else if (position <= 1) { // [-1,1]
                // Modify the default slide transition to shrink the page as well.
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1).
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0f);
            }
        }
    }

}