package com.example.playerslidding.adapter;

import static com.example.playerslidding.utils.StaticMethods.getWindowWidth;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.playerslidding.R;
import com.example.playerslidding.data.MediaLocal;

import java.util.ArrayList;

public class AdapterMedia extends RecyclerView.Adapter<AdapterMedia.ViewHolder> {
    private Context context;
    private Activity activity;
    private Cursor mediaCursor;
    private LinearLayout laySelectionMode;
    private final String TAG = "MediaAdapter";
    private ArrayList<MediaLocal> selectedMediaPath;
    private ArrayList<MediaLocal> medias = new ArrayList<>();
    private TextView countSelection;

    public  AdapterMedia(Activity activity, Context context, Cursor mediaPath, LinearLayout laySelectionMode, TextView countSelection, ArrayList<MediaLocal> selectedMediaPath) {
        this.activity = activity;
        this.context = context;
        this.mediaCursor = mediaPath;
        this.laySelectionMode = laySelectionMode;
        this.countSelection = countSelection;
        this.selectedMediaPath = selectedMediaPath;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_media, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        MediaLocal media = getCurrentMedia(holder.getAdapterPosition());

        if (!medias.contains(media)) {
            medias.add(media);
        }

        if (!medias.contains(medias.get(holder.getAdapterPosition()))) {
            medias.add(medias.get(holder.getAdapterPosition()));
        }

        Glide.with(context)
                .load(medias.get(holder.getAdapterPosition()).getPath())
                .into(holder.imageView);

        if (selectedMediaPath.contains(medias.get(holder.getAdapterPosition()))) {
            holder.layCheckbox.setVisibility(View.VISIBLE);
        } else {
            holder.layCheckbox.setVisibility(View.GONE);
        }

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaLocal media = medias.get(holder.getAdapterPosition());

                if (!selectedMediaPath.contains(media)) {
                    selectedMediaPath.add(media);
                } else {
                    selectedMediaPath.remove(media);
                }

                if (selectedMediaPath.size() != 0) {
                    laySelectionMode.setVisibility(View.VISIBLE);
                    countSelection.setText(String.valueOf(selectedMediaPath.size()));
                } else {
                    laySelectionMode.setVisibility(View.GONE);
                }

                notifyItemChanged(holder.getAdapterPosition());
            }
        });

    }

    private MediaLocal getCurrentMedia(int adapterPosition) {

        mediaCursor.moveToPosition(adapterPosition);

        int dataColumnIndex = mediaCursor.getColumnIndex(MediaStore.Files.FileColumns.DATA);

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();

        bmOptions.inSampleSize = 4;

        bmOptions.inPurgeable = true;
        int type = mediaCursor.getColumnIndex(MediaStore.Files.FileColumns.MEDIA_TYPE);

        int typeMedia = mediaCursor.getInt(type);

        return new MediaLocal(-1,mediaCursor.getString(dataColumnIndex),typeMedia);
    }

    @Override
    public int getItemCount() {
        return mediaCursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private FrameLayout grayContainer;
        private ConstraintLayout constraintLayout;
        private FrameLayout layCheckbox;
        private ImageView icCheckbox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            constraintLayout = itemView.findViewById(R.id.container_media);
            grayContainer = itemView.findViewById(R.id.gray_container);
            icCheckbox = itemView.findViewById(R.id.ic_checkbox);
            layCheckbox = itemView.findViewById(R.id.lay_checkbox);

            constraintLayout.setLayoutParams(new ViewGroup.LayoutParams(getWindowWidth(activity) / 3, getWindowWidth(activity) / 3));
        }
    }
}