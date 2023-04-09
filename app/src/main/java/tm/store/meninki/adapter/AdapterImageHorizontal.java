package tm.store.meninki.adapter;

import static tm.store.meninki.utils.StaticMethods.dpToPx;
import static tm.store.meninki.utils.StaticMethods.setMargins;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import tm.store.meninki.R;
import tm.store.meninki.databinding.ItemImageBasketBinding;

import java.util.ArrayList;

import tm.store.meninki.utils.StaticMethods;

public class AdapterImageHorizontal extends RecyclerView.Adapter<AdapterImageHorizontal.CharImageHolder> {
    private ArrayList<String> imageUrl = new ArrayList<>();
    private Context context;

    public AdapterImageHorizontal(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public CharImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ItemImageBasketBinding popularAudios = ItemImageBasketBinding.inflate(layoutInflater, parent, false);
        return new AdapterImageHorizontal.CharImageHolder(popularAudios);
    }

    @Override
    public void onBindViewHolder(@NonNull CharImageHolder holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {
        if (imageUrl == null) {
            return 4;
        }
        return imageUrl.size();
    }

    public class CharImageHolder extends RecyclerView.ViewHolder {
        private final ItemImageBasketBinding b;

        public CharImageHolder(@NonNull ItemImageBasketBinding itemView) {
            super(itemView.getRoot());
            this.b = itemView;
        }

        public void bind() {

            if (getAdapterPosition() == 0) {
                setMargins(b.getRoot(), StaticMethods.dpToPx(20, context), StaticMethods.dpToPx(0, context), StaticMethods.dpToPx(4, context), 0);
            } else if (getAdapterPosition() == getItemCount() - 1) {
                setMargins(b.getRoot(), StaticMethods.dpToPx(4, context), StaticMethods.dpToPx(0, context), StaticMethods.dpToPx(20, context), 0);
            } else {
                setMargins(b.getRoot(), StaticMethods.dpToPx(4, context), StaticMethods.dpToPx(0, context), StaticMethods.dpToPx(4, context), 0);
            }
            if (imageUrl == null || imageUrl.get(getAdapterPosition()) == null) {
                return;
            }

            Glide.with(context).load(imageUrl.get(getAdapterPosition())).placeholder(R.color.neutral_dark).into(b.image);

//            setBackgroundDrawable(context, b.image, R.color.hover, 0, 4, false, 0);


        }
    }

    public void setImageUrl(ArrayList<String> imageUrl) {
        this.imageUrl = imageUrl;
        notifyDataSetChanged();
    }
}
