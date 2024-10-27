package com.prm_shopping_toys.api;

import com.prm_shopping_toys.model.UserResponse;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

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

    @GET("get_user_info.php")
    Call<UserResponse> getUserInfo(
            @Query("user_id") int userId
    );

    @FormUrlEncoded
    @POST("edit_profile.php")
    Call<ResponseBody> updateUserProfile(
            @Field("user_id") int userId,
            @Field("email") String email,
            @Field("phone") String phone,
            @Field("birthday") String birthday
    );

    @FormUrlEncoded
    @POST("change_password.php")
    Call<ResponseBody> changePassword(
            @Field("user_id") int userId,
            @Field("current_password") String currentPassword,
            @Field("new_password") String newPassword
    );
}
