package tm.store.meninki.fragment;

import static tm.store.meninki.api.enums.CardType.product;
import static tm.store.meninki.utils.Const.mainFragmentManager;
import static tm.store.meninki.utils.FragmentHelper.addFragment;
import static tm.store.meninki.utils.StaticMethods.getWindowHeight;
import static tm.store.meninki.utils.StaticMethods.navigationBarHeight;
import static tm.store.meninki.utils.StaticMethods.setBackgroundDrawable;
import static tm.store.meninki.utils.StaticMethods.setMargins;
import static tm.store.meninki.utils.StaticMethods.setPadding;
import static tm.store.meninki.utils.StaticMethods.statusBarHeight;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.appcompat.widget.TooltipCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import tm.store.meninki.R;
import tm.store.meninki.adapter.AdapterCharColor;
import tm.store.meninki.adapter.AdapterCharImage;
import tm.store.meninki.adapter.AdapterCharPick;
import tm.store.meninki.adapter.AdapterVerticalImagePager;
import tm.store.meninki.adapter.AdapterViewPager;
import tm.store.meninki.api.RetrofitCallback;
import tm.store.meninki.api.data.MediaDto;
import tm.store.meninki.api.data.ProductDetails;
import tm.store.meninki.data.CategoryDto;
import tm.store.meninki.data.FragmentPager;
import tm.store.meninki.data.ProductImageDto;
import tm.store.meninki.databinding.FragmentProductBinding;
import tm.store.meninki.shared.Account;
import tm.store.meninki.utils.StaticMethods;

public class FragmentProduct extends Fragment {
    private FragmentProductBinding b;
    private BottomSheetBehavior<View> bottomSheetBehavior;
    private ArrayList<ProductImageDto> s = new ArrayList<>();
    private ArrayList<String> images = new ArrayList<>();
    private ArrayList<String> picks = new ArrayList<>();
    private ArrayList<String> colors = new ArrayList<>();
    private String uuid;
    private int isStore;
    public final static int STORE = 0;
    public final static int PRODUCT = 1;
    private AdapterCharImage adapterCharImage;
    private AdapterCharPick adapterCharPick;
    private AdapterCharColor adapterCharColor;

    public static FragmentProduct newInstance(String uuid, int isStore) {
        FragmentProduct fragment = new FragmentProduct();
        Bundle args = new Bundle();
        args.putString("uuid", uuid);
        args.putInt("is_store", isStore);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            uuid = getArguments().getString("uuid");
            isStore = getArguments().getInt("is_store");
        }
        s.add(new ProductImageDto("https://cdn.dsmcdn.com/mnresize/500/-/ty384/product/media/images/20220405/17/83663989/437492006/1/1_org.jpg", "salam"));
        s.add(new ProductImageDto("https://cdn.dsmcdn.com/mnresize/500/-/ty384/product/media/images/20220405/17/83663989/437492006/1/1_org.jpg", "salam"));
        s.add(new ProductImageDto("https://cdn.dsmcdn.com/mnresize/500/-/ty384/product/media/images/20220405/17/83663989/437492006/1/1_org.jpg", "salam"));
        s.add(new ProductImageDto("https://cdn.dsmcdn.com/mnresize/500/-/ty384/product/media/images/20220405/17/83663989/437492006/1/1_org.jpg", "salam"));
        s.add(new ProductImageDto("https://cdn.dsmcdn.com/mnresize/500/-/ty384/product/media/images/20220405/17/83663989/437492006/1/1_org.jpg", "salam"));

        images.add("https://cdn.dsmcdn.com/mnresize/500/-/ty384/product/media/images/20220405/17/83663989/437492006/1/1_org.jpg");
        images.add("https://cdn.dsmcdn.com/mnresize/500/-/ty384/product/media/images/20220405/17/83663989/437492006/1/1_org.jpg");
        images.add("https://cdn.dsmcdn.com/mnresize/500/-/ty384/product/media/images/20220405/17/83663989/437492006/1/1_org.jpg");
        images.add("https://cdn.dsmcdn.com/mnresize/500/-/ty384/product/media/images/20220405/17/83663989/437492006/1/1_org.jpg");
        images.add("https://cdn.dsmcdn.com/mnresize/500/-/ty384/product/media/images/20220405/17/83663989/437492006/1/1_org.jpg");


        colors = new ArrayList<>();

        colors.add("#000213");
        colors.add("#005FF0");
        colors.add("#D90169");
        colors.add("#005FF0");
        colors.add("#D90169");
        colors.add("#000213");

        picks.add("XXL");
        picks.add("XL");
        picks.add("L");
        picks.add("M");
        picks.add("S");
        picks.add("XS");
    }

    @Override
    public void onResume() {
        super.onResume();
        setMargins(b.coordinator, 0, 0, 0, navigationBarHeight);
        setPadding(b.getRoot(), 0, statusBarHeight, 0, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentProductBinding.inflate(inflater, container, false);

        checkUI();

        setBackgrounds();

        initListeners();

        setBottomSheet();

        getData();

        return b.getRoot();
    }

    private void getData() {
        Call<ProductDetails> call = StaticMethods.getApiHome().getProductsById(Account.newInstance(getContext()).getAccessToken(), uuid);
        call.enqueue(new RetrofitCallback<ProductDetails>() {
            @Override
            public void onResponse(ProductDetails response) {
                if (response == null) {
                    return;
                }
                setResources(response);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("Error", "onFailure: " + t);
            }
        });
    }

    private void setResources(ProductDetails productDetails) {
        setImagePager(productDetails.getMedias());
        setViewPager(productDetails.getCategories());
        b.bottomSheet.itemName.setText(productDetails.getName());
        b.bottomSheet.desc.setText(productDetails.getDescription());
        b.bottomSheet.price.setText(String.valueOf(productDetails.getPrice()));
        b.bottomSheet.oldPrice.setText(String.valueOf(productDetails.getDiscountPrice()));
    }

    private void checkUI() {
        if (isStore == STORE) {
            b.bottomSheet.checkStore.setVisibility(View.GONE);
            b.bottomSheet.layBasket.setVisibility(View.GONE);
            b.bottomSheet.btnCaption.setVisibility(View.GONE);
        } else {
            b.bottomSheet.checkStore.setVisibility(View.VISIBLE);
            b.bottomSheet.layBasket.setVisibility(View.VISIBLE);
            b.bottomSheet.btnCaption.setVisibility(View.VISIBLE);

            setRecyclerCharImage();
            setRecyclerCharPick();
            setRecyclerColor();

        }
    }

    private void initListeners() {
        b.backBtn.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });

        b.bottomSheet.btnCaption.setOnClickListener(v -> {
            addFragment(mainFragmentManager, R.id.fragment_container_main, FragmentAddPost.newInstance());
        });

        b.bottomSheet.goBasket.setOnClickListener(v -> b.bottomSheet.layCountControl.setVisibility(View.VISIBLE));

        b.bottomSheet.add.setOnClickListener(v -> {
            int i = Integer.parseInt(b.bottomSheet.countProduct.getText().toString().trim());
            b.bottomSheet.countProduct.setText(String.valueOf(i + 1));
        });
        b.bottomSheet.subs.setOnClickListener(v -> {
            int i = Integer.parseInt(b.bottomSheet.countProduct.getText().toString().trim());
            b.bottomSheet.countProduct.setText(String.valueOf(i - 1));
        });

    }

    private void setBackgrounds() {
        setBackgroundDrawable(getContext(), b.bottomSheet.txtGoBasket, R.color.accent, 0, 4, false, 0);

        setBackgroundDrawable(getContext(), b.bottomSheet.layCountControl, R.color.grey, 0, 4, false, 0);
        setBackgroundDrawable(getContext(), b.bottomSheet.txtBtnCaption, R.color.white, 0, 4, false, 0);
    }

    private void setImagePager(ArrayList<MediaDto> medias) {
        AdapterVerticalImagePager adapterVerticalImagePager = new AdapterVerticalImagePager(getContext());
        b.imagePager.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getWindowHeight(getActivity()) * 55 / 75));

        b.imagePager.setClipToPadding(false);
        b.imagePager.setClipChildren(false);
        b.imagePager.setOffscreenPageLimit(3);
        b.imagePager.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        b.imagePager.setAdapter(adapterVerticalImagePager);

        adapterVerticalImagePager.setImageList(medias);
        b.indicator.setViewPager(b.imagePager);


    }

    private void setBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(b.bottomSheet.root);
        bottomSheetBehavior.setHideable(false);
        bottomSheetBehavior.setPeekHeight(getWindowHeight(getActivity()) * 20 / 75);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    private void setRecyclerCharImage() {
        adapterCharImage = new AdapterCharImage(getContext(), AdapterCharPick.NOT_ADDABLE);
        b.bottomSheet.recCharImage.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        b.bottomSheet.recCharImage.setAdapter(adapterCharImage);
    }

    private void setRecyclerCharPick() {
        adapterCharPick = new AdapterCharPick(getContext(), AdapterCharPick.NOT_ADDABLE);
        b.bottomSheet.recCharPick.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        b.bottomSheet.recCharPick.setAdapter(adapterCharPick);
    }

    private void setRecyclerColor() {
        adapterCharColor = new AdapterCharColor(getContext(), AdapterCharPick.NOT_ADDABLE);
        b.bottomSheet.recCharColor.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        b.bottomSheet.recCharColor.setAdapter(adapterCharColor);
    }

    private void setViewPager(ArrayList<CategoryDto> categories) {
        String[] categoryIds = new String[categories.size()];

        for (int i = 0; i < categories.size(); i++) {
            categoryIds[i] = categories.get(i).getId();
        }

        b.bottomSheet.tabLayout.setupWithViewPager(b.bottomSheet.viewPager);

        b.bottomSheet.tabLayout.setTabRippleColor(null);

        b.bottomSheet.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int tabPosition = tab.getPosition(); // syntactic sugar
                b.bottomSheet.viewPager.setCurrentItem(tabPosition, true);
                // Repeat of the code above -- tooltips reset themselves after any tab relayout, so I
                // have to constantly keep turning them off again.
                for (int i = 0; i < b.bottomSheet.tabLayout.getTabCount(); i++) {
                    TooltipCompat.setTooltipText(Objects.requireNonNull(b.bottomSheet.tabLayout.getTabAt(i)).view, null);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        b.bottomSheet.viewPager.setOffscreenPageLimit(2);
        ArrayList<FragmentPager> mFragment = new ArrayList<>();
        mFragment.add(new FragmentPager(FragmentListGrid.newInstance(FragmentListGrid.HORIZONTAL_LINEAR, FragmentListGrid.CATEGORY, -1, categoryIds, new int[]{product}), "Похожие".toUpperCase()));

        AdapterViewPager adapterFeedPager = new AdapterViewPager(getChildFragmentManager(), mFragment);
        b.bottomSheet.viewPager.setAdapter(adapterFeedPager);

    }

}