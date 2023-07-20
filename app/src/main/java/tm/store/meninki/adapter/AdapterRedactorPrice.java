package tm.store.meninki.adapter;

import static tm.store.meninki.api.Network.BASE_URL;
import static tm.store.meninki.utils.StaticMethods.dpToPx;
import static tm.store.meninki.utils.StaticMethods.setBackgroundDrawable;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import tm.store.meninki.R;
import tm.store.meninki.api.data.OptionDto;
import tm.store.meninki.api.data.PersonalCharacterDto;
import tm.store.meninki.databinding.ItemRedarctorPriceBinding;
import tm.store.meninki.utils.Option;

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
            int adapterPosition = getAdapterPosition();
            ArrayList<OptionDto> options = characters.get(getAdapterPosition()).getOptions();

            if (options != null)
                for (int i = 0; i < options.size(); i++) {
                    if (options.get(i).getOptionType() == Option.CHARACTER_TEXT) {
                        if (options.get(i).getValue() != null)
                            addText(options.get(i).getValue());
                    } else {
                        if (options.get(i).getImagePath() != null)
                            addImage(BASE_URL + "/" + options.get(i).getImagePath());
                    }
                }
            b.edtPrice.setText(characters.get(getAdapterPosition()).getPrice() + "");
            b.edtOldPrice.setText(characters.get(getAdapterPosition()).getDiscountPrice() + "");

            b.edtOldPrice.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (b.edtPrice.getText().toString().trim().isEmpty()) {
                        characters.get(adapterPosition).setDiscountPrice(0f);
                    } else
                        try {
                            characters.get(adapterPosition).setDiscountPrice(Float.parseFloat(b.edtOldPrice.getText().toString().trim()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            b.edtPrice.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (b.edtPrice.getText().toString().trim().isEmpty()) {
                        characters.get(adapterPosition).setPrice(0f);
                    } else
                        try {
                            characters.get(adapterPosition).setPrice(Float.parseFloat(b.edtPrice.getText().toString().trim()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });


        }

        private void addImage(String imagePath) {
            RoundedImageView image = new RoundedImageView(context);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(dpToPx(40, context), dpToPx(40, context));
            lp.leftMargin = dpToPx(5, context);
            lp.rightMargin = dpToPx(5, context);
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            image.setLayoutParams(lp);
            image.setCornerRadius(10);
            image.setImageResource(R.color.low_contrast);
            b.optionsLay.addView(image);

            Glide.with(context)
                    .load(imagePath)
                    .placeholder(R.color.low_contrast)
                    .into(image);
        }

        private void addText(String value) {
            TextView tv = new TextView(context);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.leftMargin = dpToPx(5, context);
            lp.rightMargin = dpToPx(5, context);
            lp.gravity = Gravity.CENTER;
            tv.setLayoutParams(lp);
            tv.setText(value);
            tv.setPadding(dpToPx(14, context), dpToPx(10, context), dpToPx(14, context), dpToPx(10, context));
            tv.setTextColor(context.getResources().getColor(R.color.contrast));
            Typeface customFont = ResourcesCompat.getFont(context, R.font.inter_medium);
            tv.setTypeface(customFont);
            b.optionsLay.addView(tv);
        }

    }


}
