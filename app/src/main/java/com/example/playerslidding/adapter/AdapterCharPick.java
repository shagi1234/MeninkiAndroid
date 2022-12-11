package com.example.playerslidding.adapter;

import static com.example.playerslidding.utils.StaticMethods.dpToPx;
import static com.example.playerslidding.utils.StaticMethods.setBackgroundDrawable;
import static com.example.playerslidding.utils.StaticMethods.setMargins;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.playerslidding.R;
import com.example.playerslidding.databinding.ItemCharPickBinding;

import java.util.ArrayList;

public class AdapterCharPick extends RecyclerView.Adapter<AdapterCharPick.CharImageHolder> {
    private ArrayList<String> picks = new ArrayList<>();
    private Context context;
    private FrameLayout lastClicked;
    private TextView lastText;

    public AdapterCharPick(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public CharImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ItemCharPickBinding popularAudios = ItemCharPickBinding.inflate(layoutInflater, parent, false);
        return new AdapterCharPick.CharImageHolder(popularAudios);
    }

    @Override
    public void onBindViewHolder(@NonNull CharImageHolder holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {
        if (picks == null) {
            return 0;
        }
        return picks.size();
    }

    public class CharImageHolder extends RecyclerView.ViewHolder {
        private final ItemCharPickBinding b;

        public CharImageHolder(@NonNull ItemCharPickBinding itemView) {
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

            b.pickText.setText(picks.get(getAdapterPosition()).trim());

            if (getAdapterPosition() == 0) {
                setBackgroundDrawable(context, b.root, R.color.hover, R.color.accent, 4, false, 3);
                b.pickText.setTextColor(context.getResources().getColor(R.color.accent));
                lastClicked = b.root;
                lastText = b.pickText;
            } else {
                setBackgroundDrawable(context, b.root, R.color.color_transparent, R.color.caption, 4, false, 1);
                b.pickText.setTextColor(context.getResources().getColor(R.color.dark));
            }

            b.getRoot().setOnClickListener(v -> {
                if (lastClicked == b.root) return;

                setBackgroundDrawable(context, b.root, R.color.color_transparent, R.color.accent, 4, false, 3);
                b.pickText.setTextColor(context.getResources().getColor(R.color.accent));

                if (lastClicked != null && lastText != null) {
                    lastText.setTextColor(context.getResources().getColor(R.color.dark));
                    setBackgroundDrawable(context, lastClicked, R.color.color_transparent, R.color.caption, 4, false, 1);
                }
                lastText = b.pickText;
                lastClicked = b.root;
            });


        }
    }

    public void setPicks(ArrayList<String> picks) {
        this.picks = picks;
        notifyDataSetChanged();
    }
}
