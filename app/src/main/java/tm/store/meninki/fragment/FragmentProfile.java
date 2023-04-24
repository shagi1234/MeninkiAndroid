package tm.store.meninki.fragment;

import static tm.store.meninki.api.Network.BASE_URL;
import static tm.store.meninki.utils.Const.mainFragmentManager;
import static tm.store.meninki.utils.FragmentHelper.addFragment;
import static tm.store.meninki.utils.StaticMethods.navigationBarHeight;
import static tm.store.meninki.utils.StaticMethods.setBackgroundDrawable;
import static tm.store.meninki.utils.StaticMethods.setMargins;
import static tm.store.meninki.utils.StaticMethods.statusBarHeight;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import tm.store.meninki.R;
import tm.store.meninki.adapter.AdapterGrid;
import tm.store.meninki.adapter.AdapterProfileShops;
import tm.store.meninki.api.RetrofitCallback;
import tm.store.meninki.api.data.UserProfile;
import tm.store.meninki.api.request.RequestCard;
import tm.store.meninki.api.response.ResponseCard;
import tm.store.meninki.databinding.FragmentProfileBinding;
import tm.store.meninki.shared.Account;
import tm.store.meninki.utils.StaticMethods;

public class FragmentProfile extends Fragment {
    private FragmentProfileBinding b;
    private AdapterGrid adapterGrid;

    AdapterProfileShops adapter;
    private String type;
    public final static String TYPE_USER = "user";
    public final static String TYPE_SHOP = "shop";
    private boolean isMyShop;
    private Account account;
    private UserProfile user;
    private String id;

    public static FragmentProfile newInstance(String type, String id) {
        FragmentProfile fragment = new FragmentProfile();
        Bundle args = new Bundle();
        args.putString("type", type);
        args.putString("id", id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        setMargins(b.layHeader, 0, statusBarHeight, 0, 0);
        setMargins(b.coordinator, 0, 0, 0, navigationBarHeight);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        account = Account.newInstance(getContext());
        if (getActivity() != null) {
            type = getArguments().getString("type");
            id = getArguments().getString("id");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentProfileBinding.inflate(inflater, container, false);
        setRecyclerProducts();
        check();
        initListeners();
        return b.getRoot();
    }

    private void check() {
        setBackgrounds();

        switch (type) {
            case TYPE_USER:

                if (isMe()) {


                    setRecyclerShops();

                    b.editUser.setVisibility(View.VISIBLE);
                    b.contactsLay.setVisibility(View.GONE);

                    b.myShops.setText("Мои магазины");
                    b.countShops.setVisibility(View.VISIBLE);
                    b.myBookmarks.setText("Избранное");

                    b.addSms.setVisibility(View.GONE);
                    b.countBookmark.setVisibility(View.VISIBLE);

                } else {
                    b.editUser.setVisibility(View.GONE);

                    b.myBookmarks.setText("Написать сообщение");
                    b.countBookmark.setVisibility(View.GONE);
                    b.addSms.setVisibility(View.VISIBLE);

                    b.myShops.setText("Подписаться");
                    b.countShops.setVisibility(View.GONE);
                    b.followIc.setVisibility(View.VISIBLE);
                }

                getUserById();

                break;
            case TYPE_SHOP:
                b.countBookmark.setVisibility(View.GONE);
                b.editUser.setVisibility(View.GONE);

                try {
                    JSONArray shop = new JSONArray(Account.newInstance(getContext()).getMyShop());
                    int j = 0;
                    while (!isMyShop && j < shop.length()) {
                        JSONObject jsonObject = shop.getJSONObject(j);
                        isMyShop = jsonObject.getString("id").equals(id);
                        j++;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (isMyShop) {
                    b.editUser.setVisibility(View.VISIBLE);

                    b.myShops.setText("Мои магазины");
                    b.countShops.setVisibility(View.VISIBLE);
                    b.myBookmarks.setText("Избранное");

                    b.addSms.setVisibility(View.GONE);
                    b.countBookmark.setVisibility(View.VISIBLE);
                    b.contactsLay.setVisibility(View.VISIBLE);

                } else {
                    b.editUser.setVisibility(View.GONE);

                    b.myBookmarks.setText("Написать сообщение");
                    b.countBookmark.setVisibility(View.GONE);
                    b.addSms.setVisibility(View.VISIBLE);

                    b.myShops.setText("Подписаться");
                    b.countShops.setVisibility(View.GONE);
                    b.followIc.setVisibility(View.VISIBLE);
                    b.contactsLay.setVisibility(View.VISIBLE);
                    b.desc.setVisibility(View.VISIBLE);

                }


                getShopById();

                break;
        }
    }

    private void setRecyclerShops() {
        adapter = new AdapterProfileShops(getContext());
        b.rvShops.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        b.rvShops.setAdapter(adapter);
    }

    private void getShopById() {

        Call<UserProfile> call = StaticMethods.getApiHome().getShopById(id);
        call.enqueue(new RetrofitCallback<UserProfile>() {
            @Override
            public void onResponse(UserProfile response) {
                setResources(response);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void getUserById() {
        Call<UserProfile> call = StaticMethods.getApiHome().getUserById(id);
        call.enqueue(new RetrofitCallback<UserProfile>() {
            @Override
            public void onResponse(UserProfile response) {
                setResources(response);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void setResources(UserProfile response) {
        user = response;
        if (getContext() == null) return;
        b.nameUser.setText(response.getName());
        b.desc.setText(response.getDescription());
        b.phoneNum.setText(response.getPhoneNumber());
        b.countSubscribers.setText(String.valueOf(response.getSubscriberCount()));

        Glide.with(getContext())
                .load(BASE_URL + response.getImgPath())
                .into(b.bigImage);

        Glide.with(getContext())
                .load(BASE_URL + response.getImgPath())
                .into(b.imageUser);

        if (Objects.equals(type, TYPE_USER)) {
            b.countProducts.setText(String.valueOf(response.getSubscriptionCount()));

            if (isMe()) {
                b.countShops.setText(String.valueOf(response.getShopCount()));
                b.countBookmark.setText(String.valueOf(response.getFavoriteCount()));

            } else {
                checkSubscribe(response.isSubscribed());

            }

        } else {
            b.countProducts.setText(String.valueOf(response.getProductCount()));

            if (isMyShop) {
                b.countSubscribers.setText(String.valueOf(response.getVisiterCount()));
                b.countProducts.setText(String.valueOf(response.getOrderCount()));
            } else {
                checkSubscribe(response.isSubscribed());
            }
        }


        getProducts();
    }

    private void checkSubscribe(boolean subscribed) {
        if (subscribed) {
            b.myBookmarks.setText("Unsubscribe");
            b.followIc.setImageResource(R.drawable.ic_subtract_circle__2_);
        } else {
            b.myBookmarks.setText("Subscribe");
            b.followIc.setImageResource(R.drawable.ic_add);
        }
    }

    private void getProducts() {
        RequestCard requestCard = new RequestCard();
        requestCard.setSortType(0);
        requestCard.setUserId(id);
        requestCard.setTake(10);
        requestCard.setPageNumber(1);
        requestCard.setDescending(true);
        requestCard.setCategoryIds(null);
        Call<ArrayList<ResponseCard>> call = StaticMethods.getApiHome().getCard(requestCard);
        call.enqueue(new RetrofitCallback<ArrayList<ResponseCard>>() {
            @Override
            public void onResponse(ArrayList<ResponseCard> response) {
                if (response != null) {
                    adapterGrid.setStories(response);
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void setRecyclerProducts() {
        adapterGrid = new AdapterGrid(getContext(), getActivity(), AdapterGrid.TYPE_GRID, -1);
        b.recProducts.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        b.recProducts.setAdapter(adapterGrid);
    }

    private void initListeners() {
        b.layShops.setOnClickListener(v -> {
            b.layShops.setEnabled(false);
            addFragment(mainFragmentManager, R.id.fragment_container_main, FragmentMyShops.newInstance());
            new Handler().postDelayed(() -> b.layShops.setEnabled(true), 200);
        });

        b.laySubscribers.setOnClickListener(v -> {
            addFragment(mainFragmentManager, R.id.fragment_container_main, FragmentUserSubscribers.newInstance(type, id, true));
        });

        b.layFollows.setOnClickListener(v -> {
            addFragment(mainFragmentManager, R.id.fragment_container_main, FragmentUserSubscribers.newInstance(type, id, false));
        });

        b.layBack.setOnClickListener(v -> getActivity().onBackPressed());

        b.layBookmark.setOnClickListener(v -> {
            if (type.equals(TYPE_SHOP)) {

                if (isMyShop) {

                } else {
                    userSubscribe(!user.isSubscribed(), true);
                    checkSubscribe(!user.isSubscribed());
                }

            } else {

                if (isMe()) {

                } else {
                    userSubscribe(!user.isSubscribed(), false);
                    checkSubscribe(!user.isSubscribed());
                }
            }
        });

    }

    private void userSubscribe(boolean isSubscribe, boolean isShop) {
        JsonObject j = new JsonObject();
        j.addProperty("id", id);
        j.addProperty("isSubscribe", isSubscribe);
        Call<Boolean> call = null;
        if (isShop) {
            call = StaticMethods.getApiHome().shopSubscribe(account.getAccessToken(), j);
        } else {
            call = StaticMethods.getApiHome().userSubscribe(account.getAccessToken(), j);
        }
        call.enqueue(new RetrofitCallback<Boolean>() {
            @Override
            public void onResponse(Boolean response) {
                if (response) {
                    user.setSubscribed(!user.isSubscribed());
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });

    }

    private boolean isMe() {
        return Objects.equals(id, Account.newInstance(getContext()).getPrefUserUUID());
    }

    private void setBackgrounds() {
        setBackgroundDrawable(getContext(), b.profileBox, R.color.white, 0, 10,  false, 10);
        setBackgroundDrawable(getContext(), b.backgroundSearch, R.color.low_contrast, 0, 10, false, 0);
        setBackgroundDrawable(getContext(), b.edtSearch, R.color.low_contrast, 0, 10, false, 0);
        setBackgroundDrawable(getContext(), b.layFollows, R.color.white, 0, 10, false, 0);
        setBackgroundDrawable(getContext(), b.laySubscribers, R.color.white, 0, 10, false, 0);
    }
}