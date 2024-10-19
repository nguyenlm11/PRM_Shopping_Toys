package com.prm_shopping_toys.view;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.prm_shopping_toys.R;
import com.prm_shopping_toys.model.Category;
import com.prm_shopping_toys.model.Toy;
import com.prm_shopping_toys.presenter.CategoryPresenter;
import com.prm_shopping_toys.presenter.ToyPresenter;

import java.util.ArrayList;
import java.util.List;

public class EditProductActivity extends AppCompatActivity implements ToyView, CategoryView {

    private EditText productNameEditText, productDescriptionEditText, productPriceEditText, productImageLinkEditText;
    private Spinner categorySpinner;
    private Button saveButton;
    private ToyPresenter presenter;
    private CategoryPresenter categoryPresenter;
    private int toyId;
    private int selectedCategoryId;  // Holds the current toy's category ID

    private List<Category> categoryList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        // Initialize views
        productNameEditText = findViewById(R.id.product_name);
        productDescriptionEditText = findViewById(R.id.product_description);
        productPriceEditText = findViewById(R.id.product_price);
        productImageLinkEditText = findViewById(R.id.product_image_link);
        categorySpinner = findViewById(R.id.category_spinner);
        saveButton = findViewById(R.id.save_button);

        presenter = new ToyPresenter(this);
        categoryPresenter = new CategoryPresenter(this);

        // Get data passed from ManageToyActivity
        toyId = getIntent().getIntExtra("toy_id", -1);
        productNameEditText.setText(getIntent().getStringExtra("toy_name"));
        productDescriptionEditText.setText(getIntent().getStringExtra("toy_description"));
        productPriceEditText.setText(String.valueOf(getIntent().getDoubleExtra("toy_price", 0.0)));
        productImageLinkEditText.setText(getIntent().getStringExtra("toy_image"));
        selectedCategoryId = getIntent().getIntExtra("toy_category", 2);

        // Load categories and set Spinner selection
        categoryPresenter.getCategories();

        // Save updated toy information
        saveButton.setOnClickListener(v -> {
            String name = productNameEditText.getText().toString();
            String description = productDescriptionEditText.getText().toString();
            String price = productPriceEditText.getText().toString();
            String imageUrl = productImageLinkEditText.getText().toString();

            if (name.isEmpty() || description.isEmpty() || price.isEmpty() || imageUrl.isEmpty() || selectedCategoryId == -1) {
                Toast.makeText(this, "All fields and category are required", Toast.LENGTH_SHORT).show();
            } else {
                presenter.updateToy(toyId, name, description, price, imageUrl, selectedCategoryId);
            }
        });
    }

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
            categorySpinner.setAdapter(adapter);

            categorySpinner.post(() -> {
                for (int i = 0; i < categories.size(); i++) {
                    if (categories.get(i).getId() == selectedCategoryId) {
                        categorySpinner.setSelection(i);
                        break;
                    }
                }
            });

            // Handle category selection
            categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedCategoryId = categoryList.get(position).getId();  // Store selected category ID
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    selectedCategoryId = -1;  // No category selected
                }
            });
        } else {
            Toast.makeText(this, "No categories found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onToysLoaded(List<Toy> toys) {
        // Not needed for this activity
    }

    @Override
    public void onToyDeleted(String response) {
        // Not needed for this activity
    }

    @Override
    public void onAddProductSuccess(String response) {
        // Not needed for this activity
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
    public void onToyUpdated(String response) {
        Toast.makeText(this, "Toy updated successfully", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }
}
