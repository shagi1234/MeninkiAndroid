package tm.store.meninki.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import tm.store.meninki.adapter.AdapterGrid;
import tm.store.meninki.api.RetrofitCallback;
import tm.store.meninki.api.data.ProductDto;
import tm.store.meninki.data.StoreDTO;
import tm.store.meninki.databinding.FragmentListGridBinding;
import tm.store.meninki.utils.StaticMethods;

public class FragmentListGrid extends Fragment {
    private FragmentListGridBinding b;
    private ArrayList<StoreDTO> stores = new ArrayList<>();
    private AdapterGrid adapterGrid;
    private int orientation;
    private int type;
    private String id;
    public final static int VERTICAL_GRID = 0;
    public final static int HORIZONTAL_LINEAR = 1;

    public final static int CATEGORY = 1;
    public final static int POPULAR = 2;
    public final static int NEW = 3;

    public static FragmentListGrid newInstance(int orientation, String id, int type) {
        FragmentListGrid fragment = new FragmentListGrid();
        Bundle args = new Bundle();
        args.putInt("orientation", orientation);
        args.putString("id", id);
        args.putInt("type", type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            orientation = getArguments().getInt("orientation");
            type = getArguments().getInt("type");
            id = getArguments().getString("id");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentListGridBinding.inflate(inflater, container, false);
        setRecycler();
        check();
        return b.getRoot();
    }

    private void check() {
        JsonObject j = new JsonObject();
        j.addProperty("sortType", 0);
        j.addProperty("descending", true);
        j.addProperty("categoryIds", "");
        j.addProperty("pageNumber", 1);
        j.addProperty("take", 10);

        Call<ArrayList<ProductDto>> call = StaticMethods.getApiHome().getProducts(j);
        call.enqueue(new RetrofitCallback<ArrayList<ProductDto>>() {
            @Override
            public void onResponse(ArrayList<ProductDto> response) {
                adapterGrid.setStories(response);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }


    private void setRecycler() {
        if (orientation == HORIZONTAL_LINEAR) {
            adapterGrid = new AdapterGrid(getContext(), getActivity(), AdapterGrid.TYPE_HORIZONTAL);
            b.recGrid.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        } else {
            adapterGrid = new AdapterGrid(getContext(), getActivity(), AdapterGrid.TYPE_GRID);
            b.recGrid.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        }
        b.recGrid.setAdapter(adapterGrid);
    }
}