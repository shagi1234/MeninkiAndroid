package tm.store.meninki.adapter;

import static tm.store.meninki.utils.Const.mainFragmentManager;
import static tm.store.meninki.utils.FragmentHelper.addFragment;
import static tm.store.meninki.utils.StaticMethods.dpToPx;
import static tm.store.meninki.utils.StaticMethods.setBackgroundDrawable;
import static tm.store.meninki.utils.StaticMethods.setMargins;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import tm.store.meninki.R;
import tm.store.meninki.data.StoreDTO;
import tm.store.meninki.databinding.ItemStoreBinding;
import tm.store.meninki.fragment.FragmentProduct;

import java.util.ArrayList;

public class AdapterStore extends RecyclerView.Adapter<AdapterStore.StoreHolder> {
    private Context context;
    private FragmentActivity activity;
    private ArrayList<StoreDTO> stories = new ArrayList<>();

    public AdapterStore(Context context, FragmentActivity activity) {
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public StoreHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ItemStoreBinding popularAudios = ItemStoreBinding.inflate(layoutInflater, parent, false);
        return new AdapterStore.StoreHolder(popularAudios);
    }

    @Override
    public void onBindViewHolder(@NonNull StoreHolder holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {
        if (stories == null) {
            return 5;
        }
        return stories.size();
    }

    public void setStories(ArrayList<StoreDTO> stories) {
        this.stories = stories;
        notifyDataSetChanged();
    }

    public class StoreHolder extends RecyclerView.ViewHolder {
        private final ItemStoreBinding b;

        public StoreHolder(@NonNull ItemStoreBinding itemView) {
            super(itemView.getRoot());
            this.b = itemView;
        }

        public void bind() {


            if (getAdapterPosition() == 0) {
                setMargins(b.getRoot(), dpToPx(10, context), dpToPx(20, context), dpToPx(2, context), 0);
            } else if (getAdapterPosition() == getItemCount() - 1) {
                setMargins(b.getRoot(), dpToPx(2, context), dpToPx(20, context), dpToPx(10, context), 0);
            } else {
                setMargins(b.getRoot(), dpToPx(2, context), dpToPx(20, context), dpToPx(2, context), 0);
            }

            setBackgroundDrawable(context, b.posterImage, R.color.holder, R.color.accent, 0, true, 2);

            b.click.setOnClickListener(v -> addFragment(mainFragmentManager, R.id.fragment_container_main, FragmentProduct.newInstance("", FragmentProduct.STORE)));

            if (stories == null) return;

            Glide.with(context)
                    .load(stories.get(getAdapterPosition()).getImagePath())
                    .into(b.image);

            Glide.with(context)
                    .load(stories.get(getAdapterPosition()).getPosterImagePath())
                    .into(b.posterImage);

            b.storeName.setText(stories.get(getAdapterPosition()).getStoreName());

        }
    }
}
