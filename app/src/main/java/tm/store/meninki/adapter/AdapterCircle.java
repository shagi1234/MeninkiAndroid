package tm.store.meninki.adapter;

import static tm.store.meninki.utils.StaticMethods.setMargins;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import tm.store.meninki.R;
import tm.store.meninki.data.CategoryDto;
import tm.store.meninki.databinding.ItemCircleProductBinding;
import tm.store.meninki.utils.StaticMethods;

public class AdapterCircle extends RecyclerView.Adapter<AdapterCircle.StoreHolder> {
    private Context context;
    private FragmentActivity activity;
    private ArrayList<CategoryDto> stories = new ArrayList<>();

    public AdapterCircle(Context context, FragmentActivity activity) {
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public StoreHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ItemCircleProductBinding popularAudios = ItemCircleProductBinding.inflate(layoutInflater, parent, false);
        return new AdapterCircle.StoreHolder(popularAudios);
    }

    @Override
    public void onBindViewHolder(@NonNull StoreHolder holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {
        if (stories == null) {
            return 0;
        }
        return stories.size();
    }

    public void setStories(ArrayList<CategoryDto> stories) {
        this.stories = stories;
        notifyDataSetChanged();
    }

    public class StoreHolder extends RecyclerView.ViewHolder {
        private final ItemCircleProductBinding b;

        public StoreHolder(@NonNull ItemCircleProductBinding itemView) {
            super(itemView.getRoot());
            this.b = itemView;
        }

        public void bind() {

            if (getAdapterPosition() == 0) {
                setMargins(b.getRoot(), StaticMethods.dpToPx(10, context), StaticMethods.dpToPx(5, context), StaticMethods.dpToPx(4, context), StaticMethods.dpToPx(5, context));
            } else if (getAdapterPosition() == getItemCount() - 1) {
                setMargins(b.getRoot(), StaticMethods.dpToPx(4, context), StaticMethods.dpToPx(5, context), StaticMethods.dpToPx(10, context), StaticMethods.dpToPx(5, context));
            } else {
                setMargins(b.getRoot(), StaticMethods.dpToPx(4, context), StaticMethods.dpToPx(5, context), StaticMethods.dpToPx(4, context), StaticMethods.dpToPx(5, context));
            }

            Glide.with(context)
                    .load(stories.get(getAdapterPosition()).getCategoryImage())
                    .placeholder(R.color.low_contrast)
                    .error(R.color.neutral_dark)
                    .into(b.image);

            b.storeName.setText(stories.get(getAdapterPosition()).getName());
        }
    }
}
