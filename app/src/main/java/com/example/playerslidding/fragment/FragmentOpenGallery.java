
package com.example.playerslidding.fragment;

import static androidx.core.content.ContextCompat.checkSelfPermission;
import static com.example.playerslidding.utils.StaticMethods.navigationBarHeight;
import static com.example.playerslidding.utils.StaticMethods.setPadding;
import static com.example.playerslidding.utils.StaticMethods.statusBarHeight;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.playerslidding.R;
import com.example.playerslidding.adapter.AdapterMedia;
import com.example.playerslidding.adapter.AdapterMediaAddPost;
import com.example.playerslidding.data.MediaLocal;
import com.example.playerslidding.data.SelectedMedia;
import com.example.playerslidding.databinding.FragmentOpenGalleryBinding;
import com.example.playerslidding.interfaces.OnBackPressedFragment;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrInterface;
import com.r0adkll.slidr.model.SlidrPosition;

import java.util.ArrayList;

public class FragmentOpenGallery extends Fragment implements OnBackPressedFragment {
    private FragmentOpenGalleryBinding b;
    private ArrayList<MediaLocal> media = new ArrayList<>();
    private String TAG = "Media";
    private AdapterMedia adapter;
    private SlidrInterface slidrInterface;
    private int PERMISSION_FILE = 1;

    @Override
    public void onResume() {
        super.onResume();
        if (slidrInterface == null && getView() != null)
            slidrInterface = Slidr.replace(getView().findViewById(R.id.slider_layout), new SlidrConfig.Builder().position(SlidrPosition.LEFT).build());

        setPadding(b.mainGalleryLay, 0, statusBarHeight, 0, navigationBarHeight);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentOpenGalleryBinding.inflate(inflater, container, false);
        if (checkPermissionWriteExternalStorage()) {
            AsyncTask.execute(this::getMediaFromGallery);
        }
        initListener();
        return b.getRoot();
    }

    boolean checkPermissionWriteExternalStorage() {
        if (checkSelfPermission(requireActivity().getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_FILE);
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

    private void initListener() {
        b.toolbar.setVisibility(View.VISIBLE);
        b.btnBack.setOnClickListener(view -> getActivity().onBackPressed());

        b.btnNext.setOnClickListener(v -> {
            b.btnNext.setEnabled(false);
            if (AdapterMediaAddPost.getInstance() != null) {
                SelectedMedia.getArrayList().addAll(media);
                AdapterMediaAddPost.getInstance().notifyDataSetChanged();
            }
            new Handler().postDelayed(() -> b.btnNext.setEnabled(true), 200);
            getActivity().onBackPressed();
        });
    }

    private void setRecycler(Cursor cursor) {
        adapter = new AdapterMedia(getActivity(), getContext(), cursor, b.laySelectionMode, b.countSelection, media);
        b.recGallery.setLayoutManager(new GridLayoutManager(getContext(), 3));
        b.recGallery.setAdapter(adapter);
    }

    private void getMediaFromGallery() {
        String[] columns = {
                MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.DATE_ADDED,
                MediaStore.Files.FileColumns.MEDIA_TYPE,
                MediaStore.Files.FileColumns.MIME_TYPE,
                MediaStore.Files.FileColumns.TITLE,
        };

        String selection;

        selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
                + " OR "
                + MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

        Uri queryUri = MediaStore.Files.getContentUri("external");

        @SuppressWarnings("deprecation")
        Cursor imagecursor = getActivity().managedQuery(queryUri,
                columns,
                selection,
                null, // Selection args (none).
                MediaStore.Files.FileColumns.DATE_ADDED + " DESC" // Sort order.
        );
        getActivity().runOnUiThread(() -> setRecycler(imagecursor));
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}