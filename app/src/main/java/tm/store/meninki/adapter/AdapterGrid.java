package tm.store.meninki.adapter;

import static tm.store.meninki.api.Network.BASE_URL;
import static tm.store.meninki.utils.StaticMethods.dpToPx;
import static tm.store.meninki.utils.StaticMethods.getWindowWidth;
import static tm.store.meninki.utils.StaticMethods.navigationBarHeight;
import static tm.store.meninki.utils.StaticMethods.setBackgroundDrawable;
import static tm.store.meninki.utils.StaticMethods.setMargins;
import static tm.store.meninki.utils.StaticMethods.setPadding;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

import tm.store.meninki.R;
import tm.store.meninki.api.data.ResponseOrderGetAll;
import tm.store.meninki.api.data.ResponsePostGetAllItem;
import tm.store.meninki.api.data.response.ResponseCard;
import tm.store.meninki.data.AdvertisementDto;
import tm.store.meninki.databinding.ItemAdvertisementBinding;
import tm.store.meninki.databinding.ItemBasketBinding;
import tm.store.meninki.databinding.ItemPostBinding;
import tm.store.meninki.databinding.ItemStaggeredGridBinding;
import tm.store.meninki.fragment.FragmentAdvertisement;
import tm.store.meninki.fragment.FragmentProduct;
import tm.store.meninki.fragment.FragmentProfileViewPager;
import tm.store.meninki.utils.Const;
import tm.store.meninki.utils.FragmentHelper;
import tm.store.meninki.utils.StaticMethods;

public class AdapterGrid extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private FragmentActivity activity;
    private ArrayList<ResponseCard> grids = new ArrayList<>();
    private ArrayList<ResponsePostGetAllItem> posts = new ArrayList<>();

    private ArrayList<AdvertisementDto> advertisements = new ArrayList<>();
    public final static int TYPE_GRID = 0;
    public final static int TYPE_HORIZONTAL_SMALL = 1;
    public final static int TYPE_HORIZONTAL = 2;
    public final static int TYPE_POST = 4;
    public final static int TYPE_ADVERTISEMENT = 5;
    BottomSheetDialog dialog;
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

        if (type == TYPE_ADVERTISEMENT && advertisements != null) {
            return advertisements.size();
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

            if (grids.get(getAdapterPosition()).getImages().length > 0) try {
                String USER_AGENT = "Mozilla/5.0 (Linux; Android 11) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.181 Mobile Safari/537.36";

                GlideUrl glideUrl = new GlideUrl(BASE_URL + "/" + grids.get(getAdapterPosition()).getImages()[0], new LazyHeaders.Builder().addHeader("User-Agent", USER_AGENT).build());

                RequestOptions requestOptions = new RequestOptions().placeholder(R.color.low_contrast).error(R.color.neutral_dark);

                Glide.with(context).applyDefaultRequestOptions(requestOptions).load(glideUrl).timeout(60000).override(320, 480).into(b.image);
            } catch (Exception ex) {
                ex.printStackTrace();
            }


            Glide.with(context).load(BASE_URL + "/" + grids.get(getAdapterPosition()).getAvatar()).override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(b.posterImage);

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

            if (getAdapterPosition() == getItemCount() - 1) {
                setMargins(b.getRoot(), 0, 0, 0, dpToPx(80, context));
            }

            b.click.setOnClickListener(v -> {
                int adapterPosition = getAdapterPosition();
                FragmentHelper.addFragment(Const.mainFragmentManager, R.id.fragment_container_main, FragmentProfileViewPager.newInstance(posts, adapterPosition, posts.get(getAdapterPosition()).getUser().getId()));
            });

            if (posts == null) return;

            if (posts.get(getAdapterPosition()).getMedias().size() > 0 && posts.get(getAdapterPosition()).getMedias().get(0).getMediaType() == 1) {
                b.layPlayedCount.setVisibility(View.VISIBLE);
            } else b.layPlayedCount.setVisibility(View.GONE);


            b.favCount.setText(posts.get(getAdapterPosition()).getRating().getTotal() + "");

            if (posts.get(getAdapterPosition()).getMedias().size() > 0) try {
                GlideUrl glideUrl;

                if (posts.get(getAdapterPosition()).getMedias().get(0).getMediaType() == 0) {
                    glideUrl = new GlideUrl(BASE_URL + "/" + posts.get(getAdapterPosition()).getMedias().get(0).getPath(), new LazyHeaders.Builder().build());
                } else
                    glideUrl = new GlideUrl(BASE_URL + "/" + posts.get(getAdapterPosition()).getMedias().get(0).getPreview(), new LazyHeaders.Builder().build());


                RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.placeholder).error(R.drawable.placeholder);

                Glide.with(context).applyDefaultRequestOptions(requestOptions).load(glideUrl).timeout(60000).override(320, 480).into(b.image);
            } catch (Exception ex) {
                ex.printStackTrace();
            }


            if (posts.get(getAdapterPosition()).getUser() != null)
                Glide.with(context).load(BASE_URL + "/" + posts.get(getAdapterPosition()).getUser().getImgPath()).override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(b.posterImage);

            b.title.setText(posts.get(getAdapterPosition()).getName());
            Log.e("TAG_title_post", "bind: " + posts.get(getAdapterPosition()).getName());

            b.moreInfo.setOnClickListener(view -> showDialog());
        }


        private void showDialog() {
            dialog = new BottomSheetDialog(context, R.style.SheetDialog);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.bottom_sheet_story_info);
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnim;
            dialog.getWindow().setGravity(Gravity.BOTTOM);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            setDialogComponents();

            dialog.show();


        }

        private void setDialogComponents() {
            FrameLayout root = dialog.findViewById(R.id.bottom_root);
            TextView profileName = dialog.findViewById(R.id.profile_name);
            RoundedImageView roundedImageView = dialog.findViewById(R.id.profile_box);
            TextView title = dialog.findViewById(R.id.title);
            TextView description = dialog.findViewById(R.id.description);

            profileName.setText(posts.get(getAdapterPosition()).getUser().getName());
            description.setText(posts.get(getAdapterPosition()).getDescription());
            Glide.with(context).load(posts.get(getAdapterPosition()).getMedias().get(0)).into(roundedImageView);
            title.setText(posts.get(getAdapterPosition()).getName());


            setPadding(root, 0, 0, 0, navigationBarHeight);

        }
    }

    public class AdvertisementHolder extends RecyclerView.ViewHolder {
        private ItemAdvertisementBinding b;

        public AdvertisementHolder(ItemAdvertisementBinding b) {
            super(b.getRoot());
            this.b = b;
        }

        public void bind() {
            if (getAdapterPosition() == getItemCount() - 1 || getAdapterPosition() == getItemCount() - 2) {
                setMargins(b.getRoot(), 0, 0, 0, dpToPx(130, context));
            } else if (getAdapterPosition() == 0 || getAdapterPosition() == 1) {
                setMargins(b.getRoot(), 0, dpToPx(70, context), 0, 0);
            } else setMargins(b.getRoot(), 0, 0, 0, 0);


            setBackgroundDrawable(context, b.getRoot(), 0, 0, 8, false, 0);
            setComponents();

            b.click.setOnClickListener(v -> FragmentHelper.addFragment(Const.mainFragmentManager, R.id.fragment_container_main, FragmentAdvertisement.newInstance(advertisements.get(getAdapterPosition()).getId())));
        }

        private void setComponents() {
            b.title.setText(advertisements.get(getAbsoluteAdapterPosition()).getTitle());
            b.tvPrice.setText(String.valueOf(advertisements.get(getAdapterPosition()).getPrice()));

            if (advertisements.get(getAdapterPosition()).getImages() != null && advertisements.get(getAdapterPosition()).getImages().length > 0) {
                Glide.with(context).load(BASE_URL + "/" + advertisements.get(getAdapterPosition()).getImages()[0]).into(b.image);
            }

        }
    }

    public void setAdvertisements(ArrayList<AdvertisementDto> advertisements) {
        this.advertisements = advertisements;
        notifyDataSetChanged();
    }
}
