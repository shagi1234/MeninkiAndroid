package com.example.playerslidding.api.services;

import com.example.playerslidding.api.data.DataCheckSms;
import com.example.playerslidding.api.data.DataSendSms;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ServiceLogin {
    @POST("/api/User/Registration")
    Call<DataSendSms> sendSms(@Body JsonObject jsonObject);

    @POST("/api/User/ConfirmationPhoneNumber")
    Call<DataCheckSms> checkSms(@Body JsonObject jsonObject);
}
