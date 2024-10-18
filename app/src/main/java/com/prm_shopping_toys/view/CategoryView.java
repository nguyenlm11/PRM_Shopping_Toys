package com.prm_shopping_toys.view;

import com.prm_shopping_toys.model.Category;

import java.util.List;

public interface CategoryView {
    void onCategoriesLoaded(List<Category> categories);

    void onCategoryAddedSuccess(String response);

    void onCategoryDeleted(String response);

    void onError(String message);
}
