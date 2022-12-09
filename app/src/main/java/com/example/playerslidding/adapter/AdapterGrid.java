package com.example.playerslidding.adapter;

import static com.example.playerslidding.utils.StaticMethods.dpToPx;
import static com.example.playerslidding.utils.StaticMethods.setBackgroundDrawable;
import static com.example.playerslidding.utils.StaticMethods.setMargins;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.playerslidding.R;
import com.example.playerslidding.data.GridDto;
import com.example.playerslidding.databinding.ItemStaggeredGridBinding;

import java.util.ArrayList;

public class AdapterGrid extends RecyclerView.Adapter<AdapterGrid.StoreHolder> {
    private Context context;
    private FragmentActivity activity;
    private ArrayList<GridDto> grids = new ArrayList<>();
    public static int TYPE_GRID = 0;
    public static int TYPE_SHOP = 1;
    private int type;

    public AdapterGrid(Context context, FragmentActivity activity, int type) {
        this.context = context;
        this.type = type;
        this.activity = activity;
    }

    @NonNull
    @Override
    public StoreHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ItemStaggeredGridBinding popularAudios = ItemStaggeredGridBinding.inflate(layoutInflater, parent, false);
        return new AdapterGrid.StoreHolder(popularAudios);
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

    public void setStories(ArrayList<GridDto> stories) {
        this.grids = stories;
        notifyDataSetChanged();
    }

    public class StoreHolder extends RecyclerView.ViewHolder {
        private final ItemStaggeredGridBinding b;

        public StoreHolder(@NonNull ItemStaggeredGridBinding itemView) {
            super(itemView.getRoot());
            this.b = itemView;
        }

        public void bind() {
            if (type == TYPE_GRID) {
                if (grids.get(getAdapterPosition()).getOrientation().equals("0")) {
                    b.layImage.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(240, context)));
                } else {
                    b.layImage.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(110, context)));
                }

            } else {
                b.root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                b.layImage.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(110, context)));
                b.root.setLayoutParams(new ViewGroup.LayoutParams(dpToPx(166, context), ViewGroup.LayoutParams.WRAP_CONTENT));

                if (getAdapterPosition() == 0) {
                    setMargins(b.container, dpToPx(10, context), dpToPx(10, context), dpToPx(4, context), 0);
                } else if (getAdapterPosition() == getItemCount() - 1) {
                    setMargins(b.container, dpToPx(4, context), dpToPx(10, context), dpToPx(10, context), 0);
                } else {
                    setMargins(b.container, dpToPx(4, context), dpToPx(10, context), dpToPx(4, context), 0);
                }

            }

            setBackgroundDrawable(context, b.posterImage, R.color.holder, R.color.accent, 0, true, 2);

            Glide.with(context)
                    .load(grids.get(getAdapterPosition()).getImagePath())
                    .into(b.image);
            Glide.with(context)
                    .load(grids.get(getAdapterPosition()).getImagePath())
                    .into(b.posterImage);

            b.title.setText(grids.get(getAdapterPosition()).getTitle());
            b.price.setText(grids.get(getAdapterPosition()).getPrice() + " TMT");
            b.oldPrice.setText(grids.get(getAdapterPosition()).getOldPrice() + " TMT");
            b.count.setText(String.valueOf(grids.get(getAdapterPosition()).getCount()));
            b.sale.setText("- " + grids.get(getAdapterPosition()).getSale() + " %");

        }
    }
}
