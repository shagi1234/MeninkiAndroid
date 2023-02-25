package tm.store.meninki.api.services;

import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import tm.store.meninki.api.data.ProductDto;

public interface ServiceHome {
    @POST("api/Product/GetAll")
    Call<ArrayList<ProductDto>> getProducts(@Body JsonObject jsonObject);

    @GET("api/Product/GetById/{id}")
    Call<ProductDto> getProductsById(@Path("id") String id);

    @GET("api/Product/GetById/{id}")
    Call<ProductDto> getAllShop(@Path("id") String id);
}
