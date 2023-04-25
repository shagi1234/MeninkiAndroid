package tm.store.meninki.fragment;

import static tm.store.meninki.adapter.AdapterPostPager.lastExoPlayer;
import static tm.store.meninki.utils.StaticMethods.navigationBarHeight;
import static tm.store.meninki.utils.StaticMethods.statusBarHeight;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;

import tm.store.meninki.adapter.AdapterPostPager;
import tm.store.meninki.data.VideoDto;
import tm.store.meninki.databinding.FragmentPostBinding;
import tm.store.meninki.interfaces.OnBackPressedFragment;
import tm.store.meninki.utils.StaticMethods;

public class FragmentPost extends Fragment implements OnBackPressedFragment {
    private FragmentPostBinding b;
    private AdapterPostPager adapterViewPager;

    public static FragmentPost newInstance() {
        FragmentPost fragment = new FragmentPost();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (lastExoPlayer != null)
            lastExoPlayer.pause();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (lastExoPlayer != null)
            lastExoPlayer.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (lastExoPlayer != null)
            lastExoPlayer.pause();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentPostBinding.inflate(inflater, container, false);
        setViewPager(new ArrayList<>());
        return b.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (lastExoPlayer != null)
            lastExoPlayer.play();
        new Handler().post(() -> StaticMethods.setMargins(b.viewPager2, 0, statusBarHeight, 0, navigationBarHeight));
    }

    private void setViewPager(ArrayList<VideoDto> videos) {
        videos.add(new VideoDto(" https://messenger.salam.tm/public/feeds/9f3a4787-c690-49c9-a05d-e5338c42feb8/0c795df2-1a0d-48b2-8282-fa750475cfbf/video/1679325193612463892.mp4", "", "", ""));
        videos.add(new VideoDto("https://messenger.salam.tm/public/feeds/9ea3b96d-40c6-42a0-853b-c4575db161bc/d530b829-289a-4777-a2b7-47fd1b3c5f97/video/1678730540813576501.mp4", "", "", ""));
        videos.add(new VideoDto("https://messenger.salam.tm/public/feeds/f56b67bf-9773-43d6-a049-3aad6c74f4db/7f7debb6-1c50-4b83-aa98-2b834a3cf2e2/video/1678730533745664208.mp4", "", "", ""));
        videos.add(new VideoDto("https://messenger.salam.tm/public/feeds/81b9f167-c0b1-4034-bc71-4f2e432acd6c/2881f48f-a997-4ff0-8504-f1eabb87359b/video/1678730430800274323.mp4", "", "", ""));
        videos.add(new VideoDto("https://messenger.salam.tm/public/feeds/d01ba907-5232-4a6b-8ce0-499519e9609d/5f831a7d-a9a9-4aa5-9ea3-f1a099f2fea6/video/1678732221679413388.mp4", "", "", ""));
        videos.add(new VideoDto("https://messenger.salam.tm/public/feeds/81b9f167-c0b1-4034-bc71-4f2e432acd6c/2881f48f-a997-4ff0-8504-f1eabb87359b/video/1678730430800274323.mp4", "", "", ""));
        videos.add(new VideoDto("https://messenger.salam.tm/public/feeds/ef3aadc4-67d7-4c6e-be57-a9c36e5a7b20/e989642d-f1f6-4edf-a2d8-2c479dfdbe7b/video/1678730266439149339.mp4", "", "", ""));
        videos.add(new VideoDto("https://messenger.salam.tm/public/feeds/d01ba907-5232-4a6b-8ce0-499519e9609d/5f831a7d-a9a9-4aa5-9ea3-f1a099f2fea6/video/1678732221679413388.mp4", "", "", ""));
        videos.add(new VideoDto("https://messenger.salam.tm/public/feeds/0ccbf713-cc11-4e71-a357-d53f3e0fee73/fcbdbb0e-21cf-46ce-9538-3920bb72409b/video/1679300115375636113.mp4", "", "", ""));
        videos.add(new VideoDto("https://messenger.salam.tm/public/feeds/36393101-1208-4616-af9c-4c93ec11ad78/82ba27d2-756b-4d89-80a3-a6569ff1ed0f/video/1679318982672216070.mp4", "", "", ""));

        b.viewPager2.setOffscreenPageLimit(1);
        adapterViewPager = new AdapterPostPager(getContext(), getActivity(), videos, b.viewPager2);
        b.viewPager2.setAdapter(adapterViewPager);
        b.viewPager2.setPageTransformer(new ZoomOutPageTransformer());

        b.viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                adapterViewPager.setPosition(position);
                adapterViewPager.releasePlayer();
            }
        });
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
                view.setAlpha(MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) /
                                (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0f);
            }
        }
    }

}