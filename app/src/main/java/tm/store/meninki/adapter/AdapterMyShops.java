package tm.store.meninki.adapter;

import static tm.store.meninki.utils.StaticMethods.setBackgroundDrawable;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import tm.store.meninki.R;
import tm.store.meninki.api.data.UserProfile;
import tm.store.meninki.databinding.ItemMyShopsBinding;
import tm.store.meninki.fragment.FragmentProfile;
import tm.store.meninki.utils.Const;
import tm.store.meninki.utils.FragmentHelper;

public class AdapterMyShops extends RecyclerView.Adapter<AdapterMyShops.StoreHolder> {
    private final Context context;
    private final FragmentActivity activity;
    private ArrayList<UserProfile> grids = new ArrayList<>();

    public AdapterMyShops(Context context, FragmentActivity activity) {
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public StoreHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ItemMyShopsBinding popularAudios = ItemMyShopsBinding.inflate(layoutInflater, parent, false);
        return new AdapterMyShops.StoreHolder(popularAudios);
    }

    @Override
    public void onBindViewHolder(@NonNull StoreHolder holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {
        if (grids == null) {
            return 5;
        }
        return grids.size();
    }

    public void setStories(ArrayList<UserProfile> stories) {
        this.grids = stories;
        notifyDataSetChanged();
    }

    public class StoreHolder extends RecyclerView.ViewHolder {
        private final ItemMyShopsBinding b;

        public StoreHolder(@NonNull ItemMyShopsBinding itemView) {
            super(itemView.getRoot());
            this.b = itemView;
        }

        public void bind() {

            setBackgroundDrawable(context, b.root, R.color.hover, 0, 10, false, 0);
            setBackgroundDrawable(context, b.layNewMessage, R.color.background, R.color.hover, 4, false, 2);
            setBackgroundDrawable(context, b.layWaitingOrder, R.color.background, R.color.hover, 4, false, 2);

            b.shopName.setOnClickListener(v -> {
                FragmentHelper.addFragment(Const.mainFragmentManager, R.id.fragment_container_main, FragmentProfile.newInstance(FragmentProfile.TYPE_SHOP, grids.get(getAdapterPosition()).getId()));
            });
            b.shopImage.setOnClickListener(v -> {
                FragmentHelper.addFragment(Const.mainFragmentManager, R.id.fragment_container_main, FragmentProfile.newInstance(FragmentProfile.TYPE_SHOP, grids.get(getAdapterPosition()).getId()));
            });

            if (grids == null) return;
            Glide.with(context)
                    .load(grids.get(getAdapterPosition()).getImgPath())
                    .into(b.shopImage);

            b.name.setText(grids.get(getAdapterPosition()).getName());

            b.desc.setText(grids.get(getAdapterPosition()).getDescription());

        }
    }
}
