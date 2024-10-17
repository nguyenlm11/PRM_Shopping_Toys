package com.prm_shopping_toys.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface UserApi {

    @FormUrlEncoded
    @POST("user_login.php")
    Call<ResponseBody> loginUser(
            @Field("username") String username,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("user_signup.php")
    Call<ResponseBody> signupUser(
            @Field("username") String username,
            @Field("password") String password
    );
}
