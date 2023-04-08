package tm.store.meninki.fragment;

import static tm.store.meninki.utils.Const.mainFragmentManager;
import static tm.store.meninki.utils.FragmentHelper.addFragment;
import static tm.store.meninki.utils.StaticMethods.logWrite;
import static tm.store.meninki.utils.StaticMethods.navigationBarHeight;
import static tm.store.meninki.utils.StaticMethods.setBackgroundDrawable;
import static tm.store.meninki.utils.StaticMethods.setMargins;
import static tm.store.meninki.utils.StaticMethods.statusBarHeight;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tm.store.meninki.R;
import tm.store.meninki.adapter.AdapterMyShops;
import tm.store.meninki.adapter.AdapterTabLayout;
import tm.store.meninki.api.data.UserProfile;
import tm.store.meninki.api.enums.Status;
import tm.store.meninki.data.TabItemCustom;
import tm.store.meninki.databinding.FragmentMyShopsBinding;
import tm.store.meninki.interfaces.OnTabClicked;
import tm.store.meninki.shared.Account;
import tm.store.meninki.utils.StaticMethods;

public class FragmentMyShops extends Fragment implements OnTabClicked {
    private FragmentMyShopsBinding b;
    private AdapterTabLayout adapterTabLayout;
    private ArrayList<TabItemCustom> tabs = new ArrayList<>();
    private AdapterMyShops adapterMyShops;
    private Account account;

    public static FragmentMyShops newInstance() {
        FragmentMyShops fragment = new FragmentMyShops();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tabs.add(new TabItemCustom("Работающие", true));
        tabs.add(new TabItemCustom("Ожидают подтверждения", false));
        tabs.add(new TabItemCustom("Скрытые", false));
        account = Account.newInstance(getContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        setMargins(b.layHeader, 0, statusBarHeight, 0, 0);
        setMargins(b.coordinator, 0, 0, 0, navigationBarHeight);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentMyShopsBinding.inflate(inflater, container, false);
        setBackgrounds();
        setRecyclerTab();
        setRecycler();
        getMyShops();
        initListeners();
        adapterTabLayout.setTabs(tabs);
        return b.getRoot();
    }

    private void getMyShops() {
        Call<ArrayList<UserProfile>> call = StaticMethods.getApiHome().getMyShops(Account.newInstance(getContext()).getPrefUserUUID(), Status.in_review, 1, 10);
        call.enqueue(new Callback<ArrayList<UserProfile>>() {
            @Override
            public void onResponse(Call<ArrayList<UserProfile>> call, Response<ArrayList<UserProfile>> response) {
                if (response.code() == 200 && response.body() != null) {
                    adapterMyShops.setStories(response.body());

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

    private void initListeners() {
        b.click.setOnClickListener(view -> addFragment(mainFragmentManager, R.id.fragment_container_main, FragmentNewShop.newInstance()));
        b.icBack.setOnClickListener(v -> getActivity().onBackPressed());
    }

    private void setRecycler() {
        adapterMyShops = new AdapterMyShops(getContext(), getActivity());
        b.recProducts.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        b.recProducts.setAdapter(adapterMyShops);
    }

    private void setRecyclerTab() {
        adapterTabLayout = new AdapterTabLayout(getContext(), null);
        b.recTab.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        b.recTab.setAdapter(adapterTabLayout);
    }

    private void setBackgrounds() {
        setBackgroundDrawable(getContext(), b.layBtn, R.color.accent, 0, 10, false, 0);
    }

    @Override
    public void onClick(int adapterPosition) {

    }
}