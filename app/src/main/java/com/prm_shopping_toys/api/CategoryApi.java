package com.prm_shopping_toys.api;

import com.shopping_toys.model.Category;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface CategoryApi {

    @FormUrlEncoded
    @POST("add_category.php")
    Call<ResponseBody> addCategory(
            @Field("name") String name
    );

    @GET("get_category.php")
    Call<List<Category>> getCategories();

    @FormUrlEncoded
    @POST("delete_category.php")
    Call<ResponseBody> deleteCategory(
            @Field("id") int id
    );
}
