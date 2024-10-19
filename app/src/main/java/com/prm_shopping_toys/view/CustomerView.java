package com.prm_shopping_toys.view;

import com.prm_shopping_toys.model.User;

import java.util.List;

public interface CustomerView {
    void onCustomersLoaded(List<User> customers);

    void onCustomerStatusUpdated(String response);

    void onError(String message);
}
