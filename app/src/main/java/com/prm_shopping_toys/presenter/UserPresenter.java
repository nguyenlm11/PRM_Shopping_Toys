package com.prm_shopping_toys.presenter;

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
    private UserApi userApi;

    public UserPresenter(UserView view) {
        this.view = view;
        this.userApi = ApiClient.getClient().create(UserApi.class);
    }

    public void signup(String username, String password) {
        userApi.signupUser(username, password).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String result = response.body().string();
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
}
