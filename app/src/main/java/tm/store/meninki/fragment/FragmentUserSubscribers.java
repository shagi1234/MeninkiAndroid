package tm.store.meninki.fragment;

import static tm.store.meninki.utils.StaticMethods.navigationBarHeight;
import static tm.store.meninki.utils.StaticMethods.setPaddingWithHandler;
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
    public void onResume() {
        super.onResume();
        StaticMethods.setPaddingWithHandler(b.getRoot(), 0, statusBarHeight, 0, navigationBarHeight);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentUserSubscribresBinding.inflate(inflater, container, false);
        setRecycler();
        if (Objects.equals(type, FragmentProfile.TYPE_SHOP)) {
            b.tabUser.setVisibility(View.GONE);
            b.tabShop.setVisibility(View.GONE);
            getShopSubscribers();
        } else {

            b.tabUser.setBackgroundResource(R.color.accent);

            if (!subscribeType) {
                b.tabUser.setVisibility(View.VISIBLE);
                b.tabShop.setVisibility(View.VISIBLE);
            } else {
                b.tabUser.setVisibility(View.GONE);
                b.tabShop.setVisibility(View.GONE);
            }

            getUserSubscribersUser();
        }

        initListeners();

        return b.getRoot();
    }

    private void setRecycler() {
        adapter = new AdapterSubscriber(getContext());
        b.rec.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        b.rec.setAdapter(adapter);

    }

    private void initListeners() {
        b.tabUser.setOnClickListener(v -> {
            getUserSubscribersUser();
            b.tabUser.setBackgroundResource(R.color.accent);
            b.tabShop.setBackgroundResource(R.color.neutral_dark);
        });
        b.tabShop.setOnClickListener(v -> {
            getUserSubscribersShop();
            b.tabUser.setBackgroundResource(R.color.neutral_dark);
            b.tabShop.setBackgroundResource(R.color.accent);
        });
    }

    private void getUserSubscribersUser() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("subscribers", subscribeType);
        jsonObject.addProperty("id", id);

        Call<ArrayList<UserProfile>> call = StaticMethods.getApiHome().getUserUserSubscribers(account.getAccessToken(), jsonObject);
        call.enqueue(new RetrofitCallback<ArrayList<UserProfile>>() {
            @Override
            public void onResponse(ArrayList<UserProfile> response) {
                Log.e("TAG_", "onResponse: " + new Gson().toJson(response));

                adapter.setUsers(response);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void getUserSubscribersShop() {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", id);

        Call<ArrayList<UserProfile>> call = StaticMethods.getApiHome().getUserShopSubscribers(account.getAccessToken(), jsonObject);
        call.enqueue(new RetrofitCallback<ArrayList<UserProfile>>() {
            @Override
            public void onResponse(ArrayList<UserProfile> response) {
                Log.e("TAG_", "onResponse: " + new Gson().toJson(response));
                adapter.setUsers(response);

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void getShopSubscribers() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", id);

        Call<ArrayList<UserProfile>> call = StaticMethods.getApiHome().getShopSubscribers(account.getAccessToken(), jsonObject);
        call.enqueue(new RetrofitCallback<ArrayList<UserProfile>>() {
            @Override
            public void onResponse(ArrayList<UserProfile> response) {
                Log.e("TAG_", "onResponse: " + new Gson().toJson(response));
                adapter.setUsers(response);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }
}