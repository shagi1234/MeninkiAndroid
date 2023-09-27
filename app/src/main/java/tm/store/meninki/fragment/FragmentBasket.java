package tm.store.meninki.fragment;

import static tm.store.meninki.utils.StaticMethods.setBackgroundDrawable;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tm.store.meninki.R;
import tm.store.meninki.adapter.AdapterCardProducts;
import tm.store.meninki.api.data.ResponseOrderGetAll;
import tm.store.meninki.api.data.response.ResponseCard;
import tm.store.meninki.api.request.RequestGetAllOrder;
import tm.store.meninki.databinding.FragmentBasketBinding;
import tm.store.meninki.shared.Account;
import tm.store.meninki.utils.StaticMethods;

public class FragmentBasket extends Fragment {
    private FragmentBasketBinding b;
    private int totalProducts = 0;
    private int totalPrice = 0;
    private AdapterCardProducts adapterGrid;

    public static FragmentBasket newInstance() {
        FragmentBasket fragment = new FragmentBasket();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentBasketBinding.inflate(inflater, container, false);
        setBackgrounds();
        getAllOrder();
        initListeners();
        return b.getRoot();
    }

    private void getAllOrder() {
        b.progressBar.setVisibility(View.VISIBLE);
        RequestGetAllOrder requestGetAllOrder = new RequestGetAllOrder();
        requestGetAllOrder.setOrderStatus(0);
        Call<ArrayList<ResponseOrderGetAll>> call = StaticMethods.getApiHomeWithoutHeader().getAllOrder(Account.newInstance(getContext()).getAccessToken(), requestGetAllOrder);

        call.enqueue(new Callback<ArrayList<ResponseOrderGetAll>>() {
            @Override
            public void onResponse(Call<ArrayList<ResponseOrderGetAll>> call, Response<ArrayList<ResponseOrderGetAll>> response) {
                if (response.body() == null || response.body().size() == 0) {
                    b.noContent.setText(R.string.no_content);
                    b.noContent.setVisibility(View.VISIBLE);
                    b.progressBar.setVisibility(View.GONE);
                    return;
                }

                calculateTotalProducts(response.body());

                b.progressBar.setVisibility(View.GONE);
                b.noContent.setVisibility(View.GONE);

                setRecycler(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<ResponseOrderGetAll>> call, Throwable t) {
                b.progressBar.setVisibility(View.GONE);
                b.noContent.setVisibility(View.VISIBLE);
                b.noContent.setText(R.string.no_connection);
            }
        });
    }

    private void calculateTotalTotalPrice(ArrayList<ResponseCard> body) {
        for (int i = 0; i < body.size(); i++) {
            totalPrice += body.get(i).getPrice();
        }
    }

    private void calculateTotalProducts(ArrayList<ResponseOrderGetAll> body) {
        for (int i = 0; i < body.size(); i++) {
            totalProducts += body.get(i).getProducts().size();
            calculateTotalTotalPrice(body.get(i).getProducts());
        }
        b.totalCount.setText(totalProducts + " " + getActivity().getResources().getString(R.string.products));
        b.totalPrice.setText(totalPrice + " TMT");
    }

    private void setBackgrounds() {
        setBackgroundDrawable(getContext(), b.bgPayment, R.color.accent, 0, 10, false, 0);
    }

    private void initListeners() {

    }

    private void setRecycler(ArrayList<ResponseOrderGetAll> list) {
        b.recGrids.setVisibility(View.VISIBLE);
        adapterGrid = new AdapterCardProducts(getContext(), list);
        b.recGrids.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        b.recGrids.setAdapter(adapterGrid);
    }

}