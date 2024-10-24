package com.prm_shopping_toys.api;

import com.prm_shopping_toys.model.Cart;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface CartAPI {
    @FormUrlEncoded
    @POST("add_to_cart.php")
    Call<ResponseBody> addToCart(
            @Field("user_id") int userId,
            @Field("toy_id") int toyId,
            @Field("quantity") int quantity
    );

    @GET("get_cart_items.php")
    Call<List<Cart>> getCartItems(
            @Query("user_id") int userId
    );

    @FormUrlEncoded
    @POST("remove_from_cart.php")
    Call<ResponseBody> removeFromCart(
            @Field("user_id") int userId,
            @Field("toy_id") int toyId
    );

    @FormUrlEncoded
    @POST("clear_cart.php")
    Call<ResponseBody> clearCart(
            @Field("user_id") int userId
    );

    @FormUrlEncoded
    @POST("update_quantity.php")
    Call<ResponseBody> updateCartQuantity(
            @Field("user_id") int userId,
            @Field("toy_id") int toyId,
            @Field("quantity") int quantity
    );
}
