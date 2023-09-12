package tm.store.meninki.api.services;

import retrofit2.http.PUT;
import tm.store.meninki.api.data.response.DataCheckSms;
import tm.store.meninki.api.data.response.DataSendSms;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ServiceLogin {
    @POST("/api/User/Registration")
    Call<DataSendSms> registrationByNumber(@Body JsonObject jsonObject);

    @POST("/api/Authenticate/login-by-phone")
    Call<DataSendSms> loginByPhone(@Body JsonObject jsonObject);

    @POST("/api/User/CheckUserIsAlreadyExist")
    Call<Boolean> checkUserIsAlreadyExist(@Body JsonObject jsonObject);

    @POST("/api/Authenticate/login-by-google")
    Call<DataCheckSms> loginByGoogle(@Body JsonObject jsonObject);

    @POST("/api/User/ConfirmationPhoneNumber")
    Call<DataCheckSms> checkSms(@Body JsonObject jsonObject);

    @POST("api/User/Registration-by-google")
    Call<DataCheckSms> registrationByGoogle(@Body JsonObject jsonObject);

    @PUT("api/User")
    Call<Boolean> updateUser(@Body JsonObject jsonObject);
}
