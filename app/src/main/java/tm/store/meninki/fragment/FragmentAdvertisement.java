package tm.store.meninki.fragment;

import static tm.store.meninki.utils.StaticMethods.convertTime;
import static tm.store.meninki.utils.StaticMethods.logWrite;
import static tm.store.meninki.utils.StaticMethods.setBackgroundDrawable;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

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

    public FragmentAdvertisement() {
        // Required empty public constructor
    }


    public static FragmentAdvertisement newInstance(String id) {
        FragmentAdvertisement fragment = new FragmentAdvertisement();
        Bundle args = new Bundle();
        args.putString("id_", id);

        fragment.setArguments(args);
        return fragment;
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
        return b.getRoot();
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
                    ArrayList<String> images = new ArrayList<>(Arrays.asList(response.body().getImages()));
                    setImagePager(images);
                    setData(response.body());
                } else {
                    logWrite(response.code());
                }
            }

            @Override
            public void onFailure(Call<AdvertisementDto> call, Throwable t) {
                Toast.makeText(getContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void setData(AdvertisementDto body) {
        b.itemName.setText(body.getTitle());
        b.titleStore.setText(body.getUserName());
        b.desc.setText(body.getDescription());
//        b.tvCategory.setText(body.getCategory());
        b.tvPhone.setText(body.getPhoneNumber());
        b.price.setText(body.getPrice());
        Glide.with(getContext())
                .load(body.getUserAvatar())
                .into(b.avatarStore);
        b.tvCreatedAt.setText(convertTime(body.getCreatedAt(), getActivity()));
    }

    private void setBackgrounds() {
        setBackgroundDrawable(getContext(), b.bookmark, R.color.bg, 0, 10, false, 0);
        setBackgroundDrawable(getContext(), b.call, R.color.contrast, 0, 10, false, 0);

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