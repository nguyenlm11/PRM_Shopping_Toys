package com.prm_shopping_toys.api;

import com.prm_shopping_toys.model.Toy;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ToyApi {

    // Fetch all toys
    @GET("get_toy.php")
    Call<List<Toy>> getToys();

    // Add a toy with image and category
    @FormUrlEncoded
    @POST("add_product.php")
    Call<ResponseBody> addProductWithImage(
            @Field("name") String name,
            @Field("description") String description,
            @Field("price") String price,
            @Field("image") String imageUrl,
            @Field("category_id") int categoryId
    );

    //Search by name
    @GET("search_toy.php")
    Call<List<Toy>> searchToysByName(
            @Query("name") String name
    );

    // Delete a toy by ID
    @FormUrlEncoded
    @POST("delete_toy.php")
    Call<ResponseBody> deleteToy(
            @Field("id") int id
    );

    // Update a toy by ID
    @FormUrlEncoded
    @POST("update_toy.php")
    Call<ResponseBody> updateToy(
            @Field("id") int id,
            @Field("name") String name,
            @Field("description") String description,
            @Field("price") String price,
            @Field("image") String imageUrl,
            @Field("category_id") int categoryId
    );

    @GET("getToysByFilters.php")
    Call<List<Toy>> getToysByFilters(
            @Query("priceFilter") int priceFilter,
            @Query("categoryId") int categoryId
    );
}
