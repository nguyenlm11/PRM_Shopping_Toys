package com.prm_shopping_toys.presenter;

import com.prm_shopping_toys.api.CategoryApi;
import com.prm_shopping_toys.model.Category;
import com.prm_shopping_toys.view.CategoryView;
import com.prm_shopping_toys.utils.ApiClient;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryPresenter {

    private CategoryView view;
    private CategoryApi categoryApi;

    public CategoryPresenter(CategoryView view) {
        this.view = view;
        this.categoryApi = ApiClient.getClient().create(CategoryApi.class);
    }

    // Get categories from API
    public void getCategories() {
        categoryApi.getCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    view.onCategoriesLoaded(response.body());
                } else {
                    view.onError("Failed to load categories");
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                view.onError(t.getMessage());
            }
        });
    }

    // Add a new category
    public void addCategory(String name) {
        categoryApi.addCategory(name).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    view.onCategoryAddedSuccess("Category added successfully");
                } else {
                    view.onError("Failed to add category");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                view.onError(t.getMessage());
            }
        });
    }

    // Delete category
    public void deleteCategory(int categoryId) {
        categoryApi.deleteCategory(categoryId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        view.onCategoryDeleted("Category deleted successfully");
                    } catch (Exception e) {
                        view.onError(e.getMessage());
                    }
                } else {
                    view.onError("Failed to delete category");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                view.onError(t.getMessage());
            }
        });
    }
}
