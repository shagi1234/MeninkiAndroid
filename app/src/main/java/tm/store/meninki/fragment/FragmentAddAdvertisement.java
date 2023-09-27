package tm.store.meninki.fragment;

import static tm.store.meninki.utils.StaticMethods.getApiHome;
import static tm.store.meninki.utils.StaticMethods.logWrite;
import static tm.store.meninki.utils.StaticMethods.navigationBarHeight;
import static tm.store.meninki.utils.StaticMethods.setBackgroundDrawable;
import static tm.store.meninki.utils.StaticMethods.setMargins;
import static tm.store.meninki.utils.StaticMethods.setPadding;
import static tm.store.meninki.utils.StaticMethods.statusBarHeight;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tm.store.meninki.R;
import tm.store.meninki.adapter.AdapterMediaAddPost;
import tm.store.meninki.api.ApiClient;
import tm.store.meninki.api.enums.Image;
import tm.store.meninki.api.request.RequestAddAdvertisement;
import tm.store.meninki.api.request.RequestUploadImage;
import tm.store.meninki.api.services.ServiceAdvertisement;
import tm.store.meninki.data.AdvertisementDto;
import tm.store.meninki.data.CategoryDto;
import tm.store.meninki.data.MediaLocal;
import tm.store.meninki.data.SelectedMedia;
import tm.store.meninki.databinding.FragmentAddAdvertisementBinding;
import tm.store.meninki.interfaces.OnBackPressedFragment;
import tm.store.meninki.utils.FileUtil;
import tm.store.meninki.utils.StaticMethods;

public class FragmentAddAdvertisement extends Fragment implements OnBackPressedFragment {
    FragmentAddAdvertisementBinding b;
    String userId;
    RequestAddAdvertisement requestBody = new RequestAddAdvertisement();
    ArrayList<CategoryDto> categories = new ArrayList<>();
    ArrayList<String> regions = new ArrayList<>();
    private AdapterMediaAddPost mediaAddPost;
    private int selectedRegion;
    int i;
    BottomSheetDialog dialog;


    public FragmentAddAdvertisement() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        setMargins(b.layHeader, 0, statusBarHeight, 0, 0);
        setPadding(b.getRoot(), 0, 0, 0, navigationBarHeight);

    }

    public static FragmentAddAdvertisement newInstance(String userId) {
        FragmentAddAdvertisement fragment = new FragmentAddAdvertisement();
        Bundle args = new Bundle();
        args.putString("uid", userId);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getString("uid");
        }

        if (getActivity() == null) return;
        Window window = getActivity().getWindow();
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        b = FragmentAddAdvertisementBinding.inflate(inflater, container, false);
        setBackgrounds();
        setRecycler();
        getAllCategories();
        getRegions();
        initListeners();
        return b.getRoot();
    }

    private void getAllCategories() {
        Call<ArrayList<CategoryDto>> call = StaticMethods.getApiCategory().getAllCategory(1);
        call.enqueue(new Callback<ArrayList<CategoryDto>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<CategoryDto>> call, @NonNull Response<ArrayList<CategoryDto>> response) {

                if (response.code() == 200 && response.body() != null) {
//                    setTabCategories(response.body());
                    categories = response.body();
                } else {
                    logWrite(response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<CategoryDto>> call, @NonNull Throwable t) {
                logWrite(t.getMessage());

            }
        });

    }


    private void setRecycler() {
        mediaAddPost = new AdapterMediaAddPost(getContext(), getActivity(), true);
        b.recMedia.setLayoutManager(new GridLayoutManager(getContext(), 3));
        b.recMedia.setAdapter(mediaAddPost);
    }

    private void initListeners() {
        b.backBtn.setOnClickListener(view -> {
            if (getActivity() == null) return;
            getActivity().onBackPressed();
        });
        b.btnSave.setOnClickListener(view -> {
            b.btnSave.setEnabled(false);
            setRequest();
            addAdvertisement();
        });

        b.categoryLay.setOnClickListener(view -> showDialogCategory(b.txtCategory.getText().toString(), categories));
        b.regionLay.setOnClickListener(view -> showDialog(b.txtRegion.getText().toString(), regions));


    }

    private void setRequest() {
        requestBody.setUserId(userId);
        requestBody.setName(b.title.getText().toString().trim());
        try {
            requestBody.setRegion(regions.indexOf(b.regionTv.getText().toString().trim()));
        } catch (Exception e) {
            requestBody.setRegion(0);
        }
        requestBody.setCategoryId(categories.get(0).getId());
        requestBody.setPrice(Integer.parseInt(b.price.getText().toString().trim()));
        requestBody.setPhoneNumber("+993" + b.phoneEdt.getText().toString().trim());
        requestBody.setDescription(b.desc.getText().toString().trim());
    }

    private void setBackgrounds() {

        setBackgroundDrawable(getContext(), b.desc, R.color.low_contrast, 0, 10, false, 0);
        setBackgroundDrawable(getContext(), b.title, R.color.low_contrast, 0, 10, false, 0);
        setBackgroundDrawable(getContext(), b.price, R.color.low_contrast, 0, 10, false, 0);
        setBackgroundDrawable(getContext(), b.categoryLay, R.color.white, 0, 10, false, 0);
        setBackgroundDrawable(getContext(), b.regionLay, R.color.white, 0, 10, false, 0);
        setBackgroundDrawable(getContext(), b.phoneLay, R.color.low_contrast, 0, 10, false, 0);
        setBackgroundDrawable(getContext(), b.saveLay, R.color.accent, 0, 50, false, 0);
    }

    private void addAdvertisement() {
        ServiceAdvertisement request = (ServiceAdvertisement) ApiClient.createRequest(ServiceAdvertisement.class);
        Call<AdvertisementDto> call = request.addAdvertisement(requestBody);
        call.enqueue(new Callback<AdvertisementDto>() {
            @Override
            public void onResponse(Call<AdvertisementDto> call, Response<AdvertisementDto> response) {
                if (response.body() == null || getActivity() == null) return;

                uploadImage(response.body().getId());
                Log.e("TAG_salam", "onResponse: " + response.body().getId());
                Toast.makeText(getContext(), R.string.success, Toast.LENGTH_SHORT).show();
                getActivity().onBackPressed();

                b.btnSave.setEnabled(true);
                b.grayContainer.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<AdvertisementDto> call, Throwable t) {
                Toast.makeText(getContext(), R.string.went_error, Toast.LENGTH_SHORT).show();
                b.btnSave.setEnabled(true);
                Log.e("TAG_add_ads", "onFailure: " + t);
                b.grayContainer.setVisibility(View.GONE);

            }
        });
    }

    private void getRegions() {
        regions.add("Ahal");
        regions.add("Lebap");
        regions.add("Balkan");
        regions.add("Dashoguz");
        regions.add("Mary");
        regions.add("Ashgabat");
    }

    private void uploadImage(String id) {
        MediaLocal media = SelectedMedia.getProductImageList().get(i);

        RequestUploadImage uploadImage = new RequestUploadImage();
        uploadImage.setAvatar(false);
        uploadImage.setObjectId(id);
        uploadImage.setImageType(Image.advertisementBoard);
        uploadImage.setWidth(getWidth(media.getPath()));
        uploadImage.setHeight(getHeight(media.getPath()));
        uploadImage.setImage(new File(media.getPath()));

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
                        if (getActivity() != null)
                            Toast.makeText(getContext(), getActivity().getResources().getString(R.string.success_upload_image) + i, Toast.LENGTH_SHORT).show();
                        i++;
                        if (SelectedMedia.getProductImageList().size() > i) {
                            uploadImage(id);
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

    private int getWidth(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(path, options);
        return options.outWidth;
    }

    private int getHeight(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(path, options);
        return options.outHeight;
    }

    private void showDialogCategory(String title, ArrayList<CategoryDto> items) {
        if (getContext() == null) return;
        dialog = new BottomSheetDialog(getContext(), R.style.SheetDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheet_ads_filter);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnim;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setDialogComponentsCategory(title, items);
        dialog.show();

    }

    private void showDialog(String title, ArrayList<String> items) {
        if (getContext() == null) return;
        dialog = new BottomSheetDialog(getContext(), R.style.SheetDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheet_ads_filter);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnim;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setDialogComponents(title, items);
        dialog.show();

    }

    private void setDialogComponentsCategory(String title, ArrayList<CategoryDto> items) {
        LinearLayout root = dialog.findViewById(R.id.root_lay);
        TextView headerText = dialog.findViewById(R.id.title);
        LinearLayout itemsContent = dialog.findViewById(R.id.items_content);
        headerText.setText(title);
        if (items.size() == 0) return;
        assert itemsContent != null;

        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textParams.setMargins(0, 10, 0, 0);
        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.inter_medium);
        int textColor = ContextCompat.getColor(getContext(), R.color.accent);

        for (CategoryDto item : items) {
            TextView textView = new TextView(getContext());
            textView.setLayoutParams(textParams);
            textView.setTypeface(typeface);
            textView.setPadding(20, 10, 20, 10);
            textView.setTextColor(textColor);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            textView.setText(item.getName());
            itemsContent.addView(textView);

            textView.setOnClickListener(view -> {
                b.categoryTv.setText(textView.getText().toString().trim());
                dialog.dismiss();
            });
        }

        setPadding(root, 0, 0, 0, navigationBarHeight);

    }

    private void setDialogComponents(String title, ArrayList<String> items) {
        LinearLayout root = dialog.findViewById(R.id.root_lay);
        TextView headerText = dialog.findViewById(R.id.title);
        LinearLayout itemsContent = dialog.findViewById(R.id.items_content);
        headerText.setText(title);
        if (items.size() == 0) return;
        assert itemsContent != null;

        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textParams.setMargins(0, 10, 0, 0);
        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.inter_medium);
        int textColor = ContextCompat.getColor(getContext(), R.color.accent);

        for (String item : items) {
            TextView textView = new TextView(getContext());
            textView.setLayoutParams(textParams);
            textView.setTypeface(typeface);
            textView.setPadding(20, 10, 20, 10);
            textView.setTextColor(textColor);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            textView.setText(item);
            itemsContent.addView(textView);

            textView.setOnClickListener(view -> {
                b.regionTv.setText(textView.getText().toString().trim());
                dialog.dismiss();
            });
        }

        setPadding(root, 0, 0, 0, navigationBarHeight);

    }

    @Override
    public boolean onBackPressed() {
        SelectedMedia.getProductImageList().clear();
        Window window = requireActivity().getWindow();
        // Set the desired softInputMode
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        return false;
    }
}