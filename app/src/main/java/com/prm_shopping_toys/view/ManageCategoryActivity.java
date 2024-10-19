package com.prm_shopping_toys.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.prm_shopping_toys.databinding.ActivityManageCategoryBinding;
import com.prm_shopping_toys.model.Category;
import com.prm_shopping_toys.presenter.CategoryPresenter;

import java.util.ArrayList;
import java.util.List;

public class ManageCategoryActivity extends AppCompatActivity implements CategoryView {

    private ActivityManageCategoryBinding binding;
    private CategoryAdapter categoryAdapter;
    private CategoryPresenter categoryPresenter;
    private List<Category> categoryList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize View Binding
        binding = ActivityManageCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize ListView and Adapter
        categoryAdapter = new CategoryAdapter(this, categoryList, category -> {
            confirmDeleteCategory(category);
        });
        binding.categoryListView.setAdapter(categoryAdapter);

        // Handle Add Category button click
        binding.addCategoryButton.setOnClickListener(v -> {
            Intent intent = new Intent(ManageCategoryActivity.this, AddCategoryActivity.class);
            startActivityForResult(intent, 1);
        });

        // Initialize presenter and fetch categories
        categoryPresenter = new CategoryPresenter(this);
        categoryPresenter.getCategories();
    }

    // Confirm and delete category
    private void confirmDeleteCategory(Category category) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Category")
                .setMessage("Are you sure you want to delete this category?")
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    categoryPresenter.deleteCategory(category.getId());
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    // Refresh category list after adding a new category
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            categoryPresenter.getCategories();
        }
    }

    @Override
    public void onCategoriesLoaded(List<Category> categories) {
        categoryList.clear();
        categoryList.addAll(categories);
        categoryAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCategoryDeleted(String response) {
        Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
        categoryPresenter.getCategories();
    }

    @Override
    public void onError(String message) {
        Toast.makeText(this, "Error: " + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCategoryAddedSuccess(String response) {
    }
}
