package tm.store.meninki.adapter;

import static tm.store.meninki.fragment.FragmentOpenGallery.VIDEO;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMetadataRetriever;
import android.media.MediaMuxer;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gowtham.library.utils.TrimVideo;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import tm.store.meninki.R;
import tm.store.meninki.data.MediaLocal;
import tm.store.meninki.utils.StaticMethods;

public class AdapterMedia extends RecyclerView.Adapter<AdapterMedia.ViewHolder> {
    private Context context;
    private Activity activity;
    private Cursor mediaCursor;
    private LinearLayout laySelectionMode;
    private final String TAG = "MediaAdapter";
    private ArrayList<MediaLocal> selectedMediaPath;
    private ArrayList<MediaLocal> medias = new ArrayList<>();
    private TextView countSelection;
    private int chooseCount;
    private int isVideo;
    private ActivityResultLauncher<Intent> startForResult;

    public AdapterMedia(
            Activity activity,
            Context context,
            Cursor mediaPath,
            LinearLayout laySelectionMode,
            TextView countSelection,
            ArrayList<MediaLocal> selectedMediaPath,
            int isVideo,
            ActivityResultLauncher<Intent> startForResult,
            int chooseCount) {

        this.activity = activity;
        this.context = context;
        this.mediaCursor = mediaPath;
        this.laySelectionMode = laySelectionMode;
        this.countSelection = countSelection;
        this.selectedMediaPath = selectedMediaPath;
        this.chooseCount = chooseCount;
        this.isVideo = isVideo;
        this.startForResult = startForResult;
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

        Glide.with(context)
                .load(medias.get(holder.getAdapterPosition()).getPath())
                .into(holder.imageView);

        if (selectedMediaPath.contains(medias.get(holder.getAdapterPosition()))) {
            holder.layCheckbox.setVisibility(View.VISIBLE);
        } else {
            holder.layCheckbox.setVisibility(View.GONE);
        }

        holder.click.setOnClickListener(v -> {
            MediaLocal media1 = medias.get(holder.getAdapterPosition());

            if (isVideo == VIDEO && chooseCount == 1) {
                TrimVideo.activity(media1.getPath())
                        .start(activity, startForResult);
                return;
            }

            if (!selectedMediaPath.contains(media1)) {

                if (selectedMediaPath.size() == chooseCount && chooseCount != 0) {
                    Toast.makeText(context, context.getResources().getString(R.string.you_cant_select_image_more_1), Toast.LENGTH_SHORT).show();
                    return;
                }

                selectedMediaPath.add(media1);
            } else {
                selectedMediaPath.remove(media1);
            }

            if (selectedMediaPath.size() != 0) {
                laySelectionMode.setVisibility(View.VISIBLE);
                countSelection.setText(String.valueOf(selectedMediaPath.size()));
            } else {
                laySelectionMode.setVisibility(View.GONE);
            }

            notifyItemChanged(holder.getAdapterPosition());
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

        return new MediaLocal(-1, mediaCursor.getString(dataColumnIndex), typeMedia);
    }

    @Override
    public int getItemCount() {
        return mediaCursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private FrameLayout click;
        private ConstraintLayout constraintLayout;
        private FrameLayout layCheckbox;
        private ImageView icCheckbox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            constraintLayout = itemView.findViewById(R.id.container_media);
            click = itemView.findViewById(R.id.click);
            icCheckbox = itemView.findViewById(R.id.ic_checkbox);
            layCheckbox = itemView.findViewById(R.id.lay_checkbox);

            constraintLayout.setLayoutParams(new ViewGroup.LayoutParams(StaticMethods.getWindowWidth(activity) / 3, StaticMethods.getWindowWidth(activity) / 3));
        }
    }

    @SuppressLint("WrongConstant")
    public void trimVideo(String videoPath, long startMs, long endMs) {
        try {
            // Create a media retriever instance
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(videoPath);

            // Extract video information
            String videoMimeType = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE);
            int videoWidth = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
            int videoHeight = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
            int rotation = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION));

            // Create a media extractor instance
            MediaExtractor extractor = new MediaExtractor();
            extractor.setDataSource(videoPath);

            // Find the video track index
            int videoTrackIndex = -1;
            for (int i = 0; i < extractor.getTrackCount(); i++) {
                MediaFormat format = extractor.getTrackFormat(i);
                String mimeType = format.getString(MediaFormat.KEY_MIME);
                if (mimeType != null && mimeType.startsWith("video/")) {
                    videoTrackIndex = i;
                    break;
                }
            }

            // Set the track index and configure the muxer
            extractor.selectTrack(videoTrackIndex);
            MediaFormat trackFormat = extractor.getTrackFormat(videoTrackIndex);
            MediaMuxer muxer = new MediaMuxer(Environment.getExternalStorageDirectory() + "/trimmed_video.mp4", MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
            int outputVideoTrackIndex = muxer.addTrack(trackFormat);
            muxer.start();

            // Set the start and end timestamps for trimming
            long durationUs = endMs * 1000 - startMs * 1000;
            extractor.seekTo(startMs * 1000, MediaExtractor.SEEK_TO_CLOSEST_SYNC);

            // Read and write video data
            ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024);
            MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
            while (true) {
                int sampleSize = extractor.readSampleData(buffer, 0);
                if (sampleSize < 0) {
                    break;
                }

                long presentationTimeUs = extractor.getSampleTime();
                if (presentationTimeUs > endMs * 1000) {
                    break;
                }

                info.offset = 0;
                info.size = sampleSize;
                info.presentationTimeUs = presentationTimeUs - startMs * 1000;
                info.flags = extractor.getSampleFlags();

                muxer.writeSampleData(outputVideoTrackIndex, buffer, info);

                extractor.advance();
            }

            // Release resources
            muxer.stop();
            muxer.release();
            extractor.release();
            retriever.release();

            // Video trimming is complete
            // The trimmed video can be found at Environment.getExternalStorageDirectory() + "/trimmed_video.mp4"
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}