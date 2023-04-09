package tm.store.meninki.fragment;

import static tm.store.meninki.utils.Const.mainFragmentManager;
import static tm.store.meninki.utils.FragmentHelper.addFragmentWithAnim;
import static tm.store.meninki.utils.StaticMethods.navigationBarHeight;
import static tm.store.meninki.utils.StaticMethods.setBackgroundDrawable;
import static tm.store.meninki.utils.StaticMethods.setPaddingWithHandler;
import static tm.store.meninki.utils.StaticMethods.statusBarHeight;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tm.store.meninki.R;
import tm.store.meninki.api.data.UserProfile;
import tm.store.meninki.api.request.RequestCreateShop;
import tm.store.meninki.data.CategoryDto;
import tm.store.meninki.databinding.FragmentNewShopBinding;
import tm.store.meninki.interfaces.OnCategoryChecked;
import tm.store.meninki.shared.Account;
import tm.store.meninki.utils.StaticMethods;

public class FragmentNewShop extends Fragment implements OnCategoryChecked {
    private FragmentNewShopBinding b;
    private ArrayList<CategoryDto> categories = new ArrayList<>();

    public static FragmentNewShop newInstance() {
        FragmentNewShop fragment = new FragmentNewShop();
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
        StaticMethods.setPaddingWithHandler(b.getRoot(), 0, statusBarHeight, 0, navigationBarHeight);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentNewShopBinding.inflate(inflater, container, false);
        new Handler().postDelayed(this::setBackgrounds, 100);

        initListeners();
        return b.getRoot();
    }

    private void initListeners() {
        b.goBasket.setOnClickListener(v -> {
            b.goBasket.setEnabled(false);

            createShop();

            new Handler().postDelayed(() -> b.goBasket.setEnabled(true), 200);
        });
        b.chooseCategory.setOnClickListener(v -> {
            b.chooseCategory.setEnabled(false);

            categories.clear();
            addFragmentWithAnim(mainFragmentManager, R.id.fragment_container_main, FragmentCategoryList.newInstance(null, FragmentCategoryList.TYPE_CATEGORY));
            new Handler().postDelayed(() -> b.chooseCategory.setEnabled(true), 200);
        });
    }

    private void createShop() {
        RequestCreateShop requestCreateShop = new RequestCreateShop();
        String[] categoryIds = new String[categories.size()];
        for (int i = 0; i < categories.size(); i++) {
            categoryIds[i] = categories.get(i).getId();
        }
        requestCreateShop.setCategories(categoryIds);
        requestCreateShop.setUserId(Account.newInstance(getContext()).getPrefUserUUID());
        requestCreateShop.setDescriptionTm(b.about.getText().toString());
        requestCreateShop.setName(b.storeName.getText().toString());
        requestCreateShop.setPhoneNumber(b.phoneNumber.getText().toString());
        requestCreateShop.setEmail(b.extraContact.getText().toString());

        Call<UserProfile> call = StaticMethods.getApiHome().createShop(requestCreateShop);

        call.enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {

                if (response.code() == 200 && response.body() != null) {
                    // onResponse
                    Log.e("TAG", "initListeners: " + response.code());

                } else {
                    // onError
                }

            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                //onError
            }
        });
    }


    private void setBackgrounds() {
        setBackgroundDrawable(getContext(), b.storeName, R.color.neutral_dark, 0, 4, false, 0);
        setBackgroundDrawable(getContext(), b.storeNameTwo, R.color.neutral_dark, 0, 4, false, 0);
        setBackgroundDrawable(getContext(), b.phoneNumber, R.color.neutral_dark, 0, 4, false, 0);
        setBackgroundDrawable(getContext(), b.phoneNumberTwo, R.color.neutral_dark, 0, 4, false, 0);
        setBackgroundDrawable(getContext(), b.phoneNumberThree, R.color.neutral_dark, 0, 4, false, 0);
        setBackgroundDrawable(getContext(), b.about, R.color.neutral_dark, 0, 4, false, 0);
        setBackgroundDrawable(getContext(), b.extraContact, R.color.neutral_dark, 0, 4, false, 0);
        setBackgroundDrawable(getContext(), b.extraContactTwo, R.color.neutral_dark, 0, 4, false, 0);
        setBackgroundDrawable(getContext(), b.website, R.color.neutral_dark, 0, 4, false, 0);
        setBackgroundDrawable(getContext(), b.saveButton, R.color.accent, 0, 4, false, 0);

    }


    @Override
    public void onChecked(boolean isChecked, CategoryDto categoryDto) {
        if (isChecked) {
            categories.add(categoryDto);
        } else {
            categories.remove(categoryDto);
        }
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < categories.size(); i++) {
            s.append(categories.get(i).getName());
        }

        b.chooseCategory.setText(s.toString());
    }
}