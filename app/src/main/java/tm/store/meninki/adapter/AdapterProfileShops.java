package tm.store.meninki.adapter;

import static tm.store.meninki.api.Network.BASE_URL;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import tm.store.meninki.R;
import tm.store.meninki.api.data.UserProfile;
import tm.store.meninki.databinding.ItemProfileShopsBinding;
import tm.store.meninki.fragment.FragmentNewShop;
import tm.store.meninki.fragment.FragmentProfile;
import tm.store.meninki.utils.Const;
import tm.store.meninki.utils.FragmentHelper;

public class AdapterProfileShops extends RecyclerView.Adapter<AdapterProfileShops.ViewHolder> {
    private Context context;
    private ArrayList<UserProfile> shops = new ArrayList<>();

    public AdapterProfileShops(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemProfileShopsBinding item = ItemProfileShopsBinding.inflate(inflater, parent, false);
        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return shops.size() + 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemProfileShopsBinding b;

        public ViewHolder(ItemProfileShopsBinding b) {
            super(b.getRoot());
            this.b = b;
        }

        public void bind() {
            b.getRoot().setOnClickListener(v -> {
                if (getAdapterPosition() == getItemCount() - 1) {
                    FragmentHelper.addFragment(Const.mainFragmentManager, R.id.fragment_container_main, FragmentNewShop.newInstance(""));
                    return;
                }
                FragmentHelper.addFragment(Const.mainFragmentManager, R.id.fragment_container_main, FragmentProfile.newInstance(FragmentProfile.TYPE_SHOP, shops.get(getAdapterPosition()).getId()));
            });

            if (checkLast()) return;

            Glide.with(context)
                    .load(BASE_URL + "/" + shops.get(getAdapterPosition()).getImgPath())
                    .placeholder(R.color.low_contrast)
                    .error(R.color.low_contrast)
                    .into(b.shopImg);

            Log.e("TAG_shops", "bind: " + BASE_URL + "/" + shops.get(getAdapterPosition()).getImgPath());

            b.shopName.setText(shops.get(getAdapterPosition()).getName());
        }

        private boolean checkLast() {
            if (getAdapterPosition() == getItemCount() - 1) {
                b.shopsLay.setVisibility(View.GONE);
                b.addShopLay.setVisibility(View.VISIBLE);
            } else {
                b.shopsLay.setVisibility(View.VISIBLE);
                b.addShopLay.setVisibility(View.GONE);
            }

            return getAdapterPosition() == getItemCount() - 1;
        }
    }

    public void setShops(ArrayList<UserProfile> shops) {
        this.shops = shops;
        notifyDataSetChanged();
    }
}
