package tm.store.meninki.adapter;

import static tm.store.meninki.utils.StaticMethods.setBackgroundDrawable;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import tm.store.meninki.R;
import tm.store.meninki.databinding.ItemNewOrderBinding;

public class AdapterOrders extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;

    public AdapterOrders(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ItemNewOrderBinding itemNewOrder = ItemNewOrderBinding.inflate(inflater, parent, false);
        return new NewOrdersHolder(itemNewOrder);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((NewOrdersHolder) holder).bind();
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class NewOrdersHolder extends RecyclerView.ViewHolder {
        ItemNewOrderBinding b;

        public NewOrdersHolder(ItemNewOrderBinding b) {
            super(b.getRoot());
            this.b = b;
        }

        private void bind() {
            setBackgrounds();

        }

        private void setBackgrounds() {
            setBackgroundDrawable(context, b.customerLay, R.color.on_bg_ls, 0, 10, false, 0);
            setBackgroundDrawable(context, b.phoneLay, R.color.on_bg_ls, 0, 10, false, 0);
            setBackgroundDrawable(context, b.adressLay, R.color.on_bg_ls, 0, 10, false, 0);
            setBackgroundDrawable(context, b.commentsLay, R.color.on_bg_ls, 0, 10, false, 0);
            setBackgroundDrawable(context, b.deliveredLay, R.color.contrast, 0, 50, false, 0);
            setBackgroundDrawable(context, b.cancelLay, R.color.alert, 0, 50, false, 0);
        }
    }

}
