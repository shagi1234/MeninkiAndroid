package tm.store.meninki.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import tm.store.meninki.databinding.ItemProfileShopsBinding;

public class AdapterProfileShops extends RecyclerView.Adapter<AdapterProfileShops.ViewHolder> {
    private Context context;

    public AdapterProfileShops(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemProfileShopsBinding item = ItemProfileShopsBinding.inflate(inflater, parent, false);
        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemProfileShopsBinding b;

        public ViewHolder(ItemProfileShopsBinding b) {
            super(b.getRoot());
            this.b = b;
        }

        public void bind() {
            if (getAdapterPosition() == getItemCount() - 1) {
                b.shopsLay.setVisibility(View.GONE);
                b.addShopLay.setVisibility(View.VISIBLE);
            }
        }
    }

}
