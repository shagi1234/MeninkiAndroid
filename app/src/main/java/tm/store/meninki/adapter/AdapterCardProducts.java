package tm.store.meninki.adapter;

import static tm.store.meninki.api.Network.BASE_URL;
import static tm.store.meninki.utils.StaticMethods.setBackgroundDrawable;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.ArrayList;

import tm.store.meninki.R;
import tm.store.meninki.api.data.ResponseOrderGetAll;
import tm.store.meninki.databinding.ItemBasketBinding;

public class AdapterCardProducts extends RecyclerView.Adapter<AdapterCardProducts.BasketHolder> {
    private ArrayList<ResponseOrderGetAll> responseOrders;
    private Context context;

    public AdapterCardProducts(Context context, ArrayList<ResponseOrderGetAll> responseOrders) {
        this.responseOrders = responseOrders;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterCardProducts.BasketHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBasketBinding b = ItemBasketBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new AdapterCardProducts.BasketHolder(b);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCardProducts.BasketHolder holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return responseOrders == null ? 0 : responseOrders.size();
    }

    public class BasketHolder extends RecyclerView.ViewHolder {
        private final ItemBasketBinding b;
        private AdapterImageHorizontal adapterImageHorizontal;

        public BasketHolder(ItemBasketBinding b) {
            super(b.getRoot());
            this.b = b;
        }

        public void bind() {

            setBackgroundDrawable(context, b.posterImage, R.color.neutral_dark, R.color.accent, 0, true, 2);

            setRecycler();

            Glide.with(context).load(BASE_URL + "/" + responseOrders.get(getAdapterPosition()).getShop().getImgPath()).into(b.posterImage);

            b.storeName.setText(responseOrders.get(getAdapterPosition()).getShop().getName());

        }

        private void setRecycler() {
            adapterImageHorizontal = new AdapterImageHorizontal(context, responseOrders.get(getAdapterPosition()).getProducts());
            b.recImages.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            b.recImages.setAdapter(adapterImageHorizontal);
        }
    }
}
