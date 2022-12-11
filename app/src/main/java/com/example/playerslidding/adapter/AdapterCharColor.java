package com.example.playerslidding.adapter;

import static com.example.playerslidding.utils.StaticMethods.dpToPx;
import static com.example.playerslidding.utils.StaticMethods.setBackgroundDrawable;
import static com.example.playerslidding.utils.StaticMethods.setMargins;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.playerslidding.R;
import com.example.playerslidding.databinding.ItemCharColorBinding;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class AdapterCharColor extends RecyclerView.Adapter<AdapterCharColor.CharImageHolder> {
    private ArrayList<Integer> color = new ArrayList<>();
    private Context context;
    private RoundedImageView lastClicked;
    private Integer last;

    public AdapterCharColor(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public CharImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ItemCharColorBinding popularAudios = ItemCharColorBinding.inflate(layoutInflater, parent, false);
        return new AdapterCharColor.CharImageHolder(popularAudios);
    }

    @Override
    public void onBindViewHolder(@NonNull CharImageHolder holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {
        if (color == null) {
            return 0;
        }
        return color.size();
    }

    public class CharImageHolder extends RecyclerView.ViewHolder {
        private final ItemCharColorBinding b;

        public CharImageHolder(@NonNull ItemCharColorBinding itemView) {
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


            if (getAdapterPosition() == 0) {
                setBackgroundDrawable(context, b.color, color.get(getAdapterPosition()), R.color.accent, 0, true, 3);
                lastClicked = b.color;
                last = 0;
            } else {
                setBackgroundDrawable(context, b.color, color.get(getAdapterPosition()), 0, 0, true, 0);
            }

            b.getRoot().setOnClickListener(v -> {
                if (getAdapterPosition() == last) return;

                setBackgroundDrawable(context, b.color, color.get(getAdapterPosition()), R.color.accent, 0, true, 3);

                if (lastClicked != null && last != null)
                    setBackgroundDrawable(context, lastClicked, color.get(last), 0, 0, true, 0);

                lastClicked = b.color;
                last = getAdapterPosition();
            });


        }
    }

    public void setColor(ArrayList<Integer> color) {
        this.color = color;
        notifyDataSetChanged();
    }
}
