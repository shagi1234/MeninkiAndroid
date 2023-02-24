
package tm.store.meninki.fragment;

import static androidx.core.content.ContextCompat.checkSelfPermission;
import static tm.store.meninki.utils.StaticMethods.hideSoftKeyboard;
import static tm.store.meninki.utils.StaticMethods.navigationBarHeight;
import static tm.store.meninki.utils.StaticMethods.setBackgroundDrawable;
import static tm.store.meninki.utils.StaticMethods.setPadding;
import static tm.store.meninki.utils.StaticMethods.statusBarHeight;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import tm.store.meninki.R;
import tm.store.meninki.adapter.AdapterCharImage;
import tm.store.meninki.adapter.AdapterMedia;
import tm.store.meninki.adapter.AdapterMediaAddPost;
import tm.store.meninki.data.MediaLocal;
import tm.store.meninki.data.ProductImageDto;
import tm.store.meninki.data.SelectedMedia;
import tm.store.meninki.databinding.FragmentOpenGalleryBinding;
import tm.store.meninki.interfaces.OnBackPressedFragment;
import tm.store.meninki.utils.Lists;
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
    private int chooseCount;
    private SlidrInterface slidrInterface;
    private int PERMISSION_FILE = 1;

    public static FragmentOpenGallery newInstance(int chooseCount) {
        Bundle args = new Bundle();
        args.putInt("choose_count", chooseCount);
        FragmentOpenGallery fragment = new FragmentOpenGallery();
        fragment.setArguments(args);
        return fragment;
    }

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
        if (getArguments() != null) {
            chooseCount = getArguments().getInt("choose_count");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentOpenGalleryBinding.inflate(inflater, container, false);
        if (chooseCount == 1) {
            b.appBarLayout.setVisibility(View.VISIBLE);
        } else b.appBarLayout.setVisibility(View.GONE);

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
        setBackgroundDrawable(getContext(), b.edtTitle, R.color.white, 0, 10, false, 0);

        b.btnNext.setOnClickListener(v -> {
            b.btnNext.setEnabled(false);
            if (chooseCount == 1) {
                if (media.size() == 0 || b.edtTitle.getText().toString().trim().length() == 0) {
                    Toast.makeText(getContext(), "Your image or text is empty", Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(() -> b.btnNext.setEnabled(true), 100);
                    return;
                }
                if (AdapterCharImage.getInstance() != null) {
                    Lists.getS().add(new ProductImageDto(media.get(0).getPath(), b.edtTitle.getText().toString()));
                    AdapterCharImage.getInstance().notifyDataSetChanged();
                }


            } else {
                if (AdapterMediaAddPost.getInstance() != null) {
                    SelectedMedia.getArrayList().addAll(media);
                    AdapterMediaAddPost.getInstance().notifyDataSetChanged();
                }
            }

            new Handler().postDelayed(() -> b.btnNext.setEnabled(true), 200);

            hideSoftKeyboard(getActivity());
            getActivity().onBackPressed();

        });
    }

    private void setRecycler(Cursor cursor) {
        adapter = new AdapterMedia(getActivity(), getContext(), cursor, b.laySelectionMode, b.countSelection, media, chooseCount);
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