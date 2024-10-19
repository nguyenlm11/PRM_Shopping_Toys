package com.prm_shopping_toys.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.prm_shopping_toys.databinding.ActivityAddProductBinding;
import com.prm_shopping_toys.model.Category;
import com.prm_shopping_toys.model.Toy;
import com.prm_shopping_toys.presenter.CategoryPresenter;
import com.prm_shopping_toys.presenter.ToyPresenter;

import java.util.ArrayList;
import java.util.List;

public class AddProductActivity extends AppCompatActivity implements ToyView, CategoryView {

    private ActivityAddProductBinding binding;
    private ToyPresenter toyPresenter;
    private CategoryPresenter categoryPresenter;

    private List<Category> categoryList = new ArrayList<>();
    private int selectedCategoryId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize View Binding
        binding = ActivityAddProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize presenters
        toyPresenter = new ToyPresenter(this);
        categoryPresenter = new CategoryPresenter(this);

        // Load categories into Spinner
        categoryPresenter.getCategories();

        // Save product when the button is clicked
        binding.saveProductButton.setOnClickListener(v -> {
            String name = binding.productName.getText().toString();
            String description = binding.productDescription.getText().toString();
            String price = binding.productPrice.getText().toString();
            String imageUrl = binding.productImageLink.getText().toString();

            if (name.isEmpty() || description.isEmpty() || price.isEmpty() || imageUrl.isEmpty() || selectedCategoryId == -1) {
                Toast.makeText(this, "All fields and category are required", Toast.LENGTH_SHORT).show();
                return;
            }
            toyPresenter.addProductWithImage(name, description, price, imageUrl, selectedCategoryId);
        });
    }

    // Handling category loading for Spinner
    @Override
    public void onCategoriesLoaded(List<Category> categories) {
        if (categories != null && !categories.isEmpty()) {
            categoryList = categories;
            List<String> categoryNames = new ArrayList<>();
            for (Category category : categories) {
                categoryNames.add(category.getName());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryNames);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.categorySpinner.setAdapter(adapter);

            // Handle category selection
            binding.categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedCategoryId = categoryList.get(position).getId();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    selectedCategoryId = -1;
                }
            });
        } else {
            Toast.makeText(this, "No categories found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAddProductSuccess(String response) {
        Toast.makeText(this, "Product added successfully", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onError(String message) {
        Toast.makeText(this, "Error: " + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCategoryAddedSuccess(String response) {
    }

    @Override
    public void onCategoryDeleted(String response) {
    }

    @Override
    public void onToysLoaded(List<Toy> toys) {
    }

    @Override
    public void onToyDeleted(String response) {
    }

    @Override
    public void onToyUpdated(String response) {
    }
}
