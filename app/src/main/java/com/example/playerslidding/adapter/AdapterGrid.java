package com.example.playerslidding.adapter;

import static com.example.playerslidding.utils.Const.mainFragmentManager;
import static com.example.playerslidding.utils.FragmentHelper.addFragment;
import static com.example.playerslidding.utils.StaticMethods.dpToPx;
import static com.example.playerslidding.utils.StaticMethods.setBackgroundDrawable;
import static com.example.playerslidding.utils.StaticMethods.setMargins;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.playerslidding.R;
import com.example.playerslidding.data.ProductDto;
import com.example.playerslidding.databinding.ItemBasketBinding;
import com.example.playerslidding.databinding.ItemStaggeredGridBinding;
import com.example.playerslidding.fragment.FragmentProduct;

import java.util.ArrayList;

public class AdapterGrid extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private FragmentActivity activity;
    private ArrayList<ProductDto> grids = new ArrayList<>();
    public final static int TYPE_GRID = 0;
    public final static int TYPE_HORIZONTAL_SMALL = 1;
    public final static int TYPE_HORIZONTAL = 2;
    public final static int TYPE_BASKET = 3;
    private int type;

    public AdapterGrid(Context context, FragmentActivity activity, int type) {
        this.context = context;
        this.type = type;
        this.activity = activity;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        if (type == TYPE_BASKET) {
            ItemBasketBinding popularAudios = ItemBasketBinding.inflate(layoutInflater, parent, false);
            return new AdapterGrid.BasketHolder(popularAudios);
        } else {
            ItemStaggeredGridBinding popularAudios = ItemStaggeredGridBinding.inflate(layoutInflater, parent, false);
            return new AdapterGrid.StoreHolder(popularAudios);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (type == TYPE_BASKET)
            ((BasketHolder) holder).bind();
        else
            ((StoreHolder) holder).bind();
    }

    @Override
    public int getItemCount() {
        if (grids == null) {
            return 4;
        }
        return grids.size();
    }

    public void setStories(ArrayList<ProductDto> stories) {
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

            switch (type) {
                case TYPE_GRID:
                    if (getAdapterPosition() % 2 == 0) {
                        b.layImage.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(240, context)));
                    } else {
                        b.layImage.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(110, context)));
                    }
                    break;
                case TYPE_HORIZONTAL_SMALL:
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
                    break;

                case TYPE_HORIZONTAL:
                    b.root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    b.layImage.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(240, context)));
                    b.root.setLayoutParams(new ViewGroup.LayoutParams(dpToPx(166, context), ViewGroup.LayoutParams.WRAP_CONTENT));

                    if (getAdapterPosition() == 0) {
                        setMargins(b.container, dpToPx(10, context), dpToPx(10, context), dpToPx(4, context), 0);
                    } else if (getAdapterPosition() == getItemCount() - 1) {
                        setMargins(b.container, dpToPx(4, context), dpToPx(10, context), dpToPx(10, context), 0);
                    } else {
                        setMargins(b.container, dpToPx(4, context), dpToPx(10, context), dpToPx(4, context), 0);
                    }
                    break;

            }

            setBackgroundDrawable(context, b.posterImage, R.color.holder, R.color.accent, 0, true, 2);

            b.click.setOnClickListener(v -> addFragment(mainFragmentManager, R.id.fragment_container_main, FragmentProduct.newInstance("", FragmentProduct.PRODUCT)));

            if (grids == null) return;

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

    public class BasketHolder extends RecyclerView.ViewHolder {
        private final ItemBasketBinding b;
        private AdapterImageHorizontal adapterImageHorizontal;

        public BasketHolder(ItemBasketBinding b) {
            super(b.getRoot());
            this.b = b;
        }

        public void bind() {
            setBackgroundDrawable(context, b.posterImage, R.color.holder, R.color.accent, 0, true, 2);
            setBackgroundDrawable(context, b.layPrice, R.color.hover, 0, 4, false, 0);

            setRecycler();

            if (grids == null) return;

            Glide.with(context)
                    .load(grids.get(getAdapterPosition()).getImagePath())
                    .into(b.posterImage);

            ArrayList<String> images = new ArrayList<>();
            images.add(grids.get(getAdapterPosition()).getImagePath());
            adapterImageHorizontal.setImageUrl(images);

            b.name.setText(grids.get(getAdapterPosition()).getTitle());

            if (grids.get(getAdapterPosition()).getPrice() != null) {
                int price = Integer.parseInt(grids.get(getAdapterPosition()).getPrice()) * grids.get(getAdapterPosition()).getCount();
                b.price.setText(price + " TMT");
            }

            b.count.setText(String.valueOf(grids.get(getAdapterPosition()).getCount()));

            b.btnAdd.setOnClickListener(v -> {
                ProductDto data = grids.get(getAdapterPosition());
                data.setCount(data.getCount() + 1);
                notifyItemChanged(getAdapterPosition());
            });

            b.btnSubs.setOnClickListener(v -> {
                ProductDto data = grids.get(getAdapterPosition());
                data.setCount(data.getCount() - 1);
                notifyItemChanged(getAdapterPosition());
            });


        }

        private void setRecycler() {
            adapterImageHorizontal = new AdapterImageHorizontal(context);
            b.recImages.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            b.recImages.setAdapter(adapterImageHorizontal);

            adapterImageHorizontal.setImageUrl(null);

        }
    }
}
