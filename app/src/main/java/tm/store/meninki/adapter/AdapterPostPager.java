/*
 * Created by Shahruh Daniyarow on 3/30/23, 10:21 PM
 *     s.daniyarov@salam.tm
 *     Last modified 3/30/23, 10:19 PM
 *     Copyright (c) 2023.
 *     All rights reserved.
 */

package tm.store.meninki.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;

import java.util.ArrayList;

import tm.store.meninki.R;
import tm.store.meninki.data.VideoDto;
import tm.store.meninki.databinding.ItemVideoBinding;

public class AdapterPostPager extends RecyclerView.Adapter<AdapterPostPager.VideoHolder> {
    private ArrayList<VideoDto> videos;
    private Context context;
    private ViewPager2 viewPager2;
    private int position;
    public static SimpleExoPlayer lastExoPlayer;
    private Integer lastPosition = null;
    private FragmentActivity activity;

    public AdapterPostPager(Context context, FragmentActivity activity, ArrayList<VideoDto> videos, ViewPager2 viewPager2) {
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
        holder.bind();
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
        if (lastPosition == null) return;
        notifyItemChanged(lastPosition);
    }

    public void releasePlayer() {
        if (lastExoPlayer != null) {
            lastExoPlayer.stop();
            lastExoPlayer.release();
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

        public void bind() {
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

            if (getAdapterPosition() == position) {
                playFilm(exoPlayer);
            }
//            b.animationView.addAnimatorListener(new Animator.AnimatorListener() {
//                @Override
//                public void onAnimationStart(@NonNull Animator animation) {
//                }
//
//                @Override
//                public void onAnimationEnd(@NonNull Animator animation) {
//                    b.animationView.clearAnimation();
//                    b.animationView.setVisibility(View.GONE);
//                }
//
//                @Override
//                public void onAnimationCancel(@NonNull Animator animation) {
//                }
//
//                @Override
//                public void onAnimationRepeat(@NonNull Animator animation) {
//
//                }
//            });

//            b.clickLike.setOnClickListener(v -> {
//                if (videos.get(getAdapterPosition()).isLiked()) {
//                    b.icLike.setImageResource(R.drawable.ic_favorite_border);
//                } else {
//                    b.animationView.setVisibility(View.VISIBLE);
//                    b.animationView.setAnimation("like.json");
//                    b.animationView.loop(false);
//                    b.animationView.playAnimation();
//                    b.icLike.setImageResource(R.drawable.ic_favorite);
//                }
//
//                videos.get(getAdapterPosition()).setLiked(!videos.get(getAdapterPosition()).isLiked());
//            });

//            b.bgAdd.setOnClickListener(v -> {
//                // add shorts
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                activity.startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
//            });

        }

        private void playFilm(SimpleExoPlayer exoPlayer) {

            try {
                b.playerView.setPlayer(this.exoPlayer);
                exoPlayer.setMediaItem(new MediaItem.Builder().setUri(Uri.parse(videos.get(getAdapterPosition()).getPath())).build());
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

            } catch (Exception e) {
                e.printStackTrace();
            }
            lastExoPlayer = exoPlayer;
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
