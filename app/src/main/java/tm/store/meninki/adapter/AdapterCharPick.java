package tm.store.meninki.adapter;

import static tm.store.meninki.utils.StaticMethods.setBackgroundDrawable;
import static tm.store.meninki.utils.StaticMethods.setMargins;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.UUID;

import tm.store.meninki.R;
import tm.store.meninki.api.data.OptionDto;
import tm.store.meninki.databinding.ItemCharPickBinding;
import tm.store.meninki.utils.Dialog;
import tm.store.meninki.utils.Option;
import tm.store.meninki.utils.StaticMethods;

public class AdapterCharPick extends RecyclerView.Adapter<AdapterCharPick.CharImageHolder> {
    private ArrayList<OptionDto> options = new ArrayList<>();
    private Context context;
    private int isAddable;
    public static int NOT_ADDABLE = 0;
    public static int ADDABLE = 1;
    private ConstraintLayout lastClicked;
    private TextView lastText;
    private String prodId;

    public AdapterCharPick(Context context, int isAddable, String prodId, int adapterPosition) {
        this.context = context;
        this.isAddable = isAddable;
        this.prodId = prodId;
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
        if (options == null) {
            return 0;
        }
        if (isAddable == AdapterCharPick.ADDABLE) {
            return options.size() + 1;
        }

        return options.size();
    }

    public class CharImageHolder extends RecyclerView.ViewHolder {
        private final ItemCharPickBinding b;

        public CharImageHolder(@NonNull ItemCharPickBinding itemView) {
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

            if (getAdapterPosition() == 0 && isAddable == AdapterCharPick.NOT_ADDABLE) {
                b.layAdd.setVisibility(View.GONE);
                b.pickText.setVisibility(View.VISIBLE);
                b.click.setBackgroundResource(R.drawable.ripple);
                setBackgroundDrawable(context, b.root, R.color.on_bg_ls, 0, 10, false, 0);
                b.pickText.setTextColor(context.getResources().getColor(R.color.contrast));
                b.pickText.setText(options.get(getAdapterPosition()).getValue());
                lastClicked = b.root;
                lastText = b.pickText;
            } else if (getAdapterPosition() == getItemCount() - 1 && isAddable == ADDABLE) {
                b.pickText.setVisibility(View.INVISIBLE);
                setBackgroundDrawable(context, b.layAdd, R.color.low_contrast, 0, 20, false, 0);
                b.layAdd.setVisibility(View.VISIBLE);
                b.click.setBackgroundResource(R.drawable.ripple_corner_20);
            } else {
                b.layAdd.setVisibility(View.GONE);
                b.pickText.setVisibility(View.VISIBLE);
                b.click.setBackgroundResource(R.drawable.ripple);
                setBackgroundDrawable(context, b.root, R.color.on_bg_ls, 0, 10, false, 0);
                b.pickText.setTextColor(context.getResources().getColor(R.color.contrast));
                b.pickText.setText(options.get(getAdapterPosition()).getValue());
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
                    lastText.setTextColor(context.getResources().getColor(R.color.contrast));
                    setBackgroundDrawable(context, lastClicked, R.color.color_transparent, R.color.neutral_dark, 4, false, 1);
                }
                lastText = b.pickText;
                lastClicked = b.root;
            });


        }
    }

    private void addText(String trim) {
        OptionDto optionDto = new OptionDto();
        optionDto.setOptionType(Option.CHARACTER_TEXT);
        optionDto.setValue(trim);
        optionDto.setId(UUID.randomUUID().toString());
        optionDto.setProductId(prodId);
        optionDto.setOptionLevel(options.size());

        options.add(optionDto);
        notifyItemInserted(options.size() - 1);
    }

    public void setTexts(ArrayList<OptionDto> picks) {
        this.options = picks;
        notifyDataSetChanged();
    }
}
