package com.example.playerslidding.adapter;

import static com.example.playerslidding.utils.StaticMethods.dpToPx;
import static com.example.playerslidding.utils.StaticMethods.setMargins;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.playerslidding.R;
import com.example.playerslidding.data.CategoryDto;
import com.example.playerslidding.databinding.ItemTextBinding;

import java.util.ArrayList;

public class AdapterText extends RecyclerView.Adapter<AdapterText.TabLayoutHolder> {
    private Context context;
    private ArrayList<CategoryDto> tabs = new ArrayList<>();

    public AdapterText(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public TabLayoutHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ItemTextBinding tab = ItemTextBinding.inflate(layoutInflater, parent, false);
        return new AdapterText.TabLayoutHolder(tab);
    }

    @Override
    public void onBindViewHolder(@NonNull TabLayoutHolder holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {
        if (tabs == null) {
            return 0;
        }
        return tabs.size();
    }

    public class TabLayoutHolder extends RecyclerView.ViewHolder {
        private final ItemTextBinding b;

        public TabLayoutHolder(@NonNull ItemTextBinding itemView) {
            super(itemView.getRoot());
            b = itemView;
        }

        public void bind() {

            if (getAdapterPosition() == 0) {
                setMargins(b.getRoot(), dpToPx(40, context), dpToPx(40, context), dpToPx(20, context), dpToPx(10, context));
            } else if (getAdapterPosition() == getItemCount() - 1) {
                setMargins(b.getRoot(), dpToPx(40, context), dpToPx(10, context), dpToPx(20, context), dpToPx(40, context));
            } else {
                setMargins(b.getRoot(), dpToPx(40, context), dpToPx(10, context), dpToPx(20, context), dpToPx(10, context));
            }


            b.title.setText(tabs.get(getAdapterPosition()).getName());

            setActive(tabs.get(getAdapterPosition()).isActive());

            b.getRoot().setOnClickListener(v -> {

            });

        }

        private void setActive(boolean active) {
            if (active) {
                b.title.setTextColor(context.getResources().getColor(R.color.alert));
                b.point.setVisibility(View.VISIBLE);
            } else {
                b.point.setVisibility(View.GONE);
                b.title.setTextColor(context.getResources().getColor(R.color.black));
            }
        }

    }

    public void setTabs(ArrayList<CategoryDto> tabs) {
        this.tabs = tabs;
        notifyDataSetChanged();
    }
}
