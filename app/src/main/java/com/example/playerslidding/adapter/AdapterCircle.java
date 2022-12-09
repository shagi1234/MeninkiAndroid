package com.example.playerslidding.adapter;

import static com.example.playerslidding.utils.StaticMethods.dpToPx;
import static com.example.playerslidding.utils.StaticMethods.setBackgroundDrawable;
import static com.example.playerslidding.utils.StaticMethods.setMargins;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.playerslidding.R;
import com.example.playerslidding.data.StoreDTO;
import com.example.playerslidding.databinding.ItemCircleProductBinding;
import com.example.playerslidding.databinding.ItemStoreBinding;

import java.util.ArrayList;

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
            return 0;
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
                setMargins(b.getRoot(), dpToPx(10, context), dpToPx(0, context), dpToPx(4, context), 0);
            } else if (getAdapterPosition() == getItemCount() - 1) {
                setMargins(b.getRoot(), dpToPx(4, context), dpToPx(0, context), dpToPx(10, context), 0);
            } else {
                setMargins(b.getRoot(), dpToPx(4, context), dpToPx(0, context), dpToPx(4, context), 0);
            }

            Glide.with(context)
                    .load(stories.get(getAdapterPosition()).getImagePath())
                    .into(b.image);

            b.storeName.setText(stories.get(getAdapterPosition()).getStoreName());

        }
    }
}
