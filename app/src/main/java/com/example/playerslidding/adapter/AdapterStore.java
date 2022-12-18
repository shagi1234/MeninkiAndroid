package com.example.playerslidding.adapter;

import static com.example.playerslidding.utils.Const.mainFragmentManager;
import static com.example.playerslidding.utils.FragmentHelper.addFragment;
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
import com.example.playerslidding.databinding.ItemStoreBinding;
import com.example.playerslidding.fragment.FragmentProduct;

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
                setMargins(b.getRoot(), dpToPx(10, context), dpToPx(20, context), dpToPx(4, context), 0);
            } else if (getAdapterPosition() == getItemCount() - 1) {
                setMargins(b.getRoot(), dpToPx(4, context), dpToPx(20, context), dpToPx(10, context), 0);
            } else {
                setMargins(b.getRoot(), dpToPx(4, context), dpToPx(20, context), dpToPx(4, context), 0);
            }

            setBackgroundDrawable(context, b.posterImage, R.color.holder, R.color.accent, 0, true, 2);

            b.getRoot().setOnClickListener(v -> addFragment(mainFragmentManager, R.id.fragment_container_main, FragmentProduct.newInstance("", FragmentProduct.STORE)));

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
