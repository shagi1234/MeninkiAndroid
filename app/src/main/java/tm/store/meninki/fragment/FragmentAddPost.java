package tm.store.meninki.fragment;

import static tm.store.meninki.utils.StaticMethods.getApiHome;
import static tm.store.meninki.utils.StaticMethods.navigationBarHeight;
import static tm.store.meninki.utils.StaticMethods.setBackgroundDrawable;
import static tm.store.meninki.utils.StaticMethods.statusBarHeight;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tm.store.meninki.R;
import tm.store.meninki.adapter.AdapterMediaAddPost;
import tm.store.meninki.api.enums.Image;
import tm.store.meninki.api.request.RequestAddPost;
import tm.store.meninki.api.request.RequestUploadImage;
import tm.store.meninki.data.MediaLocal;
import tm.store.meninki.data.SelectedMedia;
import tm.store.meninki.databinding.FragmentAddPostBinding;
import tm.store.meninki.interfaces.OnBackPressedFragment;
import tm.store.meninki.utils.FileUtil;
import tm.store.meninki.utils.StaticMethods;

public class FragmentAddPost extends Fragment implements OnBackPressedFragment {
    private FragmentAddPostBinding b;
    private AdapterMediaAddPost mediaAddPost;
    private String prodId;
    private String postId;
    private int i = 0;

    public static FragmentAddPost newInstance(String prodId) {
        FragmentAddPost fragment = new FragmentAddPost();
        Bundle args = new Bundle();
        args.putString("prod_id", prodId);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        StaticMethods.setPadding(b.layHeader, 0, statusBarHeight, 0, 0);
        StaticMethods.setPadding(b.root, 0, 0, 0, navigationBarHeight);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            prodId = getArguments().getString("prod_id");
        }

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentAddPostBinding.inflate(inflater, container, false);
        setBackgrounds();
        setRecycler();
        initListeners();

        return b.getRoot();
    }

    private void initListeners() {
        b.backBtn.setOnClickListener(view -> getActivity().onBackPressed());
        b.goBasket.setOnClickListener(v -> addPost());
    }

    private void uploadImage(MediaLocal media) {

        RequestUploadImage uploadImage = new RequestUploadImage();
        uploadImage.setAvatar(false);
        uploadImage.setObjectId(postId);
        uploadImage.setImageType(Image.option);
        uploadImage.setWidth(getWidth(media.getPath()));
        uploadImage.setHeight(getHeight(media.getPath()));
        uploadImage.setFilename(new File(media.getPath()).getName());
        uploadImage.setData(new File(media.getPath()));

        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse(
                                FileUtil.getMimeType(uploadImage.getData())),
                        uploadImage.getData());

        try {
            RequestBody filename = RequestBody.create(MediaType.parse("multipart/form-data"), uploadImage.getFilename());
            RequestBody objectId = RequestBody.create(MediaType.parse("multipart/form-data"), uploadImage.getObjectId());
            RequestBody width = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(uploadImage.getWidth()));
            RequestBody height = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(uploadImage.getHeight()));
            RequestBody imageType = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(uploadImage.getImageType()));
            RequestBody isAvatar = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(uploadImage.isAvatar()));

            MultipartBody.Part data = MultipartBody.Part.createFormData("data", URLEncoder.encode(uploadImage.getData().getPath(), "utf-8"), requestFile);

            Call<Object> call = getApiHome().uploadImage(objectId, isAvatar, imageType, width, height, filename, data);
            call.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                    if (response.code() == 200 && response.body() != null) {
                        Toast.makeText(getContext(), "Success upload image" + i, Toast.LENGTH_SHORT).show();
                        i++;
                        if (SelectedMedia.getArrayList().size() > i) {
                            uploadImage(SelectedMedia.getArrayList().get(i));
                        } else {
                            i = 0;
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                    Log.e("Add_post", "onFailure: " + t);
                }
            });

        } catch (UnsupportedEncodingException e) {
            Log.e("Add_post", "sendApi: " + e.getMessage());
        }

    }

    private void uploadVideo(MediaLocal media) {

        RequestUploadImage uploadImage = new RequestUploadImage();
        uploadImage.setObjectId(postId);
        uploadImage.setImageType(0);
        uploadImage.setData(new File(media.getPath()));

        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse(
                                FileUtil.getMimeType(uploadImage.getData())),
                        uploadImage.getData());

        try {
            RequestBody objectId = RequestBody.create(MediaType.parse("multipart/form-data"), uploadImage.getObjectId());
            RequestBody videoType = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(uploadImage.getImageType()));
            RequestBody isVertical = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(true));

            MultipartBody.Part data = MultipartBody.Part.createFormData("Video", URLEncoder.encode(uploadImage.getData().getPath(), "utf-8"), requestFile);

            Call<Object> call = getApiHome().uploadVideo(objectId, isVertical, videoType, data);
            call.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                    if (response.code() == 200 && response.body() != null) {
                        Toast.makeText(getContext(), "Success upload image" + i, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                    Log.e("Add_post", "onFailure: " + t);
                }
            });

        } catch (UnsupportedEncodingException e) {
            Log.e("Add_post", "sendApi: " + e.getMessage());
        }

    }

    private int getHeight(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

//Returns null, sizes are in the options variable
        BitmapFactory.decodeFile(path, options);
        return options.outHeight;
    }

    private int getWidth(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(path, options);
        return options.outWidth;
    }

    private void addPost() {
        RequestAddPost r = new RequestAddPost();
        r.setDescription(b.desc.getText().toString().trim());
        r.setName(b.title.getText().toString().trim());
        r.setProductBaseId(prodId);


        Call<String> call = StaticMethods.getApiHome().addPost(r);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.code() == 200 && response.body() != null) {
                    postId = response.body();
                    if (SelectedMedia.getArrayList().size() == 0) {
                        Toast.makeText(getContext(), "Files size must be more than 0", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (SelectedMedia.getArrayList().get(0).getType() == 3) {
                        uploadVideo(SelectedMedia.getArrayList().get(0));
                    } else
                        uploadImage(SelectedMedia.getArrayList().get(i));

                    Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setBackgrounds() {
        setBackgroundDrawable(getContext(), b.title, R.color.white, R.color.low_contrast, 10, false, 1);
        setBackgroundDrawable(getContext(), b.bgStore, R.color.bg, 0, 10, false, 0);
        setBackgroundDrawable(getContext(), b.layDataProduct, R.color.white, R.color.low_contrast, 10, false, 1);
        setBackgroundDrawable(getContext(), b.layBtnBasket, R.color.accent, 0, 50, false, 0);
    }

    private void setRecycler() {
        mediaAddPost = new AdapterMediaAddPost(getContext(), getActivity());
        b.recMedia.setLayoutManager(new GridLayoutManager(getContext(), 2));
        b.recMedia.setAdapter(mediaAddPost);
    }

    @Override
    public boolean onBackPressed() {
        SelectedMedia.getArrayList().clear();
        return false;
    }
}