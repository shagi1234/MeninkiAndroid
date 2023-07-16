package tm.store.meninki.adapter;

import static tm.store.meninki.utils.StaticMethods.dpToPx;
import static tm.store.meninki.utils.StaticMethods.setBackgroundDrawable;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Stack;

import tm.store.meninki.R;
import tm.store.meninki.api.data.OptionDto;
import tm.store.meninki.api.data.PersonalCharacterDto;
import tm.store.meninki.databinding.ItemRedarctorPriceBinding;
import tm.store.meninki.utils.Option;
import tm.store.meninki.utils.StaticMethods;

public class AdapterRedactorPrice extends RecyclerView.Adapter<AdapterRedactorPrice.CharImageHolder> {
    private ArrayList<PersonalCharacterDto> characters;
    private Context context;

    public AdapterRedactorPrice(Context context, ArrayList<PersonalCharacterDto> body) {
        this.context = context;
        this.characters = body;
    }

    @NonNull
    @Override
    public CharImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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
        if (characters == null) {
            return 0;
        }
        return characters.size();
    }

    public class CharImageHolder extends RecyclerView.ViewHolder {
        private final ItemRedarctorPriceBinding b;

        public CharImageHolder(@NonNull ItemRedarctorPriceBinding itemView) {
            super(itemView.getRoot());
            this.b = itemView;
        }

        public void bind() {
            setBackgroundDrawable(context, b.root, R.color.white, R.color.low_contrast, 10, false, 1);
            setBackgroundDrawable(context, b.edtPrice, R.color.bg, 0, 10, false, 0);
            setBackgroundDrawable(context, b.edtOldPrice, R.color.bg, R.color.low_contrast, 10, false, 1);

            ArrayList<OptionDto> options = characters.get(getAdapterPosition()).getOptions();

            if (options != null)
                for (int i = 0; i < options.size(); i++) {
                    if (options.get(i).getOptionType() == Option.CHARACTER_TEXT) {
                        if (options.get(i).getValue() == null) return;

                        b.txtOption.setText(options.get(i).getValue());
                    } else {
                        if (options.get(i).getImagePath() == null) return;
                        Glide.with(context)
                                .load(options.get(i).getImagePath())
                                .placeholder(R.color.low_contrast)
                                .into(b.imgOption);
                    }
                }


            if (getAdapterPosition() == getItemCount() - 1) {
                StaticMethods.setMargins(b.getRoot(), dpToPx(14, context), dpToPx(5, context), dpToPx(14, context), dpToPx(80, context));
            }
        }

    }

}
