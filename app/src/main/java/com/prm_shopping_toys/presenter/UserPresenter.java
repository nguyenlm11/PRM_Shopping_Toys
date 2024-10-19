package com.prm_shopping_toys.presenter;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.prm_shopping_toys.api.UserApi;
import com.prm_shopping_toys.utils.ApiClient;
import com.prm_shopping_toys.view.UserView;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserPresenter {
    private UserView view;
    private Context context;
    private UserApi userApi;

    public UserPresenter(UserView view, Context context) {
        this.view = view;
        this.context = context;
        this.userApi = ApiClient.getClient().create(UserApi.class);
    }

    public void signup(String username, String password) {
        userApi.signupUser(username, password).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String result = response.body().string();
                        Log.d(TAG, "Signup Response: " + result);
                        JSONObject jsonObject = new JSONObject(result);
                        String status = jsonObject.getString("status");
                        if (status.equals("success")) {
                            view.onSignupSuccess(result);
                        } else {
                            String message = jsonObject.getString("message");
                            view.onError(message);
                        }
                    } catch (Exception e) {
                        view.onError(e.getMessage());
                    }
                } else {
                    view.onError("Failed to signup");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                view.onError(t.getMessage());
            }
        });
    }

    public void login(String username, String password) {
        userApi.loginUser(username, password).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String result = response.body().string();
                        JSONObject jsonObject = new JSONObject(result);
                        String status = jsonObject.getString("status");
                        if (status.equals("success")) {
                            int userId = jsonObject.getInt("user_id");
                            saveUserId(userId);
                            view.onLoginSuccess(result);
                        } else {
                            String message = jsonObject.getString("message");
                            view.onError(message);
                        }
                    } catch (Exception e) {
                        view.onError(e.getMessage());
                    }
                } else {
                    view.onError("Login failed");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                view.onError(t.getMessage());
            }
        });
    }

    private void saveUserId(int userId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("user_id", userId);
        editor.apply();
    }

    public int getCurrentUserId() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("user_id", -1);
    }
}
