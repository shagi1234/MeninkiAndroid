package tm.store.meninki.adapter;

import static tm.store.meninki.utils.StaticMethods.setBackgroundDrawable;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import tm.store.meninki.R;
import tm.store.meninki.api.data.response.ResponseHomeShops;
import tm.store.meninki.databinding.ItemShopBinding;
import tm.store.meninki.fragment.FragmentProfile;
import tm.store.meninki.utils.Const;
import tm.store.meninki.utils.FragmentHelper;

public class AdapterShops extends RecyclerView.Adapter<AdapterShops.StoreHolder> {
    private final Context context;
    private final FragmentActivity activity;
    private ArrayList<ResponseHomeShops> grids = new ArrayList<>();
    private AdapterGrid adapterGrid;

    public AdapterShops(Context context, FragmentActivity activity) {
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public StoreHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ItemShopBinding popularAudios = ItemShopBinding.inflate(layoutInflater, parent, false);
        return new AdapterShops.StoreHolder(popularAudios);
    }

    @Override
    public void onBindViewHolder(@NonNull StoreHolder holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {
        if (grids == null) {
            return 0;
        }
        return grids.size();
    }

    public void setStories(ArrayList<ResponseHomeShops> stories) {
        this.grids = stories;
        notifyDataSetChanged();
    }

    public class StoreHolder extends RecyclerView.ViewHolder {
        private final ItemShopBinding b;

        public StoreHolder(@NonNull ItemShopBinding itemView) {
            super(itemView.getRoot());
            this.b = itemView;
        }

        public void bind() {

            setBackgroundDrawable(context, b.shopImage, R.color.neutral_dark, R.color.accent, 0, true, 2);
            setBackgroundDrawable(context, b.root, R.color.bg, 0, 10, false, 0);

            b.shopName.setOnClickListener(v -> FragmentHelper.addFragment(Const.mainFragmentManager, R.id.fragment_container_main, FragmentProfile.newInstance(FragmentProfile.TYPE_SHOP, grids.get(getAdapterPosition()).getShop().getId())));
            b.shopImage.setOnClickListener(v -> FragmentHelper.addFragment(Const.mainFragmentManager, R.id.fragment_container_main, FragmentProfile.newInstance(FragmentProfile.TYPE_SHOP, grids.get(getAdapterPosition()).getShop().getId())));

            setRecycler();

            Glide.with(context)
                    .load(grids.get(getAdapterPosition()).getShop().getImgPath())
                    .into(b.shopImage);

            b.name.setText(grids.get(getAdapterPosition()).getShop().getName());

            b.desc.setText(grids.get(getAdapterPosition()).getShop().getDescription());

        }

        private void setRecycler() {
            adapterGrid = new AdapterGrid(context, activity, AdapterGrid.TYPE_HORIZONTAL_SMALL, -1, false);
            b.recProducts.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            b.recProducts.setAdapter(adapterGrid);

            adapterGrid.setStories(grids.get(getAdapterPosition()).getProducts());
        }
    }
}
