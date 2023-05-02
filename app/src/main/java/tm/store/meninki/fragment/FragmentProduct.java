package tm.store.meninki.fragment;

import static tm.store.meninki.api.Network.BASE_URL;
import static tm.store.meninki.utils.Const.mainFragmentManager;
import static tm.store.meninki.utils.FragmentHelper.addFragment;
import static tm.store.meninki.utils.StaticMethods.navigationBarHeight;
import static tm.store.meninki.utils.StaticMethods.setBackgroundDrawable;
import static tm.store.meninki.utils.StaticMethods.setMargins;
import static tm.store.meninki.utils.StaticMethods.statusBarHeight;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tm.store.meninki.R;
import tm.store.meninki.adapter.AdapterCharImage;
import tm.store.meninki.adapter.AdapterCharPick;
import tm.store.meninki.adapter.AdapterVerticalImagePager;
import tm.store.meninki.api.RetrofitCallback;
import tm.store.meninki.api.data.MediaDto;
import tm.store.meninki.api.data.ProductDetails;
import tm.store.meninki.api.request.RequestAddToCard;
import tm.store.meninki.databinding.FragmentProductBinding;
import tm.store.meninki.shared.Account;
import tm.store.meninki.utils.StaticMethods;

public class FragmentProduct extends Fragment {
    private FragmentProductBinding b;
    private String uuid;
    private String avatarId;
    private AdapterCharImage adapterCharImage;
    private AdapterCharPick adapterCharPick;
    private ProductDetails productDetails;

    public static FragmentProduct newInstance(String uuid, String avatarId) {
        FragmentProduct fragment = new FragmentProduct();
        Bundle args = new Bundle();
        args.putString("uuid", uuid);
        args.putString("shop_id", avatarId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            uuid = getArguments().getString("uuid");
            avatarId = getArguments().getString("avatar_id");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setMargins(b.getRoot(), 0, statusBarHeight, 0, navigationBarHeight);
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
        this.productDetails = productDetails;
        setImagePager(productDetails.getMedias());
        b.itemName.setText(productDetails.getName());
        b.desc.setText(productDetails.getDescription());
        b.titleStore.setText(productDetails.getShop().getName());

        Glide.with(getContext())
                .load(BASE_URL + productDetails.getShop().getImgPath())
                .into(b.avatarStore);

        b.price.setText(productDetails.getPrice() + " TMT");
//        adapterCharImage.setOptions(productDetails.getOptions());
        b.progressBar.setVisibility(View.GONE);
        b.main.setVisibility(View.VISIBLE);
    }

    private void checkUI() {
        b.checkStore.setVisibility(View.VISIBLE);
        b.btnAddPost.setVisibility(View.VISIBLE);

        setRecyclerCharImage();
        setRecyclerCharText();
    }

    private void initListeners() {
        b.backBtn.setOnClickListener(v -> getActivity().onBackPressed());

        b.btnAddPost.setOnClickListener(v -> addFragment(mainFragmentManager, R.id.fragment_container_main, FragmentAddPost.newInstance(uuid, productDetails.getName(), new Gson().toJson(productDetails.getShop()))));

        b.goCard.setOnClickListener(v -> addToCard());

    }

    private void addToCard() {
        RequestAddToCard requestAddToCard = new RequestAddToCard();
        requestAddToCard.setProductId(uuid);
        requestAddToCard.setShopId(avatarId);
        requestAddToCard.setCount(1);
        Call<Boolean> call = StaticMethods.getApiHome().addToCard(requestAddToCard);

        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.body() != null && response.body()) {
                    Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setBackgrounds() {
        setBackgroundDrawable(getContext(), b.bgFav, R.color.bg, 0, 10, false, 0);
        setBackgroundDrawable(getContext(), b.bgGoCard, R.color.contrast, 0, 10, false, 0);

        setBackgroundDrawable(getContext(), b.bgAddPost, R.color.contrast, 0, 50, false, 0);
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
        adapterCharImage = new AdapterCharImage(getContext(), AdapterCharPick.NOT_ADDABLE, uuid, 0);
        b.recCharImage.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        b.recCharImage.setAdapter(adapterCharImage);
    }

    private void setRecyclerCharText() {
        adapterCharPick = new AdapterCharPick(getContext(), AdapterCharPick.NOT_ADDABLE, uuid, 0);
        b.recCharPick.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        b.recCharPick.setAdapter(adapterCharPick);
    }

}