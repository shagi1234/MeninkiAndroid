package tm.store.meninki.api.services;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import tm.store.meninki.api.data.ProductDetails;
import tm.store.meninki.api.data.ProductDto;
import tm.store.meninki.api.data.UserProfile;
import tm.store.meninki.api.enums.Image;
import tm.store.meninki.api.request.RequestAddProduct;
import tm.store.meninki.api.request.RequestCard;
import tm.store.meninki.api.request.RequestCreateShop;
import tm.store.meninki.api.request.RequestUploadImage;
import tm.store.meninki.api.response.ResponseCard;
import tm.store.meninki.api.response.ResponseHomeShops;

public interface ServiceHome {
    @POST("api/Product/GetAll")
    Call<ArrayList<ProductDto>> getProducts(@Body JsonObject jsonObject);

    @GET("api/Product/GetById/{id}")
    Call<ProductDetails> getProductsById(@Path("id") String id);

    @POST("api/Shop/GetAll")
    Call<ArrayList<ResponseHomeShops>> getAllShop(@Body RequestCard requestCard);

    @POST("api/Card")
    Call<ArrayList<ResponseCard>> getCard(@Body RequestCard requestCard);

    @POST("api/Image/CreateImage")

    @Multipart
    Call<Boolean> uploadImage(
            @Part(Image.keyObjectId) RequestBody objectId,
            @Part(Image.keyIsAvatar) RequestBody isAvatar,
            @Part(Image.keyImageType) RequestBody imageType,
            @Part(Image.keyWidth) RequestBody width,
            @Part(Image.keyHeight) RequestBody height,
            @Part(Image.keyFileName) RequestBody filename,
            @Part MultipartBody.Part image
    );

    @POST("api/Product/CreateProduct")
    Call<Boolean> createProduct(@Body RequestAddProduct requestAddProduct);

    @GET("api/Shop/GetMyShop")
    Call<ArrayList<UserProfile>> getMyShops(@Query("userId") String userId, @Query("status") int status, @Query("pageNumber") int page, @Query("take") int take);

    @POST("api/Shop")
    Call<UserProfile> createShop(@Body RequestCreateShop requestAddProduct);


}
