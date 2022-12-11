package com.example.playerslidding.adapter;

import static com.example.playerslidding.utils.StaticMethods.dpToPx;
import static com.example.playerslidding.utils.StaticMethods.setBackgroundDrawable;
import static com.example.playerslidding.utils.StaticMethods.setMargins;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.playerslidding.R;
import com.example.playerslidding.databinding.ItemCharImageBinding;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class AdapterCharImage extends RecyclerView.Adapter<AdapterCharImage.CharImageHolder> {
    private ArrayList<String> imageUrl = new ArrayList<>();
    private Context context;
    private RoundedImageView lastClicked;

    public AdapterCharImage(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public CharImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ItemCharImageBinding popularAudios = ItemCharImageBinding.inflate(layoutInflater, parent, false);
        return new AdapterCharImage.CharImageHolder(popularAudios);
    }

    @Override
    public void onBindViewHolder(@NonNull CharImageHolder holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {
        if (imageUrl == null) {
            return 0;
        }
        return imageUrl.size();
    }

    public class CharImageHolder extends RecyclerView.ViewHolder {
        private final ItemCharImageBinding b;

        public CharImageHolder(@NonNull ItemCharImageBinding itemView) {
            super(itemView.getRoot());
            this.b = itemView;
        }

        public void bind() {
            if (getAdapterPosition() == 0) {
                setMargins(b.getRoot(), dpToPx(20, context), dpToPx(0, context), dpToPx(4, context), 0);
            } else if (getAdapterPosition() == getItemCount() - 1) {
                setMargins(b.getRoot(), dpToPx(4, context), dpToPx(0, context), dpToPx(20, context), 0);
            } else {
                setMargins(b.getRoot(), dpToPx(4, context), dpToPx(0, context), dpToPx(4, context), 0);
            }

            Glide.with(context).load(imageUrl.get(getAdapterPosition())).placeholder(R.color.holder).into(b.image);

            if (getAdapterPosition() == 0) {
                setBackgroundDrawable(context, b.image, R.color.hover, R.color.accent, 4, false, 3);
                lastClicked = b.image;
            } else {
                setBackgroundDrawable(context, b.image, R.color.hover, 0, 4, false, 0);
            }

            b.getRoot().setOnClickListener(v -> {
                if (lastClicked == b.image) return;

                setBackgroundDrawable(context, b.image, R.color.hover, R.color.accent, 4, false, 3);

                if (lastClicked != null)
                    setBackgroundDrawable(context, lastClicked, R.color.hover, 0, 4, false, 0);

                lastClicked = b.image;
            });


        }
    }

    public void setImageUrl(ArrayList<String> imageUrl) {
        this.imageUrl = imageUrl;
        notifyDataSetChanged();
    }
}
