package com.prm_shopping_toys.presenter;

import com.prm_shopping_toys.api.ToyApi;
import com.prm_shopping_toys.model.Toy;
import com.prm_shopping_toys.view.ToyView;
import com.prm_shopping_toys.utils.ApiClient;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ToyPresenter {
    private ToyView view;
    private ToyApi toyApi;

    public ToyPresenter(ToyView view) {
        this.view = view;
        this.toyApi = ApiClient.getClient().create(ToyApi.class);
    }

    public void getToys() {
        toyApi.getToys().enqueue(new Callback<List<Toy>>() {
            @Override
            public void onResponse(Call<List<Toy>> call, Response<List<Toy>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    view.onToysLoaded(response.body());
                } else {
                    view.onError("Failed to load toys");
                }
            }

            @Override
            public void onFailure(Call<List<Toy>> call, Throwable t) {
                view.onError(t.getMessage());
            }
        });
    }

    public void deleteToy(int toyId) {
        toyApi.deleteToy(toyId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    view.onToyDeleted("Success");
                } else {
                    view.onError("Failed to delete toy");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                view.onError(t.getMessage());
            }
        });
    }

    public void addProductWithImage(String name, String description, String price, String imageUrl, int categoryId) {
        toyApi.addProductWithImage(name, description, price, imageUrl, categoryId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String result = response.body().string();
                        view.onAddProductSuccess(result);
                    } catch (Exception e) {
                        view.onError(e.getMessage());
                    }
                } else {
                    view.onError("Failed to add product");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                view.onError(t.getMessage());
            }
        });
    }

    public void updateToy(int toyId, String name, String description, String price, String imageUrl, int categoryId) {
        toyApi.updateToy(toyId, name, description, price, imageUrl, categoryId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    view.onToyUpdated("Toy updated successfully");
                } else {
                    view.onError("Failed to update toy");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                view.onError(t.getMessage());
            }
        });
    }
}
