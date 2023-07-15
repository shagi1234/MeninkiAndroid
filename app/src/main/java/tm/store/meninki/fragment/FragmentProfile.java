package tm.store.meninki.fragment;

import static tm.store.meninki.api.Network.BASE_URL;
import static tm.store.meninki.utils.Const.mainFragmentManager;
import static tm.store.meninki.utils.FragmentHelper.addFragment;
import static tm.store.meninki.utils.StaticMethods.logWrite;
import static tm.store.meninki.utils.StaticMethods.navigationBarHeight;
import static tm.store.meninki.utils.StaticMethods.setBackgroundDrawable;
import static tm.store.meninki.utils.StaticMethods.setMargins;
import static tm.store.meninki.utils.StaticMethods.setPadding;
import static tm.store.meninki.utils.StaticMethods.statusBarHeight;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tm.store.meninki.R;
import tm.store.meninki.adapter.AdapterGrid;
import tm.store.meninki.adapter.AdapterProfileShops;
import tm.store.meninki.api.RetrofitCallback;
import tm.store.meninki.api.data.ResponsePostGetAllItem;
import tm.store.meninki.api.data.UserProfile;
import tm.store.meninki.api.enums.Status;
import tm.store.meninki.api.request.RequestCard;
import tm.store.meninki.api.data.response.ResponseCard;
import tm.store.meninki.databinding.FragmentProfileBinding;
import tm.store.meninki.interfaces.OnUserDataChanged;
import tm.store.meninki.shared.Account;
import tm.store.meninki.utils.FragmentHelper;
import tm.store.meninki.utils.StaticMethods;

public class FragmentProfile extends Fragment implements OnUserDataChanged {
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
        setMargins(b.coordinator, 0, 0, 0, navigationBarHeight);
        if (Objects.equals(type, TYPE_SHOP)) {
            setPadding(b.getRoot(), 0, statusBarHeight, 0, 0);
        }
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentProfileBinding.inflate(inflater, container, false);
        check();
        initListeners();
        return b.getRoot();
    }

    private void check() {
        setBackgrounds();

        switch (type) {
            case TYPE_USER:
                setRecycler(AdapterGrid.TYPE_POST);


                b.allFollows.setText(R.string.subscribes);
                b.header.setText(b.nameUser.getText().toString());

                if (isMe()) {

                    setRecyclerShops();

                    getMyShops();

                    b.settings.setVisibility(View.VISIBLE);
                    b.contactsLay.setVisibility(View.GONE);
                    //vagtlayynca
                    b.layShops.setVisibility(View.GONE);
                    b.layHeader.setVisibility(View.GONE);

                    b.myBookmarks.setText(R.string.bookmark);

                    b.addSms.setVisibility(View.GONE);
                    b.countBookmark.setVisibility(View.VISIBLE);

                } else {

                    b.settings.setVisibility(View.GONE);

                    b.layShops.setVisibility(View.VISIBLE);

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
                b.allFollows.setText(R.string.products);
                b.header.setText("Shops");

                b.settings.setVisibility(View.GONE);
                setRecycler(AdapterGrid.TYPE_GRID);

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
                    b.settings.setVisibility(View.VISIBLE);

                    b.myShops.setText(R.string.new_products);
                    b.countShops.setVisibility(View.VISIBLE);
                    b.myBookmarks.setText(getResources().getString(R.string.bookmark));

                    b.layVisitors.setVisibility(View.VISIBLE);
                    b.layPlaceRating.setVisibility(View.VISIBLE);

                    b.addSms.setVisibility(View.GONE);
                    b.countBookmark.setVisibility(View.VISIBLE);
                    b.contactsLay.setVisibility(View.VISIBLE);

                } else {
                    b.settings.setVisibility(View.GONE);

                    b.myBookmarks.setText(R.string.new_messages);
                    b.countBookmark.setVisibility(View.GONE);
                    b.addSms.setVisibility(View.VISIBLE);

                    b.myShops.setText(R.string.subscribe);
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
                Log.e("TAG", "onResponse: "+ true );
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
        b.phoneNum.setText(response.getPhone());
        b.countSubscribers.setText(String.valueOf(response.getSubscriberCount()));

        Glide.with(getContext()).load(BASE_URL + "/" + response.getImgPath()).placeholder(R.color.on_bg_ls).error(R.color.on_bg_ls).into(b.bigImage);

        Glide.with(getContext()).load(BASE_URL + "/" + response.getImgPath()).placeholder(R.color.on_bg_ls).error(R.color.on_bg_ls).into(b.imageUser);

        if (Objects.equals(type, TYPE_USER)) {
            b.countProducts.setText(String.valueOf(response.getSubscriptionCount()));

            if (isMe()) {
                b.countShops.setText(String.valueOf(response.getShopCount()));
                b.countBookmark.setText(String.valueOf(response.getFavoriteCount()));

            } else {
                checkSubscribe(response.isSubscribed());
            }
            getPostsUser();

        } else {
            b.countProducts.setText(String.valueOf(response.getProductCount()));

            if (isMyShop) {
                b.countVisitors.setText(String.valueOf(response.getVisiterCount()));
                b.countShops.setText(String.valueOf(response.getOrderCount()));

                b.addProduct.setVisibility(View.VISIBLE);
            } else {
                checkSubscribe(response.isSubscribed());
            }

            getProducts();
        }

        hideProgress();
    }

    private void hideProgress() {
        b.coordinator.setVisibility(View.VISIBLE);
        b.progressBar.setVisibility(View.GONE);
    }

    private void getPostsUser() {
        RequestCard r = new RequestCard();
        r.setDescending(true);
        r.setPageNumber(1);
        r.setTake(10);
//        r.setUserId(id);

        Call<ArrayList<ResponsePostGetAllItem>> call = StaticMethods.getApiHome().getAllPosts(r);
        call.enqueue(new Callback<ArrayList<ResponsePostGetAllItem>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<ResponsePostGetAllItem>> call, @NonNull Response<ArrayList<ResponsePostGetAllItem>> response) {
                if (response.code() == 200 && response.body() != null) {
                    adapterGrid.setPosts(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<ResponsePostGetAllItem>> call, @NonNull Throwable t) {

            }
        });

    }

    private void checkSubscribe(boolean subscribed) {
        if (subscribed) {
            b.myShops.setText(R.string.unsubscribe);
            b.followIc.setImageResource(R.drawable.dismiss_circle);
        } else {
            b.myShops.setText(getResources().getString(R.string.subscribe));
            b.followIc.setImageResource(R.drawable.ic_follow);
        }
    }

    private void getProducts() {
        RequestCard requestCard = new RequestCard();
        requestCard.setSortType(0);
//        requestCard.setShopId(id);
        requestCard.setTake(10);
        requestCard.setPageNumber(1);
        requestCard.setDescending(true);

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

    private void setRecycler(int type) {
        adapterGrid = new AdapterGrid(getContext(), getActivity(), type, -1);
        b.recProducts.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        b.recProducts.setAdapter(adapterGrid);
    }

    private void initListeners() {

        b.laySubscribers.setOnClickListener(v -> addFragment(mainFragmentManager, R.id.fragment_container_main, FragmentUserSubscribers.newInstance(type, id, true)));

        b.layFollows.setOnClickListener(v -> addFragment(mainFragmentManager, R.id.fragment_container_main, FragmentUserSubscribers.newInstance(type, id, false)));

        b.layBack.setOnClickListener(v -> getActivity().onBackPressed());

        b.layShops.setOnClickListener(v -> {
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

        b.settings.setOnClickListener(view -> {
            wait(view);

            addFragment(mainFragmentManager, R.id.fragment_container_main, FragmentSettings.newInstance());
        });

        b.addProduct.setOnClickListener(v -> FragmentHelper.addFragment(mainFragmentManager, R.id.fragment_container_main, FragmentAddProduct.newInstance()));

    }

    private void wait(View view) {
        view.setEnabled(false);
        new Handler().postDelayed(() -> view.setEnabled(true), 200);
    }

    private void userSubscribe(boolean isSubscribe, boolean isShop) {
        JsonObject j = new JsonObject();
        j.addProperty("id", id);
        j.addProperty("isSubscribe", isSubscribe);
        Call<Boolean> call = null;
        if (isShop) {
            call = StaticMethods.getApiHome().shopSubscribe(j);
        } else {
            call = StaticMethods.getApiHome().userSubscribe(j);
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

    private void getMyShops() {
        Call<ArrayList<UserProfile>> call = StaticMethods.getApiHome().getMyShops(Account.newInstance(getContext()).getPrefUserUUID(), Status.in_review, 1, 10);
        call.enqueue(new Callback<ArrayList<UserProfile>>() {
            @Override
            public void onResponse(Call<ArrayList<UserProfile>> call, Response<ArrayList<UserProfile>> response) {
                if (response.code() == 200 && response.body() != null) {
                    adapter.setShops(response.body());
                    account.setMyShops(new Gson().toJson(response.body()));

                } else {
                    logWrite(response.code());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<UserProfile>> call, Throwable t) {
                logWrite(t.getMessage());
            }
        });
    }

    private boolean isMe() {
        return Objects.equals(id, Account.newInstance(getContext()).getPrefUserUUID());
    }

    private void setBackgrounds() {
        setBackgroundDrawable(getContext(), b.profileBox, R.color.white, 0, 10, false, 0);
        setBackgroundDrawable(getContext(), b.backgroundSearch, R.color.low_contrast, 0, 10, false, 0);
        setBackgroundDrawable(getContext(), b.edtSearch, R.color.low_contrast, 0, 10, false, 0);
        setBackgroundDrawable(getContext(), b.layFollows, R.color.white, 0, 10, false, 0);
        setBackgroundDrawable(getContext(), b.laySubscribers, R.color.white, 0, 10, false, 0);
        setBackgroundDrawable(getContext(), b.contactsLay, R.color.bg, R.color.accent, 10, false, 2);
    }

    public UserProfile getUser() {
        return user;
    }

    @Override
    public void onChange() {
        getUserById();
    }
}