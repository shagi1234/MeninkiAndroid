package tm.store.meninki.fragment;

import static android.app.Activity.RESULT_OK;
import static tm.store.meninki.api.Network.BASE_URL;
import static tm.store.meninki.utils.Const.mainFragmentManager;
import static tm.store.meninki.utils.StaticMethods.getApiHome;
import static tm.store.meninki.utils.StaticMethods.navigationBarHeight;
import static tm.store.meninki.utils.StaticMethods.setBackgroundDrawable;
import static tm.store.meninki.utils.StaticMethods.setPadding;
import static tm.store.meninki.utils.StaticMethods.statusBarHeight;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.bumptech.glide.Glide;

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
import tm.store.meninki.api.RetrofitCallback;
import tm.store.meninki.api.data.UserProfile;
import tm.store.meninki.api.enums.Image;
import tm.store.meninki.api.request.RequestUploadImage;
import tm.store.meninki.data.MediaLocal;
import tm.store.meninki.data.SelectedMedia;
import tm.store.meninki.databinding.FragmentEditProfileBinding;
import tm.store.meninki.interfaces.OnBackPressedFragment;
import tm.store.meninki.shared.Account;
import tm.store.meninki.utils.FileUtil;
import tm.store.meninki.utils.StaticMethods;

public class FragmentEditProfile extends Fragment implements OnBackPressedFragment {
    private FragmentEditProfileBinding b;
    private FragmentProfile fragmentProfile;
    private int PICK_IMAGE_REQUEST = 2;
    private String filePath;
    private ProgressDialog dialog;


    public static FragmentEditProfile newInstance() {
        FragmentEditProfile fragment = new FragmentEditProfile();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentProfile = (FragmentProfile) mainFragmentManager.findFragmentByTag(FragmentProfile.class.getSimpleName());

    }

    @Override
    public void onResume() {
        super.onResume();
        setPadding(b.layHeader, 0, statusBarHeight, 0, 0);
        setPadding(b.getRoot(), 0, 0, 0, navigationBarHeight);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        b = FragmentEditProfileBinding.inflate(inflater, container, false);
        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setResources();
        initListeners();
        setBackgrounds();
        return b.getRoot();
    }

    private void initListeners() {
        b.btnEdit.setOnClickListener(view -> updateProfile());

        b.backBtn.setOnClickListener(view -> requireActivity().onBackPressed());
        b.layAddPhoto.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });
    }

    private void setResources() {
        if (fragmentProfile == null || fragmentProfile.getUser() == null) return;
        UserProfile user = fragmentProfile.getUser();
        b.edtUsername.setText(user.getUserName());
        b.edtFirstName.setText(user.getName());
        b.edtPhoneNum.setText(user.getPhoneNumber());

        Glide.with(requireContext()).load(BASE_URL + "/" + user.getImgPath()).placeholder(R.color.on_bg_ls).error(R.color.on_bg_ls).into(b.image);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            if (getContext() == null) return;

            Uri selectedImageUri = data.getData();
            filePath = FileUtil.getPath(getContext(), selectedImageUri);
            // Use the selectedImageUri to access the image
            Glide.with(getContext()).load(filePath).into(b.image);

        }
    }

    private void uploadImage(RequestUploadImage uploadImage) {

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
                        Toast.makeText(getContext(), "Success upload image", Toast.LENGTH_SHORT).show();
                    }
                    if (fragmentProfile != null) {
                        ((OnUserDataChanged) fragmentProfile).onChange();
                    }
                    dialog.dismiss();
                }

                @Override
                public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                    Log.e("Add_post", "onFailure: " + t);
                    dialog.dismiss();
                    if (fragmentProfile != null) {
                        ((OnUserDataChanged) fragmentProfile).onChange();
                    }
                }
            });

        } catch (UnsupportedEncodingException e) {
            Log.e("Add_post", "sendApi: " + e.getMessage());
        }

    }

    private void updateProfile() {
        dialog = ProgressDialog.show(getContext(), "", "Uploading. Please wait...", true);
        if (isContentEmpty()) {
            Toast.makeText(getContext(), "Please write your information", Toast.LENGTH_SHORT).show();
            return;
        }
        UserProfile u = new UserProfile();
        u.setName(b.edtFirstName.getText().toString().trim());
        u.setId(Account.newInstance(getContext()).getPrefUserUUID());
        u.setUserName(b.edtUsername.getText().toString().trim());
        u.setPhoneNumber(b.edtPhoneNum.getText().toString().trim());

        Call<Boolean> call = StaticMethods.getApiHome().updateUser(u);
        call.enqueue(new RetrofitCallback<Boolean>() {
            @Override
            public void onResponse(Boolean response) {
                if (response) {
                    Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();

                    if (filePath == null) {
                        if (fragmentProfile != null) {
                            ((OnUserDataChanged) fragmentProfile).onChange();
                        }
                        dialog.dismiss();
                        return;
                    }


                    RequestUploadImage uploadImage = new RequestUploadImage();
                    uploadImage.setAvatar(true);
                    uploadImage.setObjectId(fragmentProfile.getUser().getId());
                    uploadImage.setImageType(Image.user);
                    uploadImage.setWidth(getWidth(filePath));
                    uploadImage.setHeight(getHeight(filePath));
                    uploadImage.setImage(new File(filePath));

                    uploadImage(uploadImage);

                    return;
                }
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                dialog.dismiss();

            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    private boolean isContentEmpty() {
        return b.edtFirstName.getText().toString().trim().equals("") && b.edtPhoneNum.getText().toString().trim().equals("") && b.edtUsername.getText().toString().trim().equals("");
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


    private void setBackgrounds() {
        setBackgroundDrawable(getContext(), b.saveButton, R.color.accent, 0, 50, false, 0);
        setBackgroundDrawable(getContext(), b.edtFirstName, R.color.on_bg_ls, 0, 10, false, 0);
        setBackgroundDrawable(getContext(), b.edtUsername, R.color.on_bg_ls, 0, 10, false, 0);
        setBackgroundDrawable(getContext(), b.edtPhoneNum, R.color.on_bg_ls, 0, 10, false, 0);
    }

    @Override
    public boolean onBackPressed() {
        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        return false;
    }
}