package com.prm_shopping_toys.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.prm_shopping_toys.databinding.ActivityAddCategoryBinding;
import com.prm_shopping_toys.model.Category;
import com.prm_shopping_toys.presenter.CategoryPresenter;

import java.util.List;

public class AddCategoryActivity extends AppCompatActivity implements CategoryView {

    private ActivityAddCategoryBinding binding;
    private CategoryPresenter categoryPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize View Binding
        binding = ActivityAddCategoryBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // Initialize presenter
        categoryPresenter = new CategoryPresenter(this);

        // Handle Add Category button click
        binding.addCategoryButton.setOnClickListener(v -> {
            String categoryName = binding.categoryNameInput.getText().toString();

            if (categoryName.isEmpty()) {
                Toast.makeText(AddCategoryActivity.this, "Category name cannot be empty", Toast.LENGTH_SHORT).show();
            } else {
                categoryPresenter.addCategory(categoryName);
            }
        });
    }

    // Called when category is added successfully
    @Override
    public void onCategoryAddedSuccess(String response) {
        Toast.makeText(this, "Category added successfully", Toast.LENGTH_SHORT).show();

        // Set result and finish to return to ManageCategoryActivity
        setResult(RESULT_OK, new Intent());
        finish();
    }

    // Handle errors
    @Override
    public void onError(String message) {
        Toast.makeText(this, "Error: " + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCategoriesLoaded(List<Category> categories) {
    }

    @Override
    public void onCategoryDeleted(String response) {
    }
}
