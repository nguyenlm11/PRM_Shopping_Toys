package com.prm_shopping_toys.api;

import com.prm_shopping_toys.model.User;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface CustomerApi {
    @GET("get_customers.php")
    Call<List<User>> getCustomers();

    @FormUrlEncoded
    @POST("update_customer_status.php")
    Call<ResponseBody> updateCustomerStatus(
            @Field("id") int id,
            @Field("status") String status
    );
}
