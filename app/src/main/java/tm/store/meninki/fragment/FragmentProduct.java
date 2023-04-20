package tm.store.meninki.fragment;

import static tm.store.meninki.utils.Const.mainFragmentManager;
import static tm.store.meninki.utils.FragmentHelper.addFragment;
import static tm.store.meninki.utils.StaticMethods.getWindowHeight;
import static tm.store.meninki.utils.StaticMethods.navigationBarHeight;
import static tm.store.meninki.utils.StaticMethods.setBackgroundDrawable;
import static tm.store.meninki.utils.StaticMethods.setMargins;
import static tm.store.meninki.utils.StaticMethods.statusBarHeight;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import retrofit2.Call;
import tm.store.meninki.R;
import tm.store.meninki.adapter.AdapterCharColor;
import tm.store.meninki.adapter.AdapterCharImage;
import tm.store.meninki.adapter.AdapterCharPick;
import tm.store.meninki.adapter.AdapterVerticalImagePager;
import tm.store.meninki.api.RetrofitCallback;
import tm.store.meninki.api.data.MediaDto;
import tm.store.meninki.api.data.ProductDetails;
import tm.store.meninki.data.ProductImageDto;
import tm.store.meninki.databinding.FragmentProductBinding;
import tm.store.meninki.shared.Account;
import tm.store.meninki.utils.StaticMethods;

public class FragmentProduct extends Fragment {
    private FragmentProductBinding b;
    private ArrayList<ProductImageDto> s = new ArrayList<>();
    private ArrayList<String> images = new ArrayList<>();
    private ArrayList<String> picks = new ArrayList<>();
    private ArrayList<String> colors = new ArrayList<>();
    private String uuid;
    public final static int STORE = 0;
    public final static int PRODUCT = 1;
    private AdapterCharImage adapterCharImage;
    private AdapterCharPick adapterCharPick;
    private AdapterCharColor adapterCharColor;

    public static FragmentProduct newInstance(String uuid) {
        FragmentProduct fragment = new FragmentProduct();
        Bundle args = new Bundle();
        args.putString("uuid", uuid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            uuid = getArguments().getString("uuid");
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
        StaticMethods.setPadding(b.getRoot(), 0, statusBarHeight, 0, 0);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentProductBinding.inflate(inflater, container, false);

        checkUI();

        setBackgrounds();

        initListeners();

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
        b.itemName.setText(productDetails.getName());
        b.desc.setText(productDetails.getDescription());
        b.price.setText(String.valueOf(productDetails.getPrice()));
    }

    private void checkUI() {
        b.checkStore.setVisibility(View.VISIBLE);
        b.btnColor.setVisibility(View.VISIBLE);

        setRecyclerCharImage();
        setRecyclerCharPick();
    }

    private void initListeners() {
        b.backBtn.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });

        b.btnColor.setOnClickListener(v -> {
            addFragment(mainFragmentManager, R.id.fragment_container_main, FragmentAddPost.newInstance());
        });

        b.goCard.setOnClickListener(v -> {

        });

//        b.add.setOnClickListener(v -> {
//            int i = Integer.parseInt(b.countProduct.getText().toString().trim());
//            b.countProduct.setText(String.valueOf(i + 1));
//        });
//        b.subs.setOnClickListener(v -> {
//            int i = Integer.parseInt(b.countProduct.getText().toString().trim());
//            b.countProduct.setText(String.valueOf(i - 1));
//        });

    }

    private void setBackgrounds() {
        setBackgroundDrawable(getContext(), b.bgFav, R.color.bg, 0, 10, false, 0);
        setBackgroundDrawable(getContext(), b.bgGoCard, R.color.contrast, 0, 10, false, 0);

        setBackgroundDrawable(getContext(), b.layBtnColor, R.color.contrast, 0, 50, false, 0);
    }

    private void setImagePager(ArrayList<MediaDto> medias) {
        AdapterVerticalImagePager adapterVerticalImagePager = new AdapterVerticalImagePager(getContext());

        b.imagePager.setClipToPadding(false);
        b.imagePager.setClipChildren(false);
        b.imagePager.setOffscreenPageLimit(3);
        b.imagePager.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        b.imagePager.setAdapter(adapterVerticalImagePager);

        adapterVerticalImagePager.setImageList(medias);
        b.indicator.setViewPager(b.imagePager);


    }

    private void setRecyclerCharImage() {
        adapterCharImage = new AdapterCharImage(getContext(), AdapterCharPick.NOT_ADDABLE);
        b.recCharImage.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        b.recCharImage.setAdapter(adapterCharImage);
    }

    private void setRecyclerCharPick() {
        adapterCharPick = new AdapterCharPick(getContext(), AdapterCharPick.NOT_ADDABLE);
        b.recCharPick.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        b.recCharPick.setAdapter(adapterCharPick);
    }

}