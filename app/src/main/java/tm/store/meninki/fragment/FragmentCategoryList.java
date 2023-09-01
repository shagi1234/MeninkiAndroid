package tm.store.meninki.fragment;

import static tm.store.meninki.utils.Const.mainFragmentManager;
import static tm.store.meninki.utils.StaticMethods.logWrite;
import static tm.store.meninki.utils.StaticMethods.navigationBarHeight;
import static tm.store.meninki.utils.StaticMethods.setPadding;
import static tm.store.meninki.utils.StaticMethods.statusBarHeight;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrInterface;
import com.r0adkll.slidr.model.SlidrPosition;

import java.lang.reflect.Type;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tm.store.meninki.R;
import tm.store.meninki.adapter.AdapterCategory;
import tm.store.meninki.api.data.ResponsePostGetAllItem;
import tm.store.meninki.api.data.UserProfile;
import tm.store.meninki.data.CategoryDto;
import tm.store.meninki.databinding.FragmentCategoryListBinding;
import tm.store.meninki.interfaces.OnBackPressedFragment;
import tm.store.meninki.interfaces.OnCategoryChecked;
import tm.store.meninki.shared.Account;
import tm.store.meninki.utils.StaticMethods;

public class FragmentCategoryList extends Fragment implements OnBackPressedFragment {
    private FragmentCategoryListBinding b;
    private AdapterCategory adapterText;
    public final static int TYPE_CATEGORY = 1;
    public final static int TYPE_SUBCATEGORY = 2;
    public final static int TYPE_SHOP = 3;
    private int type;
    private ArrayList<CategoryDto> subCategories;
    private SlidrInterface slidrInterface;
    private String titleCategory = "";
    private boolean isSaved;

    public static FragmentCategoryList newInstance(ArrayList<CategoryDto> sub_categories, int type, String titleCategory) {
        Bundle args = new Bundle();
        FragmentCategoryList fragment = new FragmentCategoryList();
        args.putInt("type", type);
        args.putString("title", titleCategory);
        args.putString("sub_category", new Gson().toJson(sub_categories));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            type = getArguments().getInt("type");
            titleCategory = getArguments().getString("title");
            Type type = new TypeToken<ArrayList<CategoryDto>>() {
            }.getType();
            subCategories = new Gson().fromJson(getArguments().getString("sub_category"), type);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!isCategory() && subCategories != null) return;

        StaticMethods.setPadding(b.main, 0, statusBarHeight, 0, navigationBarHeight);

        if ((isCategory() || isShop()) && subCategories == null) {

            if (slidrInterface == null && getView() != null)
                slidrInterface = Slidr.replace(getView().findViewById(R.id.main), new SlidrConfig.Builder().position(SlidrPosition.TOP).build());
        }

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentCategoryListBinding.inflate(inflater, container, false);
        setRecycler();
        initListeners();
        check();
        return b.getRoot();
    }

    private void check() {

        if (isShop()) {
            getShops();
            return;
        }

        if (subCategories == null && isCategory()) {
            getCategories();
            return;
        }

        hideProgress();
        adapterText.setCategories(subCategories);
    }

    private void hideProgress() {
        b.progressBar.setVisibility(View.GONE);
        b.recCategory.setVisibility(View.VISIBLE);
    }

    private void getShops() {
        Call<ArrayList<UserProfile>> call = StaticMethods.getApiHome().getMyShops(Account.newInstance(getContext()).getPrefUserUUID(), 0, 1, 10);
        call.enqueue(new Callback<ArrayList<UserProfile>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<UserProfile>> call, @NonNull Response<ArrayList<UserProfile>> response) {
                if (response.code() == 200 && response.body() != null) {
                    hideProgress();
                    adapterText.setShops(response.body());
                } else {
                    logWrite(response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<UserProfile>> call, @NonNull Throwable t) {
                logWrite(t.getMessage());
            }
        });

    }

    private void initListeners() {
        Log.e("TAG_Category", "initListeners: " + subCategories + "  " + isCategory());
        if (subCategories == null && isCategory()) {
            b.bgHeader.setVisibility(View.VISIBLE);
            b.tvHeader.setText("Выбор категории");
        } else if (subCategories != null && isCategory()) {
            b.bgHeader.setVisibility(View.VISIBLE);
            b.tvHeader.setText(titleCategory);
        } else {
            b.bgHeader.setVisibility(View.GONE);
        }

        b.clickBack.setOnClickListener(v -> {
            isSaved = false;
            getActivity().onBackPressed();
        });
        b.clickNext.setOnClickListener(view -> {
            isSaved = true;
            getActivity().onBackPressed();
        });
    }

    private void getCategories() {
        Call<ArrayList<CategoryDto>> call = StaticMethods.getApiCategory().getAllCategory(0);
        call.enqueue(new Callback<ArrayList<CategoryDto>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<CategoryDto>> call, @NonNull Response<ArrayList<CategoryDto>> response) {

                if (response.code() == 200 && response.body() != null) {
                    hideProgress();
                    adapterText.setCategories(response.body());
                } else {
                    logWrite(response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<CategoryDto>> call, @NonNull Throwable t) {
                logWrite(t.getMessage());

            }
        });
    }

    private void setRecycler() {
        adapterText = new AdapterCategory(getContext(), type, getActivity());
        b.recCategory.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        b.recCategory.setAdapter(adapterText);
    }

    private boolean isCategory() {
        return type == TYPE_CATEGORY;
    }

    private boolean isShop() {
        return type == TYPE_SHOP;
    }

    @Override
    public boolean onBackPressed() {
        if (!isSaved) {
            Fragment addProduct = mainFragmentManager.findFragmentByTag(FragmentAddProduct.class.getName());
            Fragment addShop = mainFragmentManager.findFragmentByTag(FragmentNewShop.class.getName());

            if (addProduct instanceof OnCategoryChecked) {
                ((OnCategoryChecked) addProduct).onChecked(false, null);
            }
            if (addShop instanceof OnCategoryChecked) {
                ((OnCategoryChecked) addShop).onChecked(false, null);
            }
        }
        return false;
    }
}