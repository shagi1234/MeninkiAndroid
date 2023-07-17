package tm.store.meninki.fragment;

import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN;
import static tm.store.meninki.utils.Const.mainFragmentManager;
import static tm.store.meninki.utils.Lists.getPersonalCharacters;
import static tm.store.meninki.utils.Lists.setPersonalCharacters;
import static tm.store.meninki.utils.StaticMethods.getApiHome;
import static tm.store.meninki.utils.StaticMethods.navigationBarHeight;
import static tm.store.meninki.utils.StaticMethods.setBackgroundDrawable;
import static tm.store.meninki.utils.StaticMethods.statusBarHeight;

import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tm.store.meninki.R;
import tm.store.meninki.adapter.AdapterPersonalCharacters;
import tm.store.meninki.api.data.PersonalCharacterDto;
import tm.store.meninki.api.enums.Image;
import tm.store.meninki.api.request.RequestUploadImage;
import tm.store.meninki.data.CharactersDto;
import tm.store.meninki.data.MediaLocal;
import tm.store.meninki.data.SelectedMedia;
import tm.store.meninki.databinding.FragmentCharactericsBinding;
import tm.store.meninki.interfaces.OnBackPressedFragment;
import tm.store.meninki.interfaces.OnChangeProductCharactersCount;
import tm.store.meninki.shared.Account;
import tm.store.meninki.utils.FileUtil;
import tm.store.meninki.utils.Option;
import tm.store.meninki.utils.StaticMethods;

public class FragmentCharacterics extends Fragment implements OnBackPressedFragment {
    private FragmentCharactericsBinding b;
    private BottomSheetBehavior<View> bottomSheetBehavior;

    public static int countVariant;
    private AdapterPersonalCharacters adapterPersonalCharacters;
    private String productId;
    private String TAG = "FragmentCharacteristics";
    private boolean isEmptyCharacteristics;
    private CharactersDto personalCharacters;
    private int i = 0;

    public static FragmentCharacterics newInstance(String productId) {
        FragmentCharacterics fragment = new FragmentCharacterics();
        Bundle args = new Bundle();
        args.putString("prod_id", productId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            productId = getArguments().getString("prod_id");
        }
        isEmptyCharacteristics = getPersonalCharacters().getOptionTitles().size() == 0;
        personalCharacters = getPersonalCharacters();
    }

    @Override
    public void onResume() {
        super.onResume();
        StaticMethods.setPadding(b.getRoot(), 0, statusBarHeight, 0, navigationBarHeight);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentCharactericsBinding.inflate(inflater, container, false);

        initBottomSheet();
        setRecycler();
        initListeners();

        return b.getRoot();
    }

    private void setRecycler() {
        adapterPersonalCharacters = new AdapterPersonalCharacters(getContext(), personalCharacters, productId);
        b.recMedia.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        b.recMedia.setAdapter(adapterPersonalCharacters);
    }

    private void initBottomSheet() {
        setBackgroundDrawable(getContext(), b.txtGoBasket, R.color.accent, 0, 50, false, 0);
        setBackgroundDrawable(getContext(), b.bgRedactorCharacter, R.color.contrast, 0, 50, false, 0);

        bottomSheetBehavior = BottomSheetBehavior.from(b.bottomSheet.root);
        bottomSheetBehavior.setHideable(true);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (STATE_HIDDEN == newState) {
                    b.grayContainer.setVisibility(View.GONE);
                } else {
                    b.grayContainer.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                if (Math.abs(slideOffset) < 0.5) {
                    b.grayContainer.setAlpha((float) (slideOffset / 2 + 0.5));
                }
            }
        });

    }

    private void uploadImage(ArrayList<MediaLocal> mediaLocals) {
        MediaLocal media = mediaLocals.get(i);

        RequestUploadImage uploadImage = new RequestUploadImage();
        uploadImage.setAvatar(false);
        uploadImage.setObjectId(media.getUuid());
        uploadImage.setImageType(Image.option);
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
                        if (getActivity() == null) return;
                        getActivity().runOnUiThread(() -> Toast.makeText(getContext(), getActivity().getResources().getString(R.string.success_upload_image) + i, Toast.LENGTH_SHORT).show());

                        i++;
                        if (mediaLocals.size() > i) {
                            uploadImage(mediaLocals);
                        } else {
                            i = 0;
                            getActivity().runOnUiThread(() -> getActivity().onBackPressed());
                        }
                    } else {
                        getActivity().runOnUiThread(() -> Toast.makeText(getContext(), getActivity().getResources().getString(R.string.error_while_uploading_image), Toast.LENGTH_SHORT).show());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                    Log.e("Add_option", "onFailure: " + t);
                    getActivity().runOnUiThread(() -> Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show());
                }
            });

        } catch (UnsupportedEncodingException e) {
            Log.e("Add_option", "sendApi: " + e.getMessage());
            getActivity().runOnUiThread(() -> Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show());
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

    private void initListeners() {
        b.backBtn.setOnClickListener(v -> getActivity().onBackPressed());

        b.addCharacterics.setOnClickListener(v -> bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED));

        b.grayContainer.setOnClickListener(v -> bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN));

        b.bottomSheet.photo.setOnClickListener(v -> {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            personalCharacters.getOptionTitles().add("Characteristic");
            personalCharacters.getOptions().add(new ArrayList<>());
            personalCharacters.getOptionTypes().add(Option.CHARACTER_IMAGE);
            adapterPersonalCharacters.insert(adapterPersonalCharacters.getItemCount() - 1);
        });

        b.bottomSheet.txt.setOnClickListener(v -> {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            personalCharacters.getOptionTitles().add("Characteristic");
            personalCharacters.getOptions().add(new ArrayList<>());
            personalCharacters.getOptionTypes().add(Option.CHARACTER_TEXT);
            adapterPersonalCharacters.insert(adapterPersonalCharacters.getItemCount() - 1);
        });

        b.goBasket.setOnClickListener(v -> createOrUpdateOption());
    }

    private void createOrUpdateOption() {
        CharactersDto charactersDto = new CharactersDto();
        charactersDto.setOptions(personalCharacters.getOptions());
        charactersDto.setOptionTitles(personalCharacters.getOptionTitles());
        charactersDto.setOptionTypes(null);

        Call<ArrayList<PersonalCharacterDto>> call;

        if (isEmptyCharacteristics) call = StaticMethods.getApiHome().createOption(charactersDto);
        else call = StaticMethods.getApiHome().updateOption(charactersDto);

        call.enqueue(new Callback<ArrayList<PersonalCharacterDto>>() {
            @Override
            public void onResponse(Call<ArrayList<PersonalCharacterDto>> call, Response<ArrayList<PersonalCharacterDto>> response) {
                if (response.code() == 200 && response.body() != null) {
                    countVariant = response.body().size();
                    setPersonalCharacters(personalCharacters);

                    for (int i = 0; i < personalCharacters.getOptionTypes().size(); i++) {
                        if (personalCharacters.getOptionTypes().get(i) == 2) {

                            ArrayList<MediaLocal> arrayList = new ArrayList<>();

                            for (int j = 0; j < personalCharacters.getOptions().get(i).size(); j++) {
                                arrayList.add(new MediaLocal(personalCharacters.getOptions().get(i).get(j).getOptionsImageId(), personalCharacters.getOptions().get(i).get(j).getImagePath()));
                            }

                            AsyncTask.execute(() -> uploadImage(arrayList));
                        }
                    }

                }

            }

            @Override
            public void onFailure(Call<ArrayList<PersonalCharacterDto>> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t);
            }
        });
    }

    @Override
    public boolean onBackPressed() {
        Fragment fragment = mainFragmentManager.findFragmentByTag(FragmentAddProduct.class.getName());
        if (fragment instanceof OnChangeProductCharactersCount) {
            ((OnChangeProductCharactersCount) fragment).onCountChange(personalCharacters.getOptions().size());
        }

        if (bottomSheetBehavior.getState() != STATE_HIDDEN) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            return true;
        }

        return false;
    }
}