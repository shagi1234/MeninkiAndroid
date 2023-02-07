package com.example.playerslidding.api.services;

import com.example.playerslidding.data.CategoryDto;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ServiceCategory {

    @GET("/api/Category")
    Call<ArrayList<CategoryDto>> getAllCategory();
}
