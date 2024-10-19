package com.prm_shopping_toys.api;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface OrderAPI {
    @Multipart
    @POST("create_order.php")
    Call<ResponseBody> createOrder(
            @Part("user_id") RequestBody userId,
            @Part("total_price") RequestBody totalPrice,
            @Part("items") RequestBody items,
            @Part MultipartBody.Part billPdf
    );
}
