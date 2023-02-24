package tm.store.meninki.adapter;

import static tm.store.meninki.utils.StaticMethods.dpToPx;
import static tm.store.meninki.utils.StaticMethods.setBackgroundDrawable;
import static tm.store.meninki.utils.StaticMethods.setMargins;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import tm.store.meninki.R;
import tm.store.meninki.data.ProductImageDto;
import tm.store.meninki.databinding.ItemCharImageBinding;
import tm.store.meninki.fragment.FragmentOpenGallery;

import java.util.ArrayList;

import tm.store.meninki.utils.Const;
import tm.store.meninki.utils.FragmentHelper;
import tm.store.meninki.utils.StaticMethods;

public class AdapterCharImage extends RecyclerView.Adapter<AdapterCharImage.CharImageHolder> {
    private ArrayList<ProductImageDto> imageUrl = new ArrayList<>();
    private Context context;
    private int isAddable;
    private LinearLayout lastClicked;
    private static AdapterCharImage instance;

    public AdapterCharImage(Context context, int isAddable) {
        this.context = context;
        this.isAddable = isAddable;
    }

    @NonNull
    @Override
    public CharImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        instance = this;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ItemCharImageBinding popularAudios = ItemCharImageBinding.inflate(layoutInflater, parent, false);
        return new AdapterCharImage.CharImageHolder(popularAudios);
    }

    @Override
    public void onBindViewHolder(@NonNull CharImageHolder holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {
        if (imageUrl == null) {
            return 0;
        }
        if (isAddable == AdapterCharPick.ADDABLE) {
            return imageUrl.size() + 1;
        }

        return imageUrl.size();
    }

    public class CharImageHolder extends RecyclerView.ViewHolder {
        private final ItemCharImageBinding b;

        public CharImageHolder(@NonNull ItemCharImageBinding itemView) {
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

            StaticMethods.setBackgroundDrawable(context, b.txtProductCharacteristic, R.color.hover, 0, 0, 0, 10, 10, false, 0);

            if (getAdapterPosition() == 0 && isAddable == AdapterCharPick.NOT_ADDABLE) {
                b.click.setBackgroundResource(R.drawable.ripple);
                b.main.setVisibility(View.VISIBLE);
                b.layAdd.setVisibility(View.GONE);
                b.txtProductCharacteristic.setText(imageUrl.get(getAdapterPosition()).getTitle());
                Glide.with(context).load(imageUrl.get(getAdapterPosition()).getImagePath()).placeholder(R.color.holder).into(b.image);
                setBackgroundDrawable(context, b.main, R.color.hover, R.color.accent, 4, false, 3);
                lastClicked = b.main;
            } else if (getAdapterPosition() == getItemCount() - 1 && isAddable == AdapterCharPick.ADDABLE) {
                b.main.setVisibility(View.INVISIBLE);
                b.layAdd.setVisibility(View.VISIBLE);
                b.click.setBackgroundResource(R.drawable.ripple_corner_20);
            } else {
                b.click.setBackgroundResource(R.drawable.ripple);
                b.main.setVisibility(View.VISIBLE);
                Glide.with(context).load(imageUrl.get(getAdapterPosition()).getImagePath()).placeholder(R.color.holder).into(b.image);
                b.txtProductCharacteristic.setText(imageUrl.get(getAdapterPosition()).getTitle());
                b.layAdd.setVisibility(View.GONE);
                setBackgroundDrawable(context, b.main, R.color.hover, 0, 4, false, 0);
            }

            b.click.setOnClickListener(v -> {

                if (isAddable == AdapterCharPick.ADDABLE) {
                    if (getAdapterPosition() == getItemCount() - 1) {
                        FragmentHelper.addFragment(Const.mainFragmentManager, R.id.fragment_container_main, FragmentOpenGallery.newInstance(1));
                    }
                    return;
                }

                if (lastClicked == b.main) return;

                setBackgroundDrawable(context, b.main, R.color.hover, R.color.accent, 4, false, 3);

                if (lastClicked != null)
                    setBackgroundDrawable(context, lastClicked, R.color.hover, 0, 4, false, 0);

                lastClicked = b.main;
            });


        }
    }

    public void setImageUrl(ArrayList<ProductImageDto> imageUrl) {
        this.imageUrl = imageUrl;
        notifyDataSetChanged();
    }

    public static AdapterCharImage getInstance() {
        return instance;
    }
}
