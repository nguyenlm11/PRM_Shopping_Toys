package com.prm_shopping_toys.presenter;

import com.prm_shopping_toys.api.CustomerApi;
import com.prm_shopping_toys.model.User;
import com.prm_shopping_toys.utils.ApiClient;
import com.prm_shopping_toys.view.CustomerView;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerPresenter {
    private CustomerView view;
    private CustomerApi customerApi;

    public CustomerPresenter(CustomerView view) {
        this.view = view;
        this.customerApi = ApiClient.getClient().create(CustomerApi.class);
    }

    public void getCustomers() {
        customerApi.getCustomers().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    view.onCustomersLoaded(response.body());
                } else {
                    view.onError("Failed to load customers");
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                view.onError(t.getMessage());
            }
        });
    }

    public void updateCustomerStatus(int customerId, String status) {
        customerApi.updateCustomerStatus(customerId, status).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        view.onCustomerStatusUpdated("Change status successfully");
                    } catch (Exception e) {
                        view.onError(e.getMessage());
                    }
                } else {
                    view.onError("Failed to update customer status");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                view.onError(t.getMessage());
            }
        });
    }
}
