package tm.store.meninki.fragment;

import static tm.store.meninki.api.Network.BASE_URL;
import static tm.store.meninki.utils.Const.mainFragmentManager;
import static tm.store.meninki.utils.FragmentHelper.addFragment;
import static tm.store.meninki.utils.StaticMethods.convertTime;
import static tm.store.meninki.utils.StaticMethods.dpToPx;
import static tm.store.meninki.utils.StaticMethods.logWrite;
import static tm.store.meninki.utils.StaticMethods.navigationBarHeight;
import static tm.store.meninki.utils.StaticMethods.setBackgroundDrawable;
import static tm.store.meninki.utils.StaticMethods.setPadding;
import static tm.store.meninki.utils.StaticMethods.statusBarHeight;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tm.store.meninki.R;
import tm.store.meninki.adapter.AdapterVerticalImagePager;
import tm.store.meninki.api.ApiClient;
import tm.store.meninki.api.data.MediaDto;
import tm.store.meninki.api.services.ServiceAdvertisement;
import tm.store.meninki.data.AdvertisementDto;
import tm.store.meninki.databinding.FragmentAdvertisementBinding;
import tm.store.meninki.databinding.FragmentAdvertisementsBinding;
import tm.store.meninki.databinding.FragmentFilterAdvertisementBinding;

public class FragmentAdvertisement extends Fragment {
    FragmentAdvertisementBinding b;
    String id;

    public static FragmentAdvertisement newInstance(String id) {
        FragmentAdvertisement fragment = new FragmentAdvertisement();
        Bundle args = new Bundle();
        args.putString("id_", id);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        setPadding(b.getRoot(), 0, statusBarHeight, 0, navigationBarHeight);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getString("id_");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        b = FragmentAdvertisementBinding.inflate(inflater, container, false);
        setBackgrounds();
        getData();
        initListeners();
        return b.getRoot();
    }

    private void initListeners() {
        b.backBtn.setOnClickListener(view -> {
            getActivity().onBackPressed();
        });

    }

    private void getData() {
        ServiceAdvertisement request = (ServiceAdvertisement) ApiClient.createRequest(ServiceAdvertisement.class);
        Call<AdvertisementDto> call = request.getAdsById(id);

        call.enqueue(new Callback<AdvertisementDto>() {
            @Override
            public void onResponse(Call<AdvertisementDto> call, Response<AdvertisementDto> response) {
                b.progressBar.setVisibility(View.GONE);
                if (response.body() == null) {
                    b.noContent.setVisibility(View.VISIBLE);
                    return;
                }

                if (response.code() == 200) {
                    b.noContent.setVisibility(View.GONE);
                    b.main.setVisibility(View.VISIBLE);
                    ArrayList<String> images = new ArrayList<>(Arrays.asList(response.body().getImages()));
                    setImagePager(images);
                    setData(response.body());
                } else {
                    logWrite(response.code());
                }

            }

            @Override
            public void onFailure(Call<AdvertisementDto> call, Throwable t) {
                b.noContent.setVisibility(View.VISIBLE);
                b.progressBar.setVisibility(View.GONE);

                Toast.makeText(getContext(), getActivity().getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                Log.e("TAG_getById_ads", "onFailure: " + t);
            }
        });

    }

    private void setData(AdvertisementDto body) {
        b.itemName.setText(body.getTitle());
        b.titleStore.setText(body.getUserName());
        b.desc.setText(body.getDescription());
        b.tvCategory.setText(body.getCategoryName());
        b.tvPhone.setText(body.getPhoneNumber());
        b.price.setText(body.getPrice() + " TMT");

        Glide.with(getContext())
                .load(BASE_URL + "/" + body.getUserAvatar())
                .into(b.avatarStore);

        b.tvCreatedAt.setText(convertTime(body.getCreatedAt(), getActivity()));
        b.call.setOnClickListener(view -> {
            // Create the intent with the ACTION_DIAL action
            Intent intent = new Intent(Intent.ACTION_DIAL);
            // Set the phone number as the data for the intent
            intent.setData(Uri.parse("tel:" + body.getPhoneNumber()));
            // Start the activity with the intent
            startActivity(intent);
        });
        b.layStore.setOnClickListener(view -> {
            b.layStore.setEnabled(false);
            new Handler().postDelayed(() -> b.layStore.setEnabled(true), 200);

            addFragment(mainFragmentManager, R.id.fragment_container_main, FragmentProfile.newInstance(FragmentProfile.TYPE_USER, body.getUserId()));
        });
    }

    private void setBackgrounds() {
        setBackgroundDrawable(getContext(), b.bgBookmark, R.color.bg, 0, 10, false, 0);
        setBackgroundDrawable(getContext(), b.bgCall, R.color.contrast, 0, 10, false, 0);

    }

    private void setImagePager(ArrayList<String> images) {
        AdapterVerticalImagePager adapterVerticalImagePager = new AdapterVerticalImagePager(getContext(), 1);

        b.imagePager.setClipToPadding(false);
        b.imagePager.setClipChildren(false);
        b.imagePager.setOffscreenPageLimit(3);
        b.imagePager.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        b.imagePager.setAdapter(adapterVerticalImagePager);

        adapterVerticalImagePager.setImages(images);
        b.indicator.setViewPager(b.imagePager);

    }
}