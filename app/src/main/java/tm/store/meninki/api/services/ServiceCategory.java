package tm.store.meninki.api.services;

import retrofit2.http.Query;
import tm.store.meninki.data.CategoryDto;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ServiceCategory {
    @GET("/api/Category")
    Call<ArrayList<CategoryDto>> getAllCategory(@Query("categoryType") int type);
}
