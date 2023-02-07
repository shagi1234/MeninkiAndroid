package com.example.playerslidding.adapter;

import static com.example.playerslidding.utils.StaticMethods.dpToPx;
import static com.example.playerslidding.utils.StaticMethods.setBackgroundDrawable;
import static com.example.playerslidding.utils.StaticMethods.setMargins;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.playerslidding.R;
import com.example.playerslidding.data.ProductImageDto;
import com.example.playerslidding.databinding.ItemRedarctorPriceBinding;
import com.example.playerslidding.utils.Dialog;

import java.util.ArrayList;

public class AdapterRedactorPrice extends RecyclerView.Adapter<AdapterRedactorPrice.CharImageHolder> {
    private ArrayList<ProductImageDto> imageUrl = new ArrayList<>();
    private Context context;
    private static AdapterRedactorPrice instance;

    public AdapterRedactorPrice(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public CharImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        instance = this;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ItemRedarctorPriceBinding popularAudios = ItemRedarctorPriceBinding.inflate(layoutInflater, parent, false);
        return new AdapterRedactorPrice.CharImageHolder(popularAudios);
    }

    @Override
    public void onBindViewHolder(@NonNull CharImageHolder holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {
        if (imageUrl == null) {
            return 5;
        }
        return imageUrl.size();
    }

    public class CharImageHolder extends RecyclerView.ViewHolder {
        private final ItemRedarctorPriceBinding b;

        public CharImageHolder(@NonNull ItemRedarctorPriceBinding itemView) {
            super(itemView.getRoot());
            this.b = itemView;
        }

        public void bind() {
            if (getAdapterPosition() == 0) {
                setMargins(b.getRoot(), dpToPx(5, context), 0, 0, 0);
            } else if (getAdapterPosition() == getItemCount() - 1) {
                setMargins(b.getRoot(), 0, 0, dpToPx(5, context), 0);
            } else {
                setMargins(b.getRoot(), 0, 0, 0, 0);
            }
            setBackgroundDrawable(context, b.main, R.color.hover, 0, 4, false, 0);
            setBackgroundDrawable(context, b.root, R.color.color_transparent, R.color.caption, 4, false, 1);
            setBackgroundDrawable(context, b.pickText, R.color.color_transparent, R.color.caption, 4, false, 1);
            setBackgroundDrawable(context, b.oldPrice, R.color.color_transparent, R.color.caption, 4, false, 1);
            setBackgroundDrawable(context, b.price, R.color.color_transparent, R.color.caption, 4, false, 1);


            b.price.setOnClickListener(v -> {
                //add item text
                Dialog dialog = new Dialog();
                dialog.showDialog(context);
                dialog.yesBtn.setOnClickListener(v1 -> {
                    if (dialog.title.getText().toString().trim().length() == 0) {
                        Toast.makeText(context, "Your text is empty, please write something", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    b.price.setText(dialog.title.getText().toString().trim());
                    dialog.dialog.dismiss();
                });
            });

            b.oldPrice.setOnClickListener(v -> {
                Dialog dialog = new Dialog();
                dialog.showDialog(context);
                dialog.yesBtn.setOnClickListener(v1 -> {
                    if (dialog.title.getText().toString().trim().length() == 0) {
                        Toast.makeText(context, "Your text is empty, please write something", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    b.oldPrice.setText(dialog.title.getText().toString().trim());
                    dialog.dialog.dismiss();
                });
            });


        }
    }

    public void setImageUrl(ArrayList<ProductImageDto> imageUrl) {
        this.imageUrl = imageUrl;
        notifyDataSetChanged();
    }

    public static AdapterRedactorPrice getInstance() {
        return instance;
    }
}
