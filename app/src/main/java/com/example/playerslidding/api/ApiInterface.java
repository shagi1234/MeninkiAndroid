package com.example.playerslidding.api;

import com.example.playerslidding.data.CategoryDto;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface ApiInterface {

    @GET("/api/Category")
    Call<ArrayList<CategoryDto>> getAllCategory();
}
