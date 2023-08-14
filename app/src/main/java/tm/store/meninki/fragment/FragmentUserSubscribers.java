package tm.store.meninki.fragment;

import static tm.store.meninki.utils.StaticMethods.navigationBarHeight;
import static tm.store.meninki.utils.StaticMethods.setBackgroundDrawable;
import static tm.store.meninki.utils.StaticMethods.statusBarHeight;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import tm.store.meninki.R;
import tm.store.meninki.adapter.AdapterSubscriber;
import tm.store.meninki.api.RetrofitCallback;
import tm.store.meninki.api.data.UserProfile;
import tm.store.meninki.databinding.FragmentUserSubscribresBinding;
import tm.store.meninki.shared.Account;
import tm.store.meninki.utils.StaticMethods;

public class FragmentUserSubscribers extends Fragment {
    private FragmentUserSubscribresBinding b;
    private String type;
    private String id;
    private boolean subscribeType;
    private Account account;
    private AdapterSubscriber adapter;

    public static FragmentUserSubscribers newInstance(String type, String id, boolean subscribeType) {
        Bundle args = new Bundle();
        FragmentUserSubscribers fragment = new FragmentUserSubscribers();
        args.putString("type", type);
        args.putString("id", id);
        args.putBoolean("subscribeType", subscribeType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getString("type");
            id = getArguments().getString("id");
            subscribeType = getArguments().getBoolean("subscribeType");
        }
        account = Account.newInstance(getContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        StaticMethods.setPadding(b.getRoot(), 0, statusBarHeight, 0, navigationBarHeight);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentUserSubscribresBinding.inflate(inflater, container, false);
        setRecycler();

        check();

        initListeners();

        return b.getRoot();
    }

    private void check() {
        setTabShop(false);

        if (Objects.equals(type, FragmentProfile.TYPE_SHOP)) {
            b.tabUser.setVisibility(View.GONE);
            b.tabShop.setVisibility(View.GONE);
            b.header.setText(R.string.subscribers);
            getShopSubscribers();

        } else {
            if (!subscribeType) {
                b.tabUser.setVisibility(View.VISIBLE);
                b.header.setText(R.string.subscribes);
                b.tabShop.setVisibility(View.VISIBLE);
            } else {
                b.header.setText(R.string.subscribers);
                b.tabUser.setVisibility(View.GONE);
                b.tabShop.setVisibility(View.GONE);
            }

            getUserSubscribersUser();
        }
    }

    private void setTabShop(boolean t) {
        if (getContext() == null) return;

        if (t) {
            setBackgroundDrawable(getContext(), b.tabShop, R.color.accent, 0, 50, false, 0);
            setBackgroundDrawable(getContext(), b.tabUser, R.color.low_contrast, 0, 50, false, 0);
            b.tabUser.setTextColor(getContext().getResources().getColor(R.color.accent));
            b.tabShop.setTextColor(getContext().getResources().getColor(R.color.white));
            b.tabUser.setEnabled(true);
            b.tabShop.setEnabled(false);
            return;
        }

        setBackgroundDrawable(getContext(), b.tabShop, R.color.low_contrast, 0, 50, false, 0);
        setBackgroundDrawable(getContext(), b.tabUser, R.color.accent, 0, 50, false, 0);
        b.tabUser.setTextColor(getContext().getResources().getColor(R.color.white));
        b.tabShop.setTextColor(getContext().getResources().getColor(R.color.accent));
        b.tabShop.setEnabled(true);
        b.tabUser.setEnabled(false);
    }

    private void setRecycler() {
        adapter = new AdapterSubscriber(getContext());
        b.rec.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        b.rec.setAdapter(adapter);
    }

    private void initListeners() {
        b.tabUser.setOnClickListener(v -> {
            getUserSubscribersUser();
            setTabShop(false);
        });

        b.tabShop.setOnClickListener(v -> {
            getUsersShopSubscribes();
            setTabShop(true);
        });

        b.swipeLayout.setOnRefreshListener(() -> {
            check();
            b.swipeLayout.setRefreshing(false);
        });

        b.icBack.setOnClickListener(v -> getActivity().onBackPressed());
    }

    private void getUserSubscribersUser() {
        // only user
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("subscribers", subscribeType);
        jsonObject.addProperty("id", id);

        Call<ArrayList<UserProfile>> call = StaticMethods.getApiHome().getUserUserSubscribers(jsonObject);
        call.enqueue(new RetrofitCallback<ArrayList<UserProfile>>() {
            @Override
            public void onResponse(ArrayList<UserProfile> response) {
                Log.e("TAG_", "onResponse: " + new Gson().toJson(response));
                setUsers(response);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void showNoContent() {
        b.noContent.setVisibility(View.VISIBLE);
        b.rec.setVisibility(View.INVISIBLE);
    }

    private void getUsersShopSubscribes() {
        //user's shop follows

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", id);

        Call<ArrayList<UserProfile>> call = StaticMethods.getApiHome().getUserShopSubscribers(jsonObject);
        call.enqueue(new RetrofitCallback<ArrayList<UserProfile>>() {
            @Override
            public void onResponse(ArrayList<UserProfile> response) {
                Log.e("TAG_", "onResponse: " + new Gson().toJson(response));
                setUsers(response);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void setUsers(ArrayList<UserProfile> arrayList) {
        if (arrayList == null || arrayList.size() == 0) {
            showNoContent();
            return;
        }

        hideNoContent();
        adapter.setUsers(arrayList);
    }

    private void hideNoContent() {
        b.noContent.setVisibility(View.GONE);
        b.rec.setVisibility(View.VISIBLE);
    }

    private void getShopSubscribers() {
        //only users
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", id);

        Call<ArrayList<UserProfile>> call = StaticMethods.getApiHome().getShopSubscribers(jsonObject);
        call.enqueue(new RetrofitCallback<ArrayList<UserProfile>>() {
            @Override
            public void onResponse(ArrayList<UserProfile> response) {
                Log.e("TAG_", "onResponse: " + new Gson().toJson(response));
                setUsers(response);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }
}