package tm.store.meninki.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import tm.store.meninki.R;
import tm.store.meninki.data.SelectedMedia;
import tm.store.meninki.databinding.ItemMediaAddPostBinding;
import tm.store.meninki.fragment.FragmentOpenGallery;
import tm.store.meninki.utils.Const;
import tm.store.meninki.utils.FragmentHelper;

public class AdapterMediaAddPost extends RecyclerView.Adapter<AdapterMediaAddPost.CharImageHolder> {
    private Context context;
    private static AdapterMediaAddPost instance;

    public AdapterMediaAddPost(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public CharImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        instance = this;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ItemMediaAddPostBinding popularAudios = ItemMediaAddPostBinding.inflate(layoutInflater, parent, false);
        return new AdapterMediaAddPost.CharImageHolder(popularAudios);
    }

    @Override
    public void onBindViewHolder(@NonNull CharImageHolder holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {
        if (SelectedMedia.getArrayList() == null) {
            return 0;
        }
        return SelectedMedia.getArrayList().size() + 1;
    }

    public class CharImageHolder extends RecyclerView.ViewHolder {
        private final ItemMediaAddPostBinding b;

        public CharImageHolder(@NonNull ItemMediaAddPostBinding itemView) {
            super(itemView.getRoot());
            this.b = itemView;
        }

        public void bind() {

            if (getAdapterPosition() == getItemCount() - 1) {
                b.clear.setVisibility(View.GONE);
                b.layAdd.setVisibility(View.VISIBLE);
                b.image.setImageResource(R.color.on_bg_ls);

            } else {
                b.clear.setVisibility(View.VISIBLE);
                b.layAdd.setVisibility(View.GONE);

                Glide.with(context)
                        .load(SelectedMedia.getArrayList().get(getAdapterPosition()).getPath())
                        .placeholder(R.color.on_bg_ls)
                        .into(b.image);
            }

            b.click.setOnClickListener(v -> {
                if (getAdapterPosition() == getItemCount() - 1) {
                    FragmentHelper.addFragment(Const.mainFragmentManager, R.id.fragment_container_main, FragmentOpenGallery.newInstance(0));
                }
            });

            b.clear.setOnClickListener(v -> removeAt(getAdapterPosition()));
        }
    }

    private void removeAt(int position) {
        try {
            SelectedMedia.getArrayList().remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, SelectedMedia.getArrayList().size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static AdapterMediaAddPost getInstance() {
        return instance;
    }
}
