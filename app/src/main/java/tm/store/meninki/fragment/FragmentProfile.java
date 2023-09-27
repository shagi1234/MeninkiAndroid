package tm.store.meninki.fragment;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static tm.store.meninki.api.Network.BASE_URL;
import static tm.store.meninki.utils.Const.mainFragmentManager;
import static tm.store.meninki.utils.FragmentHelper.addFragment;
import static tm.store.meninki.utils.StaticMethods.logWrite;
import static tm.store.meninki.utils.StaticMethods.navigationBarHeight;
import static tm.store.meninki.utils.StaticMethods.setBackgroundDrawable;
import static tm.store.meninki.utils.StaticMethods.setMargins;
import static tm.store.meninki.utils.StaticMethods.setPadding;
import static tm.store.meninki.utils.StaticMethods.statusBarHeight;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
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
import tm.store.meninki.activity.ActivitySettings;
import tm.store.meninki.adapter.AdapterGrid;
import tm.store.meninki.adapter.AdapterProfileShops;
import tm.store.meninki.api.RetrofitCallback;
import tm.store.meninki.api.data.ResponsePostGetAllItem;
import tm.store.meninki.api.data.UserProfile;
import tm.store.meninki.api.enums.Status;
import tm.store.meninki.api.request.RequestCard;
import tm.store.meninki.api.data.response.ResponseCard;
import tm.store.meninki.databinding.FragmentProfileBinding;
import tm.store.meninki.interfaces.OnProfileOpened;
import tm.store.meninki.interfaces.OnUserDataChanged;
import tm.store.meninki.shared.Account;
import tm.store.meninki.utils.FragmentHelper;
import tm.store.meninki.utils.StaticMethods;

public class FragmentProfile extends Fragment implements OnUserDataChanged, OnProfileOpened {
    private FragmentProfileBinding b;
    private AdapterGrid adapterGrid;
    AdapterProfileShops adapter;
    private String type;
    public final static String TYPE_USER = "user";
    public final static String TYPE_SHOP = "shop";
    public final static String MY_PROFILE_WITH_NAV = "my_profile_with_nav";
    private boolean isMyShop;
    private Account account;
    private UserProfile user;
    private String id;
    private ProgressDialog progressDialog;

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
        if (!Objects.equals(type, MY_PROFILE_WITH_NAV)) {
            setPadding(b.getRoot(), 0, statusBarHeight, 0, 0);
        }
        b.appBarLayout.addOnOffsetChangedListener(new AppBarLayout.BaseOnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                b.swipeLayout.setEnabled(verticalOffset == 0);
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        account = Account.newInstance(getContext());
        if (getArguments() != null) {
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
            case MY_PROFILE_WITH_NAV:
                setRecycler(AdapterGrid.TYPE_POST);

                b.allFollows.setText(R.string.subscribes);
                b.header.setText(b.nameUser.getText().toString());

                setRecyclerShops();

                getUserShops();

                if (isMe()) {

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

                    b.myBookmarks.setText(R.string.send_message);
                    b.countBookmark.setVisibility(View.GONE);
                    b.addSms.setVisibility(View.VISIBLE);

                    b.myShops.setText(R.string.subscribe);
                    b.countShops.setVisibility(View.GONE);
                    b.followIc.setVisibility(View.VISIBLE);

                }

                getUserById();

                break;
            case TYPE_SHOP:
                b.countBookmark.setVisibility(View.GONE);
                b.allFollows.setText(R.string.products);


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
                    b.settings.setImageResource(R.drawable.editvv);

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
        adapter = new AdapterProfileShops(getContext(), isMe());
        b.rvShops.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        b.rvShops.setAdapter(adapter);
    }

    private void getShopById() {
        Call<UserProfile> call = StaticMethods.getApiHome().getShopById(id);
        call.enqueue(new RetrofitCallback<UserProfile>() {
            @Override
            public void onResponse(UserProfile response) {
                setResources(response);
                b.mainLayout.setVisibility(View.VISIBLE);
                Log.e("TAG", "onResponse: " + true);
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
                b.mainLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Throwable t) {
                b.progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void setResources(UserProfile response) {
        user = response;
        if (getContext() == null) return;
        b.nameUser.setText(response.getName());
        b.header.setText(response.getName());
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
        r.setUserId(id);

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
        requestCard.setShopId(id);
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
        adapterGrid = new AdapterGrid(getContext(), getActivity(), type, -1, false);
        b.recProducts.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        b.recProducts.setAdapter(adapterGrid);
    }

    private void initListeners() {

        b.laySubscribers.setOnClickListener(v -> addFragment(mainFragmentManager, R.id.fragment_container_main, FragmentUserSubscribers.newInstance(type, id, true)));

        b.layFollows.setOnClickListener(v -> {
            if (!Objects.equals(type, TYPE_SHOP))
                addFragment(mainFragmentManager, R.id.fragment_container_main, FragmentUserSubscribers.newInstance(type, id, false));
            else b.appBarLayout.setExpanded(false, true);
        });

        b.layBack.setOnClickListener(v -> getActivity().onBackPressed());

        b.layShops.setOnClickListener(v -> {
            if (type.equals(TYPE_SHOP)) {

                if (isMyShop) {
                    addFragment(mainFragmentManager, R.id.fragment_container_main, FragmentOrders.newInstance());
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
            if (Objects.equals(type, TYPE_SHOP) && isMyShop) {
                addFragment(mainFragmentManager, R.id.fragment_container_main, FragmentNewShop.newInstance(id));
                return;
            }
            Intent intent = new Intent(getContext(), ActivitySettings.class);
            startActivity(intent);

        });

        b.icMore.setOnClickListener(view -> {
            if (Objects.equals(type, TYPE_SHOP) && isMyShop) {
                showBottomSheet();
                return;
            }
        });

        b.swipeLayout.setOnRefreshListener(() -> {
            check();
            b.swipeLayout.setRefreshing(false);
        });

        b.addProduct.setOnClickListener(v -> FragmentHelper.addFragment(mainFragmentManager, R.id.fragment_container_main, FragmentAddProduct.newInstance(id)));

    }

    private void showBottomSheet() {
        android.app.Dialog dialog1 = new android.app.Dialog(getContext());
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.bottomsheet_choose_language);

        dialog1.findViewById(R.id.lay_ru).setVisibility(View.GONE);
        dialog1.findViewById(R.id.header).setVisibility(View.GONE);
        TextView tv = dialog1.findViewById(R.id.lang_en);
        tv.setText(R.string.delete_shop);

        dialog1.findViewById(R.id.lay_en).setOnClickListener(v -> {
            new AlertDialog.Builder(getContext()).setTitle(getResources().getString(R.string.delete_shop)).setMessage(R.string.do_you_really_want_delete_shop).setPositiveButton(getResources().getString(R.string.continuee), (dialog, which) -> {
                //deleteShop
                showProgressDialog();
                deleteShop();

            }).setNegativeButton(getResources().getString(R.string.cancel), null).setIcon(R.drawable.ic_trash).show();
            dialog1.dismiss();
        });

        dialog1.getWindow().setLayout(MATCH_PARENT, WRAP_CONTENT);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog1.getWindow().getAttributes().windowAnimations = R.style.DialogAnim;
        dialog1.getWindow().setGravity(Gravity.BOTTOM);
        dialog1.show();
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void deleteShop() {
        Call<Object> call = StaticMethods.getApiHome().deleteShop(id);
        call.enqueue(new RetrofitCallback<Object>() {
            @Override
            public void onResponse(Object response) {
                dismissProgressDialog();
                Toast.makeText(getContext(), getActivity().getResources().getString(R.string.success), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Throwable t) {
                dismissProgressDialog();
                Toast.makeText(getContext(), getActivity().getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }
        });
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

    private void getUserShops() {
        Call<ArrayList<UserProfile>> call = StaticMethods.getApiHome().getMyShops(id, Status.in_review, 1, 10);
        call.enqueue(new Callback<ArrayList<UserProfile>>() {
            @Override
            public void onResponse(Call<ArrayList<UserProfile>> call, Response<ArrayList<UserProfile>> response) {
                if (response.code() == 200 && response.body() != null) {
                    adapter.setShops(response.body());
                    adapter.setMe(isMe());

                    if (isMe())
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

    @Override
    public void onOpen(String uuid) {
        b.mainLayout.setVisibility(View.GONE);
        b.progressBar.setVisibility(View.VISIBLE);
        id = uuid;
        check();

    }
}