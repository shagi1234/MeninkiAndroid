package tm.store.meninki.fragment;

import static tm.store.meninki.utils.Const.mainFragmentManager;
import static tm.store.meninki.utils.FragmentHelper.addFragment;
import static tm.store.meninki.utils.FragmentHelper.addFragmentWithAnim;
import static tm.store.meninki.utils.StaticMethods.getApiHome;
import static tm.store.meninki.utils.StaticMethods.navigationBarHeight;
import static tm.store.meninki.utils.StaticMethods.setBackgroundDrawable;
import static tm.store.meninki.utils.StaticMethods.setMargins;
import static tm.store.meninki.utils.StaticMethods.setPadding;
import static tm.store.meninki.utils.StaticMethods.statusBarHeight;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.gson.Gson;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tm.store.meninki.R;
import tm.store.meninki.adapter.AdapterMediaAddPost;
import tm.store.meninki.api.RetrofitCallback;
import tm.store.meninki.api.data.UserProfile;
import tm.store.meninki.api.enums.Image;
import tm.store.meninki.api.request.RequestAddProduct;
import tm.store.meninki.api.request.RequestUploadImage;
import tm.store.meninki.data.CategoryDto;
import tm.store.meninki.data.SelectedMedia;
import tm.store.meninki.databinding.FragmentAddProductBinding;
import tm.store.meninki.interfaces.OnBackPressedFragment;
import tm.store.meninki.interfaces.OnCategoryChecked;
import tm.store.meninki.interfaces.OnChangeProductCharactersCount;
import tm.store.meninki.interfaces.OnShopChecked;
import tm.store.meninki.utils.FileUtil;
import tm.store.meninki.utils.Lists;
import tm.store.meninki.utils.StaticMethods;

public class FragmentAddProduct extends Fragment implements OnBackPressedFragment, OnChangeProductCharactersCount, OnShopChecked, OnCategoryChecked {
    private FragmentAddProductBinding b;
    private AdapterMediaAddPost mediaAddPost;
    private String[] categoryIds;
    private String productId;
    private ArrayList<CategoryDto> categories = new ArrayList<>();
    private UserProfile shop;

    public static FragmentAddProduct newInstance() {
        FragmentAddProduct fragment = new FragmentAddProduct();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        setMargins(b.layHeader, 0, statusBarHeight, 0, 0);
        setPadding(b.getRoot(), 0, 0, 0, navigationBarHeight);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productId = UUID.randomUUID().toString();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentAddProductBinding.inflate(inflater, container, false);
        check();
        initListeners();
        setBackgrounds();
        setRecycler();
        return b.getRoot();
    }

    private void uploadImage() {
//        List<MultipartBody.Part> datas = new ArrayList<>();
//        List<MultipartBody.Part> filenames = new ArrayList<>();
//        List<MultipartBody.Part> heights = new ArrayList<>();
//        List<MultipartBody.Part> widths = new ArrayList<>();
//        List<MultipartBody.Part> objectIds = new ArrayList<>();
//        List<MultipartBody.Part> imageTypes = new ArrayList<>();
//        List<MultipartBody.Part> isAvatars = new ArrayList<>();


        for (int i = 0; i < SelectedMedia.getArrayList().size(); i++) {

            RequestUploadImage uploadImage = new RequestUploadImage(
                    productId,
                    false,
                    Image.option,
                    getWidth(SelectedMedia.getArrayList().get(i).getPath()),
                    getHeight(SelectedMedia.getArrayList().get(i).getPath()),
                    new File(SelectedMedia.getArrayList().get(i).getPath()).getName(),
                    new File(SelectedMedia.getArrayList().get(i).getPath())
            );

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

//                MultipartBody.Builder filename = new MultipartBody.Builder();
//                filename.setType(MultipartBody.FORM);
//                filename.addFormDataPart(Image.keyFileName + "Images[" + i + "]", uploadImage.getFilename());
//
//                MultipartBody.Builder objectId = new MultipartBody.Builder();
//                objectId.setType(MultipartBody.FORM);
//                objectId.addFormDataPart(Image.keyObjectId +"Images[" + i + "]" , uploadImage.getObjectId());
//
//                MultipartBody.Builder height = new MultipartBody.Builder();
//                height.setType(MultipartBody.FORM);
//                height.addFormDataPart(Image.keyHeight +"Images[" + i + "]", String.valueOf(uploadImage.getHeight()));
//
//                MultipartBody.Builder width = new MultipartBody.Builder();
//                width.setType(MultipartBody.FORM);
//                width.addFormDataPart(Image.keyWidth +"Images[" + i + "]", String.valueOf(uploadImage.getHeight()));
//
//                MultipartBody.Builder imageType = new MultipartBody.Builder();
//                imageType.setType(MultipartBody.FORM);
//                imageType.addFormDataPart(Image.keyImageType +"Images[" + i + "]", String.valueOf(uploadImage.getImageType()));
//
//                MultipartBody.Builder isAvatar = new MultipartBody.Builder();
//                isAvatar.setType(MultipartBody.FORM);
//                isAvatar.addFormDataPart(Image.keyIsAvatar +"Images[" + i + "]", String.valueOf(uploadImage.isAvatar()));
//
//                MultipartBody.Part data = MultipartBody.Part.createFormData("data"+"Images[" + i + "]", URLEncoder.encode(uploadImage.getData().getPath(), "utf-8"), requestFile);
                MultipartBody.Part data = MultipartBody.Part.createFormData("data", URLEncoder.encode(uploadImage.getData().getPath(), "utf-8"), requestFile);
//
                Call<Boolean> call = getApiHome().uploadImage(objectId, isAvatar, imageType, width, height, filename, data);
                call.enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {

                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {

                    }
                });
//                datas.add(fileToUpload);
//                filenames.add(MultipartBody.Part.createFormData(Image.keyFileName +"Images[" + i + "]", uploadImage.getData().getPath(), filename.build()));
//                objectIds.add(MultipartBody.Part.createFormData(Image.keyObjectId +"Images[" + i + "]", uploadImage.getData().getPath(), objectId.build()));
//                widths.add(MultipartBody.Part.createFormData(Image.keyWidth +"Images[" + i + "]", uploadImage.getData().getPath(), width.build()));
//                heights.add(MultipartBody.Part.createFormData(Image.keyHeight +"Images[" + i + "]", uploadImage.getData().getPath(), height.build()));
//                imageTypes.add(MultipartBody.Part.createFormData(Image.keyImageType +"Images[" + i + "]", uploadImage.getData().getPath(), imageType.build()));
//                isAvatars.add(MultipartBody.Part.createFormData(Image.keyIsAvatar +"Images[" + i + "]", uploadImage.getData().getPath(), isAvatar.build()));

            } catch (UnsupportedEncodingException e) {
                Log.e("Add_post", "sendApi: " + e.getMessage());
            }
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

    private void createProduct() {

        categoryIds = new String[categories.size()];

        for (int i = 0; i < categories.size(); i++) {
            categoryIds[i] = categories.get(i).getId();
        }

        uploadImage();

        RequestAddProduct requestAddProduct = new RequestAddProduct();
        requestAddProduct.setName(b.title.getText().toString().trim());
        requestAddProduct.setId(productId);
        requestAddProduct.setDescription(b.price.getText().toString().trim());
        requestAddProduct.setPrice(Double.parseDouble(b.oldPrice.getText().toString().trim()));
        requestAddProduct.setDiscountPrice(Double.parseDouble(b.price.getText().toString().trim()));
        requestAddProduct.setCategoryIds(categoryIds);
        requestAddProduct.setShopId(shop.getId());

        Call<Boolean> call = StaticMethods.getApiHome().createProduct(requestAddProduct);
        call.enqueue(new RetrofitCallback<Boolean>() {
            @Override
            public void onResponse(Boolean response) {

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void check() {
        if (getContext() == null) return;

        if (Lists.getPersonalCharacters().size() > 0) {
            b.wariants.setVisibility(View.VISIBLE);
            b.layPriceIfMany.setVisibility(View.VISIBLE);
            b.layPriceIf1.setVisibility(View.GONE);
        } else {
            b.wariants.setVisibility(View.GONE);
            b.layPriceIfMany.setVisibility(View.GONE);
            b.layPriceIf1.setVisibility(View.VISIBLE);
        }
    }

    private void initListeners() {
        b.backBtn.setOnClickListener(v -> getActivity().onBackPressed());

        b.redactorCharacter.setOnClickListener(v -> {
            b.redactorCharacter.setEnabled(false);

            addFragment(mainFragmentManager, R.id.fragment_container_main, FragmentCharacterics.newInstance());

            new Handler().postDelayed(() -> b.redactorCharacter.setEnabled(true), 200);
        });

        b.redactorPrice.setOnClickListener(v -> {
            b.redactorPrice.setEnabled(false);

            addFragment(mainFragmentManager, R.id.fragment_container_main, FragmentRedactorPrice.newInstance());

            new Handler().postDelayed(() -> b.redactorPrice.setEnabled(true), 200);
        });

        b.goBasket.setOnClickListener(v -> {
            b.goBasket.setEnabled(false);
            createProduct();

            new Handler().postDelayed(() -> b.goBasket.setEnabled(true), 200);
        });

        b.chooseCategory.setOnClickListener(v -> {
            b.chooseCategory.setEnabled(false);
            addFragmentWithAnim(mainFragmentManager, R.id.fragment_container_main, FragmentCategoryList.newInstance(null, FragmentCategoryList.TYPE_CATEGORY));
            new Handler().postDelayed(() -> b.chooseCategory.setEnabled(true), 200);
        });

        b.chooseShop.setOnClickListener(v -> {
            b.chooseShop.setEnabled(false);
            addFragmentWithAnim(mainFragmentManager, R.id.fragment_container_main, FragmentCategoryList.newInstance(null, FragmentCategoryList.TYPE_SHOP));
            new Handler().postDelayed(() -> b.chooseShop.setEnabled(true), 200);
        });

    }

    private void setBackgrounds() {
        setBackgroundDrawable(getContext(), b.title, R.color.white, R.color.hover, 4, false, 1);
        setBackgroundDrawable(getContext(), b.desc, R.color.white, R.color.hover, 4, false, 1);
        setBackgroundDrawable(getContext(), b.price, R.color.white, R.color.hover, 4, false, 1);
        setBackgroundDrawable(getContext(), b.txtGoBasket, R.color.accent, 0, 4, false, 0);
        setBackgroundDrawable(getContext(), b.oldPrice, R.color.white, R.color.hover, 4, false, 1);
        setBackgroundDrawable(getContext(), b.chooseCategory, R.color.white, R.color.hover, 4, false, 1);
        setBackgroundDrawable(getContext(), b.chooseShop, R.color.white, R.color.hover, 4, false, 1);
        setBackgroundDrawable(getContext(), b.count, R.color.white, R.color.hover, 4, false, 1);
        setBackgroundDrawable(getContext(), b.desc, R.color.white, R.color.hover, 4, false, 1);
        setBackgroundDrawable(getContext(), b.wariants, R.color.hover, 0, 10, 10, 0, 0, false, 0);
        setBackgroundDrawable(getContext(), b.prices, R.color.hover, 0, 10, 10, 0, 0, false, 0);
        setBackgroundDrawable(getContext(), b.redactorCharacter, R.color.hover, 0, 0, 0, 10, 10, false, 0);
        setBackgroundDrawable(getContext(), b.redactorPrice, R.color.hover, 0, 0, 0, 10, 10, false, 0);
    }

    private void setRecycler() {
        mediaAddPost = new AdapterMediaAddPost(getContext());
        b.recMedia.setLayoutManager(new GridLayoutManager(getContext(), 2));
        b.recMedia.setAdapter(mediaAddPost);
    }

    @Override
    public boolean onBackPressed() {
        SelectedMedia.getArrayList().clear();
        return false;
    }

    @Override
    public void onCountChange(int count) {
        check();
    }

    @Override
    public void onShopChecked(boolean isChecked, UserProfile shop) {
        if (isChecked) {
            this.shop = shop;
        } else {
            this.shop = null;
        }
        Log.e("TAG_add_prod", "onShopChecked: " + new Gson().toJson(this.shop));
    }

    @Override
    public void onChecked(boolean isChecked, CategoryDto categoryDto) {
        if (isChecked) {
            categories.add(categoryDto);
        } else {
            categories.remove(categoryDto);
        }
        Log.e("TAG_add_prod", "onChecked: " + new Gson().toJson(categories));
    }
}