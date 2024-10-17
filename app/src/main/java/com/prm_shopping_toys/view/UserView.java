package com.prm_shopping_toys.view;

public interface UserView {
    void onSignupSuccess(String response);

    void onLoginSuccess(String response);

    void onError(String message);

}
