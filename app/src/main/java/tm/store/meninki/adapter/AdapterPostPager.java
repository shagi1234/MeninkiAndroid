/*
 * Created by Shahruh Daniyarow on 3/30/23, 10:21 PM
 *     s.daniyarov@salam.tm
 *     Last modified 3/30/23, 10:19 PM
 *     Copyright (c) 2023.
 *     All rights reserved.
 */

package tm.store.meninki.adapter;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static tm.store.meninki.api.Network.BASE_URL;
import static tm.store.meninki.utils.StaticMethods.navigationBarHeight;
import static tm.store.meninki.utils.StaticMethods.setPadding;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tm.store.meninki.R;
import tm.store.meninki.api.data.ResponsePostGetAllItem;
import tm.store.meninki.databinding.ItemVideoBinding;
import tm.store.meninki.utils.StaticMethods;

public class AdapterPostPager extends RecyclerView.Adapter<AdapterPostPager.VideoHolder> {
    private ArrayList<ResponsePostGetAllItem> videos;
    private Context context;
    private ViewPager2 viewPager2;
    private int position;
    public static SimpleExoPlayer lastExoPlayer;
    private Integer lastPosition = null;
    private FragmentActivity activity;
    private ImageView lastThumb;

    public AdapterPostPager(Context context, FragmentActivity activity, ArrayList<ResponsePostGetAllItem> videos, ViewPager2 viewPager2) {
        this.videos = videos;
        this.context = context;
        this.activity = activity;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public AdapterPostPager.VideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemVideoBinding binding = ItemVideoBinding.inflate(layoutInflater, parent, false);

        return new AdapterPostPager.VideoHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterPostPager.VideoHolder holder, int position) {

        if (videos.get(holder.getAdapterPosition()).getMedias().get(0).getMediaType() == 1) {
            holder.bindVideo();
        } else {
            holder.bindImages();
        }

    }

    @Override
    public int getItemCount() {
        if (videos == null) {
            return 0;
        }
        return videos.size();
    }

    public void setPosition(int position) {
        this.position = position;
        notifyItemChanged(position);
    }

    public void releasePlayer() {
        if (lastThumb != null)
            lastThumb.setVisibility(View.VISIBLE);

        if (lastExoPlayer != null) {
            lastExoPlayer.stop();
            lastExoPlayer.release();
            lastExoPlayer = null;
        }
    }

    public void pause() {
        if (lastExoPlayer != null) {
            lastExoPlayer.pause();
        }
    }

    public void play() {
        if (lastExoPlayer != null) {
            lastExoPlayer.play();
        }
    }

    public class VideoHolder extends RecyclerView.ViewHolder {
        public final ItemVideoBinding b;
        private SimpleExoPlayer exoPlayer;
        private float scaleFactor = 1.0f;

        public VideoHolder(@NonNull ItemVideoBinding itemView) {
            super(itemView.getRoot());
            this.b = itemView;
        }

        public void bindVideo() {
            b.playerView.setVisibility(View.VISIBLE);
            b.image.setVisibility(View.VISIBLE);
            b.imagePager.setVisibility(View.GONE);
            b.indicator.setVisibility(View.GONE);

            DefaultLoadControl loadControl = new DefaultLoadControl.Builder()
                    .setBufferDurationsMs(DefaultLoadControl.DEFAULT_MIN_BUFFER_MS,
                            100_000,
                            DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_MS,
                            DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS)
                    .build();
            exoPlayer = new SimpleExoPlayer.Builder(context).setLoadControl(loadControl).build();

//            if (!videos.get(getAdapterPosition()).isLiked()) {
//                b.icLike.setImageResource(R.drawable.ic_favorite_border);
//            } else b.icLike.setImageResource(R.drawable.ic_favorite);

            Glide.with(context)
                    .load(BASE_URL + "/" + videos.get(getAdapterPosition()).getMedias().get(0).getPreview())
                    .placeholder(R.color.bg_post)
                    .into(b.image);

            if (getAdapterPosition() == position) playFilm(exoPlayer);

            b.username.setText(videos.get(getAdapterPosition()).getUser().getName());
            b.desc.setText(videos.get(getAdapterPosition()).getDescription());
            b.desc2.setText(videos.get(getAdapterPosition()).getUser().getDescription());

            Glide.with(context)
                    .load(BASE_URL + "/" + videos.get(getAdapterPosition()).getUser().getImgPath())
                    .placeholder(R.color.low_contrast)
                    .into(b.imgProfile);

            b.backBtn.setOnClickListener(v -> activity.onBackPressed());
            b.like.setOnClickListener(v -> like(getAdapterPosition()));
            b.comment.setOnClickListener(v -> comment());
            b.options.setOnClickListener(v -> showDialog());

        }

        public void bindImages() {
            b.playerView.setVisibility(View.GONE);
            b.image.setVisibility(View.GONE);
            b.imagePager.setVisibility(View.VISIBLE);
            b.indicator.setVisibility(View.VISIBLE);

//            if (!videos.get(getAdapterPosition()).isLiked()) {
//                b.icLike.setImageResource(R.drawable.ic_favorite_border);
//            } else b.icLike.setImageResource(R.drawable.ic_favorite);

            setImagePager();

            b.username.setText(videos.get(getAdapterPosition()).getUser().getName());
            b.desc.setText(videos.get(getAdapterPosition()).getDescription());
            b.desc2.setText(videos.get(getAdapterPosition()).getUser().getDescription());

            Glide.with(context)
                    .load(BASE_URL + "/" + videos.get(getAdapterPosition()).getUser().getImgPath())
                    .placeholder(R.color.low_contrast)
                    .into(b.imgProfile);

            b.backBtn.setOnClickListener(v -> activity.onBackPressed());
            b.like.setOnClickListener(v -> like(getAdapterPosition()));
            b.comment.setOnClickListener(v -> comment());
            b.options.setOnClickListener(v -> showDialog());

        }

        private void showDialog() {
            android.app.Dialog dialog = new android.app.Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.bottom_sheet_story_options);
            LinearLayout root = dialog.findViewById(R.id.root);
            setPadding(root, 0, 0, 0, navigationBarHeight);

            dialog.getWindow().setLayout(MATCH_PARENT, WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnim;
            dialog.getWindow().setGravity(Gravity.BOTTOM);
            dialog.show();
        }

        private void comment() {

        }

        private void like(int adapterPosition) {
            changeUILike();
            Call<Boolean> call;

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("uuid", videos.get(getAdapterPosition()).getId());

            if (getAdapterPosition() == 0) {
                call = StaticMethods.getApiHome().like(jsonObject);
            } else
                call = StaticMethods.getApiHome().dislike(videos.get(getAdapterPosition()).getId());

            call.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    changeData(adapterPosition);
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void changeData(int adapterPosition) {

        }

        private void changeUILike() {

        }

        private void setImagePager() {
            AdapterVerticalImagePager adapterVerticalImagePager = new AdapterVerticalImagePager(context);
            adapterVerticalImagePager.setImageList(new ArrayList<>(videos.get(getAdapterPosition()).getMedias()));
            b.imagePager.setClipToPadding(false);
            b.imagePager.setClipChildren(false);
            b.imagePager.setOffscreenPageLimit(3);
            b.imagePager.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
            b.indicator.setVisibility(View.VISIBLE);
            b.indicator.setViewPager(b.imagePager);
            b.imagePager.setAdapter(adapterVerticalImagePager);

        }

        private void playFilm(SimpleExoPlayer exoPlayer) {

            try {
                b.playerView.setPlayer(this.exoPlayer);
                exoPlayer.setMediaItem(new MediaItem.Builder().setUri(BASE_URL + Uri.parse(videos.get(getAdapterPosition()).getMedias().get(0).getPath())).build());
                exoPlayer.setPlayWhenReady(true);
                exoPlayer.prepare();

                exoPlayer.addListener(new Player.Listener() {
                    @SuppressWarnings("deprecation")
                    @Override
                    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                        Player.Listener.super.onPlayerStateChanged(playWhenReady, playbackState);
                        setProgress(playbackState == ExoPlayer.STATE_IDLE);

                        if (playbackState == ExoPlayer.STATE_ENDED) {
                            exoPlayer.seekTo(0);
                            exoPlayer.play();
                        }
                    }
                });

                exoPlayer.addListener(new Player.Listener() {
                    @Override
                    public void onRenderedFirstFrame() {
                        Player.Listener.super.onRenderedFirstFrame();
                        b.image.setVisibility(View.GONE);
                        b.playerView.setVisibility(View.VISIBLE);
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
            lastExoPlayer = exoPlayer;
            lastThumb = b.image;
        }

        private void setProgress(boolean isProgress) {
            ProgressBar lottieAnimationView = b.playerView.findViewById(R.id.loading_linear);
            if (isProgress) {
                lottieAnimationView.setVisibility(View.VISIBLE);
                b.playerView.findViewById(R.id.exo_progress).setVisibility(View.GONE);
            } else {
                lottieAnimationView.setVisibility(View.GONE);
                b.playerView.findViewById(R.id.exo_progress).setVisibility(View.VISIBLE);
            }
        }
    }

}
