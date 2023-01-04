package com.example.playerslidding.fragment;

import static com.example.playerslidding.utils.Const.mainFragmentManager;
import static com.example.playerslidding.utils.FragmentHelper.addFragment;
import static com.example.playerslidding.utils.StaticMethods.dpToPx;
import static com.example.playerslidding.utils.StaticMethods.navigationBarHeight;
import static com.example.playerslidding.utils.StaticMethods.setBackgroundDrawable;
import static com.example.playerslidding.utils.StaticMethods.setMargins;
import static com.example.playerslidding.utils.StaticMethods.statusBarHeight;

import android.os.Bundle;
import android.os.Handler;
import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.example.playerslidding.R;
import com.example.playerslidding.adapter.AdapterCircle;
import com.example.playerslidding.adapter.AdapterGrid;
import com.example.playerslidding.databinding.FragmentProfileBinding;

public class FragmentProfile extends Fragment {
    private FragmentProfileBinding b;
    private AdapterCircle adapterCircle;
    private AdapterGrid adapterGrid;
    private String type;
    public final static String TYPE_USER = "user";
    public final static String TYPE_SHOP = "shop";

    public static FragmentProfile newInstance(String param) {
        FragmentProfile fragment = new FragmentProfile();
        Bundle args = new Bundle();
        args.putString("type", param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        setMargins(b.layHeader, 0, statusBarHeight, 0, 0);
        setMargins(b.coordinator, 0, 0, 0, navigationBarHeight);
        setMargins(b.frameFab, 0, 0, 0, navigationBarHeight);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() != null) {
            type = getArguments().getString("type");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentProfileBinding.inflate(inflater, container, false);
        check();
        adapterGrid.setStories(null);
        initListeners();
        return b.getRoot();
    }

    private void check() {
        setBackgrounds();

        switch (type) {
            case TYPE_USER:
                b.recCategories.setVisibility(View.GONE);
                b.textContent.setVisibility(View.GONE);
                b.layFollowsAndSubs.setVisibility(View.GONE);
                b.icMore.setVisibility(View.GONE);
                b.settings.setVisibility(View.VISIBLE);
                b.nameUser.setText("Майса гелнеджен");
                b.myBookmarks.setText("Избранное");
                b.myShops.setText("Мои магазины");
                b.countShops.setVisibility(View.VISIBLE);
                b.nextBtn.setImageResource(R.drawable.ic_ffrd);
                b.allSoldProducts.setText("Куплено за все время");
                b.allMessages.setText("Подписчики");
                b.allPosts.setText("Подписки");
                b.countBookmark.setVisibility(View.VISIBLE);
                b.editUser.setVisibility(View.VISIBLE);
                b.edit.setVisibility(View.GONE);
                b.frameFab.setVisibility(View.GONE);

                setMargins(b.layReply, dpToPx(20, getContext()), dpToPx(4, getContext()), dpToPx(20, getContext()), dpToPx(70, getContext()));

                break;
            case TYPE_SHOP:
                b.nameUser.setText("Вино Газиза Store");
                b.layFollowsAndSubs.setVisibility(View.VISIBLE);
                b.myBookmarks.setText("Настроить магазин");
                b.icMore.setVisibility(View.VISIBLE);
                b.settings.setVisibility(View.GONE);
                b.myShops.setText("Контакты");
                b.countShops.setVisibility(View.GONE);
                b.nextBtn.setImageResource(R.drawable.ic_person_arrow_right);
                b.allSoldProducts.setText("Место в общем рейтинге");
                b.allMessages.setText("Заказы");
                b.allPosts.setText("Посетителей");
                b.countBookmark.setVisibility(View.GONE);
                b.editUser.setVisibility(View.GONE);
                b.edit.setVisibility(View.VISIBLE);
                setMargins(b.layReply, dpToPx(20, getContext()), dpToPx(4, getContext()), dpToPx(20, getContext()), dpToPx(20, getContext()));
                b.recCategories.setVisibility(View.VISIBLE);
                b.textContent.setVisibility(View.GONE);
                b.frameFab.setVisibility(View.VISIBLE);

                setRecyclerCircle();
                adapterCircle.setStories(null);
                break;
        }
        setRecyclerProducts();

        Glide.with(getContext()).load("https://a.espncdn.com/photo/2022/1112/r1089820_1296x729_16-9.jpg").into(b.bigImage);
        Glide.with(getContext()).load("https://a.espncdn.com/photo/2022/1112/r1089820_1296x729_16-9.jpg").into(b.imageUser);
    }

    private void setRecyclerProducts() {
        adapterGrid = new AdapterGrid(getContext(), getActivity(), AdapterGrid.TYPE_GRID);
        b.recProducts.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        b.recProducts.setAdapter(adapterGrid);
    }

    private void initListeners() {
        b.layShops.setOnClickListener(v -> {
            b.layShops.setEnabled(false);
            addFragment(mainFragmentManager, R.id.fragment_container_main, FragmentMyShops.newInstance());
            new Handler().postDelayed(() -> b.layShops.setEnabled(true), 200);
        });

        b.layBack.setOnClickListener(v -> getActivity().onBackPressed());

        b.frameFab.setOnClickListener(v -> {
            b.frameFab.setEnabled(false);
            addFragment(mainFragmentManager, R.id.fragment_container_main, FragmentAddProduct.newInstance());
            new Handler().postDelayed(() -> b.frameFab.setEnabled(true), 200);
        });

    }


    private void setRecyclerCircle() {
        adapterCircle = new AdapterCircle(getContext(), getActivity());
        b.recCategories.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        b.recCategories.setAdapter(adapterCircle);
    }


    private void setBackgrounds() {
        setBackgroundDrawable(getContext(), b.backgroundSearch, R.color.white, 0, 10, false, 0);
        setBackgroundDrawable(getContext(), b.edtSearch, R.color.white, 0, 10, false, 0);
        setBackgroundDrawable(getContext(), b.layUserData, R.color.white, R.color.hover, 4, 4, 0, 0, false, 1);
        setBackgroundDrawable(getContext(), b.layShops, R.color.white, R.color.hover, 4, false, 1);
        setBackgroundDrawable(getContext(), b.layBookmark, R.color.white, R.color.hover, 0, 0, 4, 4, false, 1);
        setBackgroundDrawable(getContext(), b.layStatistics, R.color.hover, 0, 4, false, 0);
        setBackgroundDrawable(getContext(), b.layPosts, R.color.hover, 0, 4, false, 0);
        setBackgroundDrawable(getContext(), b.layFollows, R.color.white, 0, 4, false, 0);
        setBackgroundDrawable(getContext(), b.layMessages, R.color.hover, 0, 4, false, 0);
        setBackgroundDrawable(getContext(), b.laySubscribers, R.color.white, 0, 4, false, 0);
        setBackgroundDrawable(getContext(), b.frameFab, R.color.white, R.color.accent, 10, false, 2);
    }
}