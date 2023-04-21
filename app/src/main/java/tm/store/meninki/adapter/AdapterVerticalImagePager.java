package tm.store.meninki.adapter;

import static tm.store.meninki.api.Network.BASE_URL;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import tm.store.meninki.api.data.MediaDto;
import tm.store.meninki.databinding.ItemImageVerticalBinding;

public class AdapterVerticalImagePager extends RecyclerView.Adapter<AdapterVerticalImagePager.ImageHolder> {
    private ArrayList<MediaDto> imageList = new ArrayList<>();
    private Context context;

    public AdapterVerticalImagePager(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemImageVerticalBinding itemImageVerticalBinding = ItemImageVerticalBinding.inflate(layoutInflater, parent, false);
        return new ImageHolder(itemImageVerticalBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageHolder holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {
        if (imageList == null) {
            return 0;
        }
        return imageList.size();
    }

    public class ImageHolder extends RecyclerView.ViewHolder {
        private ItemImageVerticalBinding b;

        public ImageHolder(@NonNull ItemImageVerticalBinding itemView) {
            super(itemView.getRoot());
            this.b = itemView;
        }

        public void bind() {
            Glide.with(context)
                    .load(BASE_URL + imageList.get(getAdapterPosition()).getPath())
                    .into(b.image);

            Glide.with(context)
                    .load(BASE_URL + imageList.get(getAdapterPosition()).getPath())
                    .into(b.imageSmall);
        }
    }

    public void setImageList(ArrayList<MediaDto> imageList) {
        this.imageList = imageList;
        notifyDataSetChanged();
    }
}
