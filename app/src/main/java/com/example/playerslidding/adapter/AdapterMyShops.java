package com.example.playerslidding.adapter;

import static com.example.playerslidding.utils.StaticMethods.setBackgroundDrawable;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.playerslidding.R;
import com.example.playerslidding.data.ShopDTO;
import com.example.playerslidding.databinding.ItemMyShopsBinding;

import java.util.ArrayList;

public class AdapterMyShops extends RecyclerView.Adapter<AdapterMyShops.StoreHolder> {
    private final Context context;
    private final FragmentActivity activity;
    private ArrayList<ShopDTO> grids = new ArrayList<>();

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

    public void setStories(ArrayList<ShopDTO> stories) {
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
            setBackgroundDrawable(context, b.layWaitingOrder, R.color.background,  R.color.hover, 4, false, 2);

            if (grids == null) return;
            Glide.with(context)
                    .load(grids.get(getAdapterPosition()).getImage())
                    .into(b.shopImage);

            b.name.setText(grids.get(getAdapterPosition()).getName());

            b.desc.setText(grids.get(getAdapterPosition()).getDesc());

        }
    }
}
