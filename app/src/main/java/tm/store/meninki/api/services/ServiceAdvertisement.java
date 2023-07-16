package tm.store.meninki.api.services;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import tm.store.meninki.api.request.RequestAddAdvertisement;
import tm.store.meninki.api.request.RequestAllAdvertisement;
import tm.store.meninki.data.AdvertisementDto;

public interface ServiceAdvertisement {
    @POST("api/AdvertisementBoards/GetAll")
    Call<ArrayList<AdvertisementDto>> getAllAdvertisements(@Body RequestAllAdvertisement requestAllAdvertisement);

    @POST("api/AdvertisementBoards/GetById/{id}")
    Call<AdvertisementDto> getAdsById(@Path("id") String id);

    @POST("api/AdvertisementBoards")
    Call<AdvertisementDto> addAdvertisement(@Body RequestAddAdvertisement requestAddAdvertisement);
}
