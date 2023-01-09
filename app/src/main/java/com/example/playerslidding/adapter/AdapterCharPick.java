package com.example.playerslidding.adapter;

import static com.example.playerslidding.utils.StaticMethods.dpToPx;
import static com.example.playerslidding.utils.StaticMethods.setBackgroundDrawable;
import static com.example.playerslidding.utils.StaticMethods.setMargins;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.playerslidding.R;
import com.example.playerslidding.databinding.ItemCharPickBinding;
import com.example.playerslidding.utils.Dialog;

import java.util.ArrayList;

public class AdapterCharPick extends RecyclerView.Adapter<AdapterCharPick.CharImageHolder> {
    private ArrayList<String> picks = new ArrayList<>();
    private Context context;
    private int isAddable;
    public static int NOT_ADDABLE = 0;
    public static int ADDABLE = 1;
    private ConstraintLayout lastClicked;
    private TextView lastText;

    public AdapterCharPick(Context context, int isAddable) {
        this.context = context;
        this.isAddable = isAddable;
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
        if (isAddable == AdapterCharPick.ADDABLE) {
            return picks.size() + 1;
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

            if (getAdapterPosition() == 0 && isAddable == AdapterCharPick.NOT_ADDABLE) {
                b.layAdd.setVisibility(View.GONE);
                b.pickText.setVisibility(View.VISIBLE);
                b.click.setBackgroundResource(R.drawable.ripple);
                setBackgroundDrawable(context, b.root, R.color.hover, R.color.accent, 4, false, 3);
                b.pickText.setTextColor(context.getResources().getColor(R.color.accent));
                b.pickText.setText(picks.get(getAdapterPosition()).trim());
                lastClicked = b.root;
                lastText = b.pickText;
            } else if (getAdapterPosition() == getItemCount() - 1 && isAddable == ADDABLE) {
                b.pickText.setVisibility(View.INVISIBLE);
                setBackgroundDrawable(context, b.layAdd, R.color.grey, 0, 20, false, 0);
                b.layAdd.setVisibility(View.VISIBLE);
                b.click.setBackgroundResource(R.drawable.ripple_corner_20);
            } else {
                b.layAdd.setVisibility(View.GONE);
                b.pickText.setVisibility(View.VISIBLE);
                b.click.setBackgroundResource(R.drawable.ripple);
                setBackgroundDrawable(context, b.root, R.color.color_transparent, R.color.caption, 4, false, 1);
                b.pickText.setTextColor(context.getResources().getColor(R.color.dark));
                b.pickText.setText(picks.get(getAdapterPosition()).trim());
            }

            b.click.setOnClickListener(v -> {
                if (isAddable == ADDABLE) {
                    if (getAdapterPosition() == getItemCount() - 1) {
                        //add item text
                        Dialog dialog = new Dialog();
                        dialog.showDialog(context);
                        dialog.yesBtn.setOnClickListener(v1 -> {
                            if (dialog.title.getText().toString().trim().length() == 0) {
                                Toast.makeText(context, "Your text is empty, please write something", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            addText(dialog.title.getText().toString().trim());
                            dialog.dialog.dismiss();
                        });
                    }
                    return;
                }
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

    private void addText(String trim) {
        picks.add(trim);
        notifyItemInserted(picks.size() - 1);
    }

    public void setPicks(ArrayList<String> picks) {
        this.picks = picks;
        notifyDataSetChanged();
    }
}
