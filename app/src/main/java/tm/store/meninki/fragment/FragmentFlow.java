package tm.store.meninki.fragment;

import static tm.store.meninki.utils.StaticMethods.logWrite;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tm.store.meninki.adapter.AdapterViewPager;
import tm.store.meninki.api.data.UserProfile;
import tm.store.meninki.api.enums.Status;
import tm.store.meninki.data.FragmentPager;
import tm.store.meninki.databinding.FragmentFlowBinding;
import tm.store.meninki.interfaces.ChangeFlowPage;
import tm.store.meninki.interfaces.OnBackPressedFragment;
import tm.store.meninki.shared.Account;
import tm.store.meninki.utils.StaticMethods;

public class FragmentFlow extends Fragment implements ChangeFlowPage, OnBackPressedFragment {
    private FragmentFlowBinding b;
    private AdapterViewPager adapterFeedPager;
    private Account account;

    public static FragmentFlow newInstance() {
        FragmentFlow fragment = new FragmentFlow();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        account = Account.newInstance(getContext());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentFlowBinding.inflate(inflater, container, false);
        getMyShops();
        setViewPager();
        b.viewPager.setCurrentItem(1);
        return b.getRoot();
    }

    private void getMyShops() {
        Call<ArrayList<UserProfile>> call = StaticMethods.getApiHome().getMyShops(Account.newInstance(getContext()).getPrefUserUUID(), Status.in_review, 1, 10);
        call.enqueue(new Callback<ArrayList<UserProfile>>() {
            @Override
            public void onResponse(Call<ArrayList<UserProfile>> call, Response<ArrayList<UserProfile>> response) {
                if (response.code() == 200 && response.body() != null) {
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

    private void setViewPager() {
//        b.viewPager.setOffscreenPageLimit(2);
        ArrayList<FragmentPager> mFragment = new ArrayList<>();
//        mFragment.add(new FragmentPager(FragmentCategory.newInstance(), ""));
        mFragment.add(new FragmentPager(FragmentMain.newInstance(), ""));
        adapterFeedPager = new AdapterViewPager(getChildFragmentManager(), mFragment);
        b.viewPager.setAdapter(adapterFeedPager);
    }

    @Override
    public void change() {
        b.viewPager.setCurrentItem(0);
    }

    @Override
    public boolean onBackPressed() {
        if (b.viewPager.getCurrentItem() == 0) {
            b.viewPager.setCurrentItem(1);
            return true;
        }
        return false;
    }
}