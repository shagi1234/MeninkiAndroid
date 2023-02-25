package tm.store.meninki.adapter;

import static tm.store.meninki.api.Network.BASE_URL;
import static tm.store.meninki.utils.StaticMethods.setBackgroundDrawable;
import static tm.store.meninki.utils.StaticMethods.setMargins;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import tm.store.meninki.R;
import tm.store.meninki.api.data.ProductDto;
import tm.store.meninki.databinding.ItemBasketBinding;
import tm.store.meninki.databinding.ItemStaggeredGridBinding;
import tm.store.meninki.fragment.FragmentProduct;
import tm.store.meninki.utils.Const;
import tm.store.meninki.utils.FragmentHelper;
import tm.store.meninki.utils.StaticMethods;

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
                        b.layImage.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, StaticMethods.dpToPx(240, context)));
                    } else {
                        b.layImage.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, StaticMethods.dpToPx(110, context)));
                    }
                    break;
                case TYPE_HORIZONTAL_SMALL:
                    b.root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    b.layImage.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, StaticMethods.dpToPx(110, context)));
                    b.root.setLayoutParams(new ViewGroup.LayoutParams(StaticMethods.dpToPx(166, context), ViewGroup.LayoutParams.WRAP_CONTENT));

                    if (getAdapterPosition() == 0) {
                        setMargins(b.container, StaticMethods.dpToPx(10, context), StaticMethods.dpToPx(10, context), StaticMethods.dpToPx(4, context), 0);
                    } else if (getAdapterPosition() == getItemCount() - 1) {
                        setMargins(b.container, StaticMethods.dpToPx(4, context), StaticMethods.dpToPx(10, context), StaticMethods.dpToPx(10, context), 0);
                    } else {
                        setMargins(b.container, StaticMethods.dpToPx(4, context), StaticMethods.dpToPx(10, context), StaticMethods.dpToPx(4, context), 0);
                    }
                    break;

                case TYPE_HORIZONTAL:
                    b.root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    b.layImage.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, StaticMethods.dpToPx(240, context)));
                    b.root.setLayoutParams(new ViewGroup.LayoutParams(StaticMethods.dpToPx(166, context), ViewGroup.LayoutParams.WRAP_CONTENT));

                    if (getAdapterPosition() == 0) {
                        setMargins(b.container, StaticMethods.dpToPx(10, context), StaticMethods.dpToPx(10, context), StaticMethods.dpToPx(4, context), 0);
                    } else if (getAdapterPosition() == getItemCount() - 1) {
                        setMargins(b.container, StaticMethods.dpToPx(4, context), StaticMethods.dpToPx(10, context), StaticMethods.dpToPx(10, context), 0);
                    } else {
                        setMargins(b.container, StaticMethods.dpToPx(4, context), StaticMethods.dpToPx(10, context), StaticMethods.dpToPx(4, context), 0);
                    }
                    break;

            }

            setBackgroundDrawable(context, b.posterImage, R.color.holder, R.color.accent, 0, true, 2);

            b.click.setOnClickListener(v -> FragmentHelper.addFragment(Const.mainFragmentManager, R.id.fragment_container_main, FragmentProduct.newInstance("", FragmentProduct.PRODUCT)));

            if (grids == null) return;

            if (grids.get(getAdapterPosition()).getMedias().size() > 0)
                Glide.with(context)
                        .load(BASE_URL + "/" + grids.get(getAdapterPosition()).getMedias().get(0).getImage().getDirectoryThumbnails())
                        .centerCrop()
                        .into(b.image);

//            Glide.with(context)
//                    .load(grids.get(getAdapterPosition()).getImagePath())
//                    .into(b.posterImage);

            b.title.setText(grids.get(getAdapterPosition()).getName());
            b.price.setText(grids.get(getAdapterPosition()).getPrice() + " TMT");
            b.oldPrice.setText(grids.get(getAdapterPosition()).getDiscountPrice() + " TMT");
            b.count.setText(String.valueOf(grids.get(getAdapterPosition()).getCount()));
            b.sale.setText("- " + grids.get(getAdapterPosition()).getPriceBonus() + " %");

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

//            Glide.with(context)
//                    .load(grids.get(getAdapterPosition()).getImagePath())
//                    .into(b.posterImage);

            ArrayList<String> images = new ArrayList<>();
            images.add(BASE_URL + "/" + grids.get(getAdapterPosition()).getMedias().get(0).getImage().getDirectoryCompressed());
            adapterImageHorizontal.setImageUrl(images);

            b.name.setText(grids.get(getAdapterPosition()).getName());

            int price = grids.get(getAdapterPosition()).getPrice();
            b.price.setText(price + " TMT");

//            b.count.setText(String.valueOf(grids.get(getAdapterPosition()).get()));

            b.btnAdd.setOnClickListener(v -> {
                ProductDto data = grids.get(getAdapterPosition());
//                data.setCount(data.getCount() + 1);
                notifyItemChanged(getAdapterPosition());
            });

            b.btnSubs.setOnClickListener(v -> {
                ProductDto data = grids.get(getAdapterPosition());
//                data.setCount(data.getCount() - 1);
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
