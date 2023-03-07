package tm.store.meninki.api.services;

import retrofit2.http.PUT;
import tm.store.meninki.api.response.DataCheckSms;
import tm.store.meninki.api.response.DataSendSms;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ServiceLogin {
    @POST("/api/User/Registration")
    Call<DataSendSms> sendSms(@Body JsonObject jsonObject);

    @POST("/api/User/ConfirmationPhoneNumber")
    Call<DataCheckSms> checkSms(@Body JsonObject jsonObject);

    @POST("api/User/Registration-by-google")
    Call<DataCheckSms> signInGoogle(@Body JsonObject jsonObject);

    @PUT("api/User")
    Call<Boolean> updateUser(@Body JsonObject jsonObject);
}
