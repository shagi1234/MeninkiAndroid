package tm.store.meninki.adapter;

import static com.google.common.net.HttpHeaders.USER_AGENT;
import static tm.store.meninki.api.Network.BASE_URL;
import static tm.store.meninki.utils.StaticMethods.dpToPx;
import static tm.store.meninki.utils.StaticMethods.getWindowWidth;
import static tm.store.meninki.utils.StaticMethods.setBackgroundDrawable;
import static tm.store.meninki.utils.StaticMethods.setMargins;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

import tm.store.meninki.R;
import tm.store.meninki.api.data.ResponsePostGetAllItem;
import tm.store.meninki.api.response.ResponseCard;
import tm.store.meninki.databinding.ItemAdvertisementBinding;
import tm.store.meninki.databinding.ItemBasketBinding;
import tm.store.meninki.databinding.ItemPostBinding;
import tm.store.meninki.databinding.ItemStaggeredGridBinding;
import tm.store.meninki.fragment.FragmentPost;
import tm.store.meninki.fragment.FragmentProduct;
import tm.store.meninki.utils.Const;
import tm.store.meninki.utils.FragmentHelper;
import tm.store.meninki.utils.StaticMethods;

public class AdapterGrid extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private FragmentActivity activity;
    private ArrayList<ResponseCard> grids = new ArrayList<>();
    private ArrayList<ResponsePostGetAllItem> posts = new ArrayList<>();
    public final static int TYPE_GRID = 0;
    public final static int TYPE_HORIZONTAL_SMALL = 1;
    public final static int TYPE_HORIZONTAL = 2;
    public final static int TYPE_POST = 4;
    public final static int TYPE_ADVERTISEMENT = 5;
    public final static int TYPE_BASKET = 3;
    private int maxSize;
    private int type;

    public AdapterGrid(Context context, FragmentActivity activity, int type, int maxSize) {
        this.context = context;
        this.type = type;
        this.activity = activity;
        this.maxSize = maxSize;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        switch (type) {
            case TYPE_BASKET:
                ItemBasketBinding b = ItemBasketBinding.inflate(layoutInflater, parent, false);
                return new AdapterGrid.BasketHolder(b);
            case TYPE_POST:
                ItemPostBinding postBinding = ItemPostBinding.inflate(layoutInflater, parent, false);
                return new AdapterGrid.PostHolder(postBinding);
            case TYPE_ADVERTISEMENT:
                ItemAdvertisementBinding ad = ItemAdvertisementBinding.inflate(layoutInflater, parent, false);
                return new AdapterGrid.AdvertisementHolder(ad);
            default:
                ItemStaggeredGridBinding popularAudios = ItemStaggeredGridBinding.inflate(layoutInflater, parent, false);
                return new AdapterGrid.StoreHolder(popularAudios);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (type) {
            case TYPE_BASKET:
                ((BasketHolder) holder).bind();
                break;
            case TYPE_POST:
                ((PostHolder) holder).bind();
                break;
            case TYPE_ADVERTISEMENT:
                ((AdvertisementHolder) holder).bind();
                break;
            default:
                ((StoreHolder) holder).bind();
                break;
        }
    }

    @Override
    public int getItemCount() {
        if (type == TYPE_POST && posts != null) {
            return posts.size();
        }
        if (grids == null) {
            return 0;
        }

        if (maxSize == -1) {
            return grids.size();
        }

        return Math.min(grids.size(), maxSize);

    }

    public void setStories(ArrayList<ResponseCard> stories) {
        this.grids = stories;
        notifyDataSetChanged();
    }

    public void setPosts(ArrayList<ResponsePostGetAllItem> posts) {
        this.posts = posts;
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
//                    if (getAdapterPosition() % 2 == 0) {
//                        b.layImage.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, StaticMethods.dpToPx(240, context)));
//                    } else {
//                        b.layImage.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, StaticMethods.dpToPx(110, context)));
//                    }
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

            setBackgroundDrawable(context, b.posterImage, R.color.neutral_dark, R.color.accent, 0, true, 2);

            b.click.setOnClickListener(v -> FragmentHelper.addFragment(Const.mainFragmentManager, R.id.fragment_container_main, FragmentProduct.newInstance(grids.get(getAdapterPosition()).getId())));

            if (grids == null) return;

            if (grids.get(getAdapterPosition()).getImages().length > 0)
                try {
                    String USER_AGENT = "Mozilla/5.0 (Linux; Android 11) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.181 Mobile Safari/537.36";

                    GlideUrl glideUrl = new GlideUrl(BASE_URL + "/" + grids.get(getAdapterPosition()).getImages()[0],
                            new LazyHeaders.Builder()
                                    .addHeader("User-Agent", USER_AGENT)
                                    .build());

                    RequestOptions requestOptions = new RequestOptions()
                            .placeholder(R.color.low_contrast)
                            .error(R.color.neutral_dark);

                    Glide.with(context)
                            .applyDefaultRequestOptions(requestOptions)
                            .load(glideUrl)
                            .timeout(60000)
                            .override(320, 480)
                            .into(b.image);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }


            Glide.with(context)
                    .load(BASE_URL + "/" + grids.get(getAdapterPosition()).getAvatar())
                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(b.posterImage);

            Double price = grids.get(getAdapterPosition()).getPrice();
            Double discountPrice = grids.get(getAdapterPosition()).getDiscountPrice();

            b.title.setText(grids.get(getAdapterPosition()).getName());
            b.price.setText(discountPrice + " TMT");
            b.oldPrice.setText(price + " TMT");
            b.count.setText(String.valueOf(grids.get(getAdapterPosition()).getCount()));

            if (price != null && discountPrice != null) {
                double percent = 100 - discountPrice * 100 / price;
                b.sale.setText("-" + Math.round(percent) + "%");
            }

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
            setBackgroundDrawable(context, b.posterImage, R.color.neutral_dark, R.color.accent, 0, true, 2);
            setBackgroundDrawable(context, b.layPrice, R.color.neutral_dark, 0, 4, false, 0);

            setRecycler();

            if (grids == null) return;

            Glide.with(context)
                    .load(grids.get(getAdapterPosition()).getAvatar())
                    .into(b.posterImage);


            if (grids.get(getAdapterPosition()).getImages().length > 0) {
                ArrayList<String> images = new ArrayList<>();

                images.add(BASE_URL + "/" + grids.get(getAdapterPosition()).getImages()[0]);
                adapterImageHorizontal.setImageUrl(images);
            }

            b.name.setText(grids.get(getAdapterPosition()).getName());

            double price = grids.get(getAdapterPosition()).getPrice();
            b.price.setText(price + " TMT");

            b.btnAdd.setOnClickListener(v -> {
                ResponseCard data = grids.get(getAdapterPosition());
//                data.setCount(data.getCount() + 1);
                notifyItemChanged(getAdapterPosition());
            });

            b.btnSubs.setOnClickListener(v -> {
                ResponseCard data = grids.get(getAdapterPosition());
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

    public class PostHolder extends RecyclerView.ViewHolder {
        private ItemPostBinding b;

        public PostHolder(ItemPostBinding b) {
            super(b.getRoot());
            this.b = b;
        }

        public void bind() {
            setIsRecyclable(false);

            setBackgroundDrawable(context, b.posterImage, R.color.neutral_dark, R.color.accent, 0, true, 2);

            b.layImage.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (getWindowWidth(activity) * 1.43 / 2)));
            ;

            if (getAdapterPosition() == getItemCount() - 1) {
                setMargins(b.getRoot(), 0, 0, 0, dpToPx(80, context));
            }

            b.click.setOnClickListener(v -> {
                int adapterPosition = getAdapterPosition();
                FragmentHelper.addFragment(Const.mainFragmentManager, R.id.fragment_container_main, FragmentPost.newInstance(posts, adapterPosition));
            });

            if (posts == null) return;

            if (posts.get(getAdapterPosition()).getMedias().size() > 0 && posts.get(getAdapterPosition()).getMedias().get(0).getMediaType() == 1) {
                b.layPlayedCount.setVisibility(View.VISIBLE);
            } else b.layPlayedCount.setVisibility(View.GONE);


            b.favCount.setText(posts.get(getAdapterPosition()).getRating().getTotal() + "");

            if (posts.get(getAdapterPosition()).getMedias().size() > 0)
                try {
                    GlideUrl glideUrl;

                    if (posts.get(getAdapterPosition()).getMedias().get(0).getMediaType() == 0) {
                        glideUrl = new GlideUrl(BASE_URL + "/" + posts.get(getAdapterPosition()).getMedias().get(0).getPath(),
                                new LazyHeaders.Builder().build());
                    } else
                        glideUrl = new GlideUrl(BASE_URL + "/" + posts.get(getAdapterPosition()).getMedias().get(0).getPreview(),
                                new LazyHeaders.Builder().build());


                    RequestOptions requestOptions = new RequestOptions()
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder);

                    Glide.with(context)
                            .applyDefaultRequestOptions(requestOptions)
                            .load(glideUrl)
                            .timeout(60000)
                            .override(320, 480)
                            .into(b.image);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }


            if (posts.get(getAdapterPosition()).getUser() != null)
                Glide.with(context)
                        .load(BASE_URL + "/" + posts.get(getAdapterPosition()).getUser().getImgPath())
                        .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .into(b.posterImage);

            b.title.setText(posts.get(getAdapterPosition()).getName());
            Log.e("TAG_title_post", "bind: " + posts.get(getAdapterPosition()).getName());
        }
    }

    public class AdvertisementHolder extends RecyclerView.ViewHolder {
        private ItemAdvertisementBinding b;

        public AdvertisementHolder(ItemAdvertisementBinding b) {
            super(b.getRoot());
            this.b = b;
        }

        public void bind() {

            if (getAdapterPosition() == getItemCount() - 1) {
                setMargins(b.getRoot(), 0, 0, 0, dpToPx(80, context));
            }

            b.click.setOnClickListener(v -> {
                int adapterPosition = getAdapterPosition();
                FragmentHelper.addFragment(Const.mainFragmentManager, R.id.fragment_container_main, FragmentPost.newInstance(posts, adapterPosition));
            });

            if (posts == null) return;

            if (posts.get(getAdapterPosition()).getMedias().size() > 0)
                try {
                    GlideUrl glideUrl;

                    if (posts.get(getAdapterPosition()).getMedias().get(0).getMediaType() == 0) {
                        glideUrl = new GlideUrl(BASE_URL + "/" + posts.get(getAdapterPosition()).getMedias().get(0).getPath(),
                                new LazyHeaders.Builder().build());
                    } else
                        glideUrl = new GlideUrl(BASE_URL + "/" + posts.get(getAdapterPosition()).getMedias().get(0).getPreview(),
                                new LazyHeaders.Builder().build());


                    RequestOptions requestOptions = new RequestOptions()
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder);

                    Glide.with(context)
                            .applyDefaultRequestOptions(requestOptions)
                            .load(glideUrl)
                            .timeout(60000)
                            .override(320, 480)
                            .into(b.image);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            b.title.setText(posts.get(getAdapterPosition()).getName());
        }
    }
}
