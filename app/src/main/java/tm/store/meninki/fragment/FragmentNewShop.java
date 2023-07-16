package tm.store.meninki.fragment;

import static android.app.Activity.RESULT_OK;
import static tm.store.meninki.utils.Const.mainFragmentManager;
import static tm.store.meninki.utils.FragmentHelper.addFragmentWithAnim;
import static tm.store.meninki.utils.StaticMethods.getApiHome;
import static tm.store.meninki.utils.StaticMethods.navigationBarHeight;
import static tm.store.meninki.utils.StaticMethods.setBackgroundDrawable;
import static tm.store.meninki.utils.StaticMethods.statusBarHeight;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tm.store.meninki.R;
import tm.store.meninki.api.data.UserProfile;
import tm.store.meninki.api.enums.Image;
import tm.store.meninki.api.request.RequestCreateShop;
import tm.store.meninki.api.request.RequestUploadImage;
import tm.store.meninki.data.CategoryDto;
import tm.store.meninki.databinding.FragmentNewShopBinding;
import tm.store.meninki.interfaces.OnBackPressedFragment;
import tm.store.meninki.interfaces.OnCategoryChecked;
import tm.store.meninki.shared.Account;
import tm.store.meninki.utils.FileUtil;
import tm.store.meninki.utils.StaticMethods;

public class FragmentNewShop extends Fragment implements OnCategoryChecked, OnBackPressedFragment {
    private FragmentNewShopBinding b;
    private ArrayList<CategoryDto> categories = new ArrayList<>();
    private final int PICK_IMAGE_REQUEST = 1;
    private String filePath;

    public static FragmentNewShop newInstance() {
        FragmentNewShop fragment = new FragmentNewShop();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Change the soft input mode

    }

    @Override
    public void onResume() {
        super.onResume();
        StaticMethods.setPadding(b.getRoot(), 0, statusBarHeight, 0, navigationBarHeight);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentNewShopBinding.inflate(inflater, container, false);
        new Handler().postDelayed(this::setBackgrounds, 100);
        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        initListeners();
        return b.getRoot();
    }

    private void initListeners() {
        b.goBasket.setOnClickListener(v -> {
            b.goBasket.setEnabled(false);
            new Handler().postDelayed(() -> b.goBasket.setEnabled(true), 200);

            if (isContentsEmpty()) {
                Toast.makeText(getContext(), getActivity().getResources().getString(R.string.please_give_information_completely), Toast.LENGTH_SHORT).show();
                return;
            }

            createShop();

        });
        b.chooseCategory.setOnClickListener(v -> {
            b.chooseCategory.setEnabled(false);

            categories.clear();
            addFragmentWithAnim(mainFragmentManager, R.id.fragment_container_main, FragmentCategoryList.newInstance(null, FragmentCategoryList.TYPE_CATEGORY, ""));
            new Handler().postDelayed(() -> b.chooseCategory.setEnabled(true), 200);
        });

        b.layAddPhoto.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });
    }

    private boolean isContentsEmpty() {
        return !(b.storeName.getText().toString().trim().length() > 0 && categories.size() > 0 && b.contactPhone1.getText().toString().trim().length() > 0 && b.userName.getText().toString().trim().length() > 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            if (getContext() == null) return;

            Uri selectedImageUri = data.getData();
            filePath = FileUtil.getPath(getContext(), selectedImageUri);
            // Use the selectedImageUri to access the image
            // ...
            Glide.with(getContext()).load(filePath).into(b.imageShop);
        }
    }

    private void uploadImage(String productId) {
        RequestUploadImage uploadImage = new RequestUploadImage();
        uploadImage.setAvatar(true);
        uploadImage.setObjectId(productId);
        uploadImage.setImageType(Image.shop);
        uploadImage.setWidth(getWidth(filePath));
        uploadImage.setHeight(getHeight(filePath));
        uploadImage.setImage(new File(filePath));

        RequestBody requestFile = RequestBody.create(MediaType.parse(FileUtil.getMimeType(uploadImage.getImage())), uploadImage.getImage());

        try {
            RequestBody objectId = RequestBody.create(MediaType.parse("multipart/form-data"), uploadImage.getObjectId());
            RequestBody width = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(uploadImage.getWidth()));
            RequestBody height = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(uploadImage.getHeight()));
            RequestBody imageType = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(uploadImage.getImageType()));
            RequestBody isAvatar = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(uploadImage.isAvatar()));

            MultipartBody.Part data = MultipartBody.Part.createFormData("Image", URLEncoder.encode(uploadImage.getImage().getPath(), "utf-8"), requestFile);

            Call<Object> call = getApiHome().uploadImage(objectId, isAvatar, imageType, width, height, data);
            call.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                    if (response.code() == 200 && response.body() != null) {
                        Toast.makeText(getContext(), getActivity().getResources().getString(R.string.success_upload_image), Toast.LENGTH_SHORT).show();
                    }
                    Log.e("Add_post", "onResponse: " + response.message());

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

        BitmapFactory.decodeFile(path, options);
        return options.outHeight;
    }

    private int getWidth(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(path, options);
        return options.outWidth;
    }

    private void createShop() {
        RequestCreateShop requestCreateShop = new RequestCreateShop();
        String[] categoryIds = new String[categories.size()];

        for (int i = 0; i < categories.size(); i++) {
            categoryIds[i] = categories.get(i).getId();
        }

        requestCreateShop.setCategories(categoryIds);
        requestCreateShop.setUserId(Account.newInstance(getContext()).getPrefUserUUID());
        requestCreateShop.setName(b.storeName.getText().toString());
        requestCreateShop.setEmail(b.edtEmail.getText().toString());
        requestCreateShop.setPhoneNumber("+993 " + b.contactPhone1.getText().toString());
        requestCreateShop.setUserName(b.userName.getText().toString().trim());

        Call<UserProfile> call = StaticMethods.getApiHome().createShop(requestCreateShop);

        call.enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                if (response.code() == 200 && response.body() != null) {
                    uploadImage(response.body().getId());
                    Log.e("SUCCESS", "onResponse:");
                } else Log.e("CODE", "onResponse: " + response.code());

            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                //onError
            }
        });
    }

    private void setBackgrounds() {
        setBackgroundDrawable(getContext(), b.storeName, R.color.white, 0, 4, false, 0);
        setBackgroundDrawable(getContext(), b.website, R.color.white, 0, 4, false, 0);
        setBackgroundDrawable(getContext(), b.saveButton, R.color.accent, 0, 4, false, 0);

    }

    @Override
    public void onChecked(boolean isChecked, CategoryDto categoryDto) {
        if (isChecked) {
            categories.add(categoryDto);
        } else {
            categories.remove(categoryDto);
        }
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < categories.size(); i++) {
            s.append(categories.get(i).getName());

            if (i != categories.size() - 1) s.append(" , ");
        }

        b.chooseCategory.setText(s.toString());
    }

    @Override
    public boolean onBackPressed() {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        return false;
    }
}