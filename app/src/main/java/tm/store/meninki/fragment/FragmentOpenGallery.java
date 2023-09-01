package tm.store.meninki.fragment;

import static androidx.core.content.ContextCompat.checkSelfPermission;
import static tm.store.meninki.utils.StaticMethods.hideSoftKeyboard;
import static tm.store.meninki.utils.StaticMethods.navigationBarHeight;
import static tm.store.meninki.utils.StaticMethods.setBackgroundDrawable;
import static tm.store.meninki.utils.StaticMethods.statusBarHeight;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.gowtham.library.utils.LogMessage;
import com.gowtham.library.utils.TrimVideo;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrInterface;
import com.r0adkll.slidr.model.SlidrPosition;

import java.util.ArrayList;

import tm.store.meninki.R;
import tm.store.meninki.adapter.AdapterCharImage;
import tm.store.meninki.adapter.AdapterMedia;
import tm.store.meninki.adapter.AdapterMediaAddPost;
import tm.store.meninki.data.MediaLocal;
import tm.store.meninki.data.SelectedMedia;
import tm.store.meninki.databinding.FragmentOpenGalleryBinding;
import tm.store.meninki.interfaces.OnBackPressedFragment;
import tm.store.meninki.utils.StaticMethods;

public class FragmentOpenGallery extends Fragment implements OnBackPressedFragment {
    public static final int IMAGE_OPTION = 3;
    private FragmentOpenGalleryBinding b;
    private ArrayList<MediaLocal> media = new ArrayList<>();
    private String TAG = "Media";
    private AdapterMedia adapter;
    private int chooseCount;
    private SlidrInterface slidrInterface;
    public static final int VIDEO = 1;
    public static final int IMAGE = 2;
    private int isVideo;
    private int PERMISSION_FILE = 1;
    private ActivityResultLauncher<Intent> startForResult;

    public static FragmentOpenGallery newInstance(int chooseCount, int isVideo) {
        Bundle args = new Bundle();
        args.putInt("choose_count", chooseCount);
        args.putInt("is_video", isVideo);
        FragmentOpenGallery fragment = new FragmentOpenGallery();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (slidrInterface == null && getView() != null)
            slidrInterface = Slidr.replace(getView().findViewById(R.id.slider_layout), new SlidrConfig.Builder().position(SlidrPosition.LEFT).build());

        StaticMethods.setPadding(b.mainGalleryLay, 0, statusBarHeight, 0, navigationBarHeight);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            chooseCount = getArguments().getInt("choose_count");
            isVideo = getArguments().getInt("is_video");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentOpenGalleryBinding.inflate(inflater, container, false);

        checkUI();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkPermissionWriteExternalStorageApi33()) {
                AsyncTask.execute(this::getMediaFromGallery);
            }
        } else {
            if (checkPermissionWriteExternalStorage()) {
                AsyncTask.execute(this::getMediaFromGallery);
            }
        }

        initListener();

        return b.getRoot();
    }

    private void checkUI() {
        if (chooseCount == 1 && isVideo != VIDEO) {
            b.appBarLayout.setVisibility(View.VISIBLE);
        } else b.appBarLayout.setVisibility(View.GONE);

    }


    boolean checkPermissionWriteExternalStorage() {
        if (checkSelfPermission(requireActivity().getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED /*||
                checkSelfPermission(requireActivity().getApplicationContext(), Manifest.permission.READ_MEDIA_VIDEO) == PackageManager.PERMISSION_DENIED*/) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE/*, Manifest.permission.READ_MEDIA_VIDEO*/}, PERMISSION_FILE);
            return false;
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    boolean checkPermissionWriteExternalStorageApi33() {
        if (checkSelfPermission(requireActivity().getApplicationContext(), Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_DENIED || checkSelfPermission(requireActivity().getApplicationContext(), Manifest.permission.READ_MEDIA_VIDEO) == PackageManager.PERMISSION_DENIED) {
            requestPermissions(new String[]{Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO}, PERMISSION_FILE);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == PERMISSION_FILE) {
                AsyncTask.execute(this::getMediaFromGallery);
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void initListener() {
        b.toolbar.setVisibility(View.VISIBLE);
        b.btnBack.setOnClickListener(view -> getActivity().onBackPressed());
        setBackgroundDrawable(getContext(), b.edtTitle, R.color.white, 0, 10, false, 0);

        startForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (getActivity() == null) return;

            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                Uri uri = Uri.parse(TrimVideo.getTrimmedVideoPath(result.getData()));

                if (AdapterMediaAddPost.getInstance() != null) {
                    SelectedMedia.getProductImageList().add(new MediaLocal(-1, uri.getPath(), 3));
                    AdapterMediaAddPost.getInstance().notifyDataSetChanged();
                }

            } else LogMessage.v("videoTrimResultLauncher data is null");
        });
        b.btnNext.setOnClickListener(v -> {
            b.btnNext.setEnabled(false);

            if (chooseCount == 1) {
                if (media.size() == 0 || b.edtTitle.getText().toString().trim().length() == 0) {
                    Toast.makeText(getContext(), getActivity().getResources().getString(R.string.your_image_or_text_is_empty), Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(() -> b.btnNext.setEnabled(true), 100);
                    return;
                }

            } else {

                if (AdapterMediaAddPost.getInstance() != null && isVideo != IMAGE_OPTION) {
                    SelectedMedia.getProductImageList().addAll(media);
                    AdapterMediaAddPost.getInstance().notifyDataSetChanged();
                }

                if (AdapterCharImage.getInstance() != null) {
                    AdapterCharImage.getInstance().insertOption(media);
                }
            }

            new Handler().postDelayed(() -> b.btnNext.setEnabled(true), 200);

            hideSoftKeyboard(getActivity());
            getActivity().onBackPressed();

        });
    }

    private void setRecycler(Cursor cursor) {
        adapter = new AdapterMedia(getActivity(), getContext(), cursor, b.laySelectionMode, b.countSelection, media, isVideo, startForResult, chooseCount);
        b.recGallery.setLayoutManager(new GridLayoutManager(getContext(), 3));
        b.recGallery.setAdapter(adapter);
    }

    private void getMediaFromGallery() {
        String[] columns = {MediaStore.Files.FileColumns._ID, MediaStore.Files.FileColumns.DATA, MediaStore.Files.FileColumns.DATE_ADDED, MediaStore.Files.FileColumns.MEDIA_TYPE, MediaStore.Files.FileColumns.MIME_TYPE, MediaStore.Files.FileColumns.TITLE,};

        String selection;

        switch (isVideo) {
            case VIDEO:
                selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "=" + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;
                break;
            case IMAGE:
            case IMAGE_OPTION:
                selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "=" + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
                break;
            default:
                selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "=" + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE + " OR " + MediaStore.Files.FileColumns.MEDIA_TYPE + "=" + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;
                break;
        }


        Uri queryUri = MediaStore.Files.getContentUri("external");

        @SuppressWarnings("deprecation")
        Cursor imagecursor = getActivity().managedQuery(queryUri, columns, selection, null, // Selection args (none).
                MediaStore.Files.FileColumns.DATE_ADDED + " DESC" // Sort order.
        );
        getActivity().runOnUiThread(() -> setRecycler(imagecursor));
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}