package tm.store.meninki.adapter;

import static tm.store.meninki.utils.StaticMethods.dpToPx;
import static tm.store.meninki.utils.StaticMethods.setMargins;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import tm.store.meninki.data.StoreDTO;
import tm.store.meninki.databinding.ItemCircleProductBinding;

import java.util.ArrayList;

import tm.store.meninki.utils.StaticMethods;

public class AdapterCircle extends RecyclerView.Adapter<AdapterCircle.StoreHolder> {
    private Context context;
    private FragmentActivity activity;
    private ArrayList<StoreDTO> stories = new ArrayList<>();

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
            return 5;
        }
        return stories.size();
    }

    public void setStories(ArrayList<StoreDTO> stories) {
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
                setMargins(b.getRoot(), StaticMethods.dpToPx(10, context), StaticMethods.dpToPx(0, context), StaticMethods.dpToPx(4, context), 0);
            } else if (getAdapterPosition() == getItemCount() - 1) {
                setMargins(b.getRoot(), StaticMethods.dpToPx(4, context), StaticMethods.dpToPx(0, context), StaticMethods.dpToPx(10, context), 0);
            } else {
                setMargins(b.getRoot(), StaticMethods.dpToPx(4, context), StaticMethods.dpToPx(0, context), StaticMethods.dpToPx(4, context), 0);
            }

            if (stories == null) {
                return;
            }
            Glide.with(context)
                    .load(stories.get(getAdapterPosition()).getImagePath())
                    .into(b.image);

            b.storeName.setText(stories.get(getAdapterPosition()).getStoreName());

        }
    }
}
