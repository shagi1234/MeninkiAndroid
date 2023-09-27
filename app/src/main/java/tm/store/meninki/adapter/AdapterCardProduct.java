package tm.store.meninki.adapter;

import static tm.store.meninki.api.Network.BASE_URL;
import static tm.store.meninki.utils.StaticMethods.setBackgroundDrawable;
import static tm.store.meninki.utils.StaticMethods.setMargins;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import tm.store.meninki.R;
import tm.store.meninki.api.data.OptionDto;
import tm.store.meninki.api.data.response.ResponseCard;
import tm.store.meninki.data.ShopDTO;
import tm.store.meninki.databinding.ItemCharchtersInCardBinding;
import tm.store.meninki.databinding.ItemImageBasketBinding;
import tm.store.meninki.utils.Option;

public class AdapterCardProduct extends RecyclerView.Adapter<AdapterCardProduct.CharImageHolder> {
    private ArrayList<ResponseCard> products;
    private Context context;
    private ShopDTO shop;

    public AdapterCardProduct(Context context, ArrayList<ResponseCard> responseCards, ShopDTO shop) {
        this.context = context;
        this.products = responseCards;
        this.shop = shop;
    }

    @NonNull
    @Override
    public CharImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ItemImageBasketBinding popularAudios = ItemImageBasketBinding.inflate(layoutInflater, parent, false);
        return new AdapterCardProduct.CharImageHolder(popularAudios);
    }

    @Override
    public void onBindViewHolder(@NonNull CharImageHolder holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {
        if (products == null) {
            return 0;
        }
        return products.size();
    }

    public class CharImageHolder extends RecyclerView.ViewHolder {
        private final ItemImageBasketBinding b;

        public CharImageHolder(@NonNull ItemImageBasketBinding itemView) {
            super(itemView.getRoot());
            this.b = itemView;
        }

        public void bind() {

            if (products == null || products.get(getAdapterPosition()) == null) {
                return;
            }

            Glide.with(context)
                    .load(BASE_URL + "/" + products.get(getAdapterPosition()).getImages()[0])
                    .placeholder(R.color.low_contrast)
                    .into(b.image);

            Glide.with(context).load(BASE_URL + "/" + shop.getImgPath()).into(b.posterImage);

            b.count.setText(products.get(getAdapterPosition()).getCount() + "");

            setCharacteristics();

            b.btnAdd.setOnClickListener(v -> {
                ResponseCard data = products.get(getAdapterPosition());
                data.setCount(data.getCount() + 1);
                notifyItemChanged(getAdapterPosition());
            });

            b.btnSubs.setOnClickListener(v -> {
                ResponseCard data = products.get(getAdapterPosition());
                if (data.getCount() == 0) return;

                data.setCount(data.getCount() - 1);
                notifyItemChanged(getAdapterPosition());
            });

            b.price.setText(products.get(getAdapterPosition()).getPrice() + " TMT");
            b.oldPrice.setText(products.get(getAdapterPosition()).getDiscountPrice() + " TMT");

            b.name.setText(products.get(getAdapterPosition()).getName());
            setBackgroundDrawable(context, b.countController, R.color.bg, 0, 40, false, 0);


        }

        private void setCharacteristics() {
            ArrayList<String> optionTitles = products.get(getAdapterPosition()).getOptionTitles();
            ArrayList<OptionDto> options = products.get(getAdapterPosition()).getOption();
            b.layCharacters.removeAllViews();

            for (int i = 0; i < optionTitles.size(); i++) {

                ItemCharchtersInCardBinding c = ItemCharchtersInCardBinding.inflate(LayoutInflater.from(context));
                c.name.setText(optionTitles.get(i) + " : ");

                if (options.get(i).getOptionType() == Option.CHARACTER_IMAGE) {
                    c.valImg.setVisibility(View.VISIBLE);
                    c.valText.setVisibility(View.GONE);
                    Glide.with(context)
                            .load(BASE_URL + "/" + options.get(i).getImagePath())
                            .placeholder(R.color.neutral_dark)
                            .error(R.color.neutral_dark)
                            .into(c.valImg);
                } else {
                    c.valImg.setVisibility(View.GONE);
                    c.valText.setVisibility(View.VISIBLE);
                    c.valText.setText(options.get(i).getValue());
                }

                b.layCharacters.addView(c.getRoot());
            }


        }
    }
}
