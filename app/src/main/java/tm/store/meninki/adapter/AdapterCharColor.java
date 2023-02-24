package tm.store.meninki.adapter;

import static tm.store.meninki.utils.StaticMethods.dpToPx;
import static tm.store.meninki.utils.StaticMethods.setBackgroundDrawable;
import static tm.store.meninki.utils.StaticMethods.setMargins;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import tm.store.meninki.R;
import tm.store.meninki.databinding.ItemCharColorBinding;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

import tm.store.meninki.utils.StaticMethods;

public class AdapterCharColor extends RecyclerView.Adapter<AdapterCharColor.CharImageHolder> {
    private ArrayList<String> color = new ArrayList<>();
    private Context context;
    private RoundedImageView lastClicked;
    private int isAddable;
    private Integer last;

    public AdapterCharColor(Context context, int isAddable) {
        this.context = context;
        this.isAddable = isAddable;
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

        if (isAddable == AdapterCharPick.ADDABLE) {
            return color.size() + 1;
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
                setMargins(b.getRoot(), StaticMethods.dpToPx(20, context), StaticMethods.dpToPx(0, context), StaticMethods.dpToPx(4, context), 0);
            } else if (getAdapterPosition() == getItemCount() - 1) {
                setMargins(b.getRoot(), StaticMethods.dpToPx(4, context), StaticMethods.dpToPx(0, context), StaticMethods.dpToPx(20, context), 0);
            } else {
                setMargins(b.getRoot(), StaticMethods.dpToPx(4, context), StaticMethods.dpToPx(0, context), StaticMethods.dpToPx(4, context), 0);
            }


            if (getAdapterPosition() == 0 && isAddable == AdapterCharPick.NOT_ADDABLE) {
                setBackgroundDrawable(context, b.color, color.get(getAdapterPosition()), R.color.accent, 0, true, 3);
                lastClicked = b.color;
                last = 0;
            } else if (getAdapterPosition() == getItemCount() - 1 && isAddable == AdapterCharPick.ADDABLE) {
                setBackgroundDrawable(context, b.image, R.color.color_transparent, R.color.accent, 0, true, 3);
                b.color.setImageResource(R.drawable.ic_add);
            } else {
                setBackgroundDrawable(context, b.color, color.get(getAdapterPosition()), 0, 0, true, 0);
            }

            b.getRoot().setOnClickListener(v -> {
                if (isAddable == AdapterCharPick.ADDABLE) {
                    if (getAdapterPosition() == getItemCount() - 1) {

                        ColorPickerDialogBuilder
                                .with(context)
                                .setTitle("Choose color")
                                .initialColor(R.color.holder)
                                .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
                                .density(10)
                                .setPositiveButton("Ok", (dialog, selectedColor, allColors) -> addColor(Integer.toHexString(selectedColor)))
                                .build()
                                .show();

                    }
                    return;
                }
                if (getAdapterPosition() == last) return;

                setBackgroundDrawable(context, b.color, color.get(getAdapterPosition()), R.color.accent, 0, true, 3);

                if (lastClicked != null && last != null)
                    StaticMethods.setBackgroundDrawable(context, lastClicked, color.get(last), 0, 0, true, 0);

                lastClicked = b.color;
                last = getAdapterPosition();
            });


        }
    }

    private void addColor(String selectedColor) {
        color.add("#"+selectedColor);
        notifyItemInserted(color.size() - 1);
    }

    public void setColor(ArrayList<String> color) {
        this.color = color;
        notifyDataSetChanged();
    }
}
