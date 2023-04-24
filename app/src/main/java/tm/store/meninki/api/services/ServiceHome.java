package tm.store.meninki.api.services;

import com.google.gson.JsonObject;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import tm.store.meninki.api.data.ProductDetails;
import tm.store.meninki.api.data.UserProfile;
import tm.store.meninki.api.enums.Image;
import tm.store.meninki.api.request.RequestAddPost;
import tm.store.meninki.api.request.RequestAddProduct;
import tm.store.meninki.api.request.RequestAddToCard;
import tm.store.meninki.api.request.RequestCard;
import tm.store.meninki.api.request.RequestCreateOrder;
import tm.store.meninki.api.request.RequestCreateShop;
import tm.store.meninki.api.request.RequestGetAllOrder;
import tm.store.meninki.api.request.RequestPlaceOrder;
import tm.store.meninki.api.response.ResponseCard;
import tm.store.meninki.api.response.ResponseGetOnePost;
import tm.store.meninki.api.response.ResponseHomeShops;
import tm.store.meninki.data.HomeArray;

public interface ServiceHome {
    @GET("api/Product/GetById/{id}")
    Call<ProductDetails> getProductsById(@Header("Authorization") String token, @Path("id") String id);

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

    @POST("api/Order/AddCard")
    Call<Boolean> addToCard(@Body RequestAddToCard requestAddProduct);

    @POST("api/Order/GetAll")
    Call<Boolean> getAllOrder(@Body RequestGetAllOrder requestAddProduct);

    @POST("api/Order")
    Call<Boolean> createOrder(@Body RequestCreateOrder requestAddProduct);

    @POST("api/Order/PlaceOrder")
    Call<Boolean> placeOrder(@Body RequestPlaceOrder requestAddProduct);

    @POST("api/Post")
    Call<Boolean> addPost(@Body RequestAddPost requestAddProduct);

    @GET("api/Post/GetById/{id}")
    Call<ResponseGetOnePost> getPostById(@Path("id") String id);

    @GET("api/MainScreen/Part-One")
    Call<ArrayList<HomeArray>> getHome1(@Header("authorization") String token);

    @GET("api/MainScreen/Part-Three")
    Call<ArrayList<HomeArray>> getHome3(@Header("authorization") String token);

    @GET("api/MainScreen/Part-Two")
    Call<ArrayList<HomeArray>> getHome2(@Header("authorization") String token);

    @GET("api/Shop/GetMyShop")
    Call<ArrayList<UserProfile>> getMyShops(@Query("userId") String userId, @Query("status") int status, @Query("pageNumber") int page, @Query("take") int take);

    @POST("api/Shop")
    Call<UserProfile> createShop(@Body RequestCreateShop requestAddProduct);

    @GET("api/Shop/GetById/{id}")
    Call<UserProfile> getShopById(@Path("id") String id);

    @GET("api/User/GetById/{id}")
    Call<UserProfile> getUserById(@Path("id") String id);

    @POST("api/User/GetSubscriber")
    Call<ArrayList<UserProfile>> getUserUserSubscribers(@Header("Authorization") String token, @Body JsonObject jsonObject);

    @POST("api/User/GetShopSubscriber")
    Call<ArrayList<UserProfile>> getUserShopSubscribers(@Header("Authorization") String token, @Body JsonObject jsonObject);

    @POST("api/Shop/GetUserSubscriber")
    Call<ArrayList<UserProfile>> getShopSubscribers(@Header("Authorization") String token, @Body JsonObject jsonObject);

    @POST("api/User/Subscribe")
    Call<Boolean> userSubscribe(@Header("Authorization") String token, @Body JsonObject jsonObject);

    @POST("api/Shop/Subscribe")
    Call<Boolean> shopSubscribe(@Header("Authorization") String token, @Body JsonObject jsonObject);


}
