package tm.store.meninki.adapter;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static tm.store.meninki.utils.StaticMethods.dpToPx;
import static tm.store.meninki.utils.StaticMethods.getWindowWidth;
import static tm.store.meninki.utils.StaticMethods.navigationBarHeight;
import static tm.store.meninki.utils.StaticMethods.pxToDp;
import static tm.store.meninki.utils.StaticMethods.setBackgroundDrawable;
import static tm.store.meninki.utils.StaticMethods.setMargins;
import static tm.store.meninki.utils.StaticMethods.setPadding;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
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
    private FragmentActivity activity;
    private boolean isHasVideo;
    private boolean isAdvertisement;

    public AdapterMediaAddPost(Context context, FragmentActivity activity, boolean isAdvertisement) {
        this.context = context;
        this.activity = activity;
        this.isAdvertisement = isAdvertisement;
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
        if (SelectedMedia.getProductImageList() == null) {
            return 0;
        }
        return SelectedMedia.getProductImageList().size() + 1;
    }

    public class CharImageHolder extends RecyclerView.ViewHolder {
        private final ItemMediaAddPostBinding b;

        public CharImageHolder(@NonNull ItemMediaAddPostBinding itemView) {
            super(itemView.getRoot());
            this.b = itemView;
        }

        public void bind() {
            if (!isAdvertisement)
                b.image.setLayoutParams(new FrameLayout.LayoutParams(MATCH_PARENT, getWindowWidth(activity) * 3 / 7));
            else
                b.image.setLayoutParams(new FrameLayout.LayoutParams(MATCH_PARENT, (getWindowWidth(activity)-78) / 3));
            setMargins(b.image, dpToPx(5, context), dpToPx(5, context), dpToPx(5, context), dpToPx(5, context));
            if (getAdapterPosition() == getItemCount() - 1) {
                if (isHasVideo) {
                    b.getRoot().setVisibility(View.GONE);
                    return;
                }

                b.clear.setVisibility(View.GONE);
                b.layAdd.setVisibility(View.VISIBLE);
                b.image.setImageResource(R.color.on_bg_ls);

            } else {
                b.clear.setVisibility(View.VISIBLE);
                b.layAdd.setVisibility(View.GONE);

                if (SelectedMedia.getProductImageList().get(getAdapterPosition()).getType() == 3) {
                    b.isVideo.setVisibility(View.VISIBLE);
                    isHasVideo = true;
                } else b.isVideo.setVisibility(View.GONE);


                Glide.with(context)
                        .load(SelectedMedia.getProductImageList().get(getAdapterPosition()).getPath())
                        .placeholder(R.color.on_bg_ls)
                        .into(b.image);
            }

            b.click.setOnClickListener(v -> {
                if (getAdapterPosition() == getItemCount() - 1) {
                    if (!isAdvertisement)
                        showDialog();
                    else
                        FragmentHelper.addFragment(Const.mainFragmentManager, R.id.fragment_container_main, FragmentOpenGallery.newInstance(0, FragmentOpenGallery.IMAGE));

                }
            });

            b.clear.setOnClickListener(v -> removeAt(getAdapterPosition()));
        }
    }

    private void showDialog() {
        android.app.Dialog dialog = new android.app.Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheet_choose_media);
        LinearLayout root = dialog.findViewById(R.id.root);
        setPadding(root, 0, 0, 0, navigationBarHeight);

        if (SelectedMedia.getProductImageList().size() > 0) {
            dialog.findViewById(R.id.video).setVisibility(View.GONE);
        } else dialog.findViewById(R.id.video).setVisibility(View.VISIBLE);

        dialog.findViewById(R.id.video).setOnClickListener(v -> {
            dialog.dismiss();
            FragmentHelper.addFragment(Const.mainFragmentManager, R.id.fragment_container_main, FragmentOpenGallery.newInstance(1, FragmentOpenGallery.VIDEO));
        });

        dialog.findViewById(R.id.image).setOnClickListener(v -> {
            dialog.dismiss();
            FragmentHelper.addFragment(Const.mainFragmentManager, R.id.fragment_container_main, FragmentOpenGallery.newInstance(0, FragmentOpenGallery.IMAGE));
        });

        dialog.getWindow().setLayout(MATCH_PARENT, WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnim;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();
    }

    private void removeAt(int position) {
        try {
            if (SelectedMedia.getProductImageList().get(position).getType() == 3) {
                isHasVideo = false;
                notifyItemChanged(getItemCount() - 1);
            }
            SelectedMedia.getProductImageList().remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, SelectedMedia.getProductImageList().size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static AdapterMediaAddPost getInstance() {
        return instance;
    }
}
