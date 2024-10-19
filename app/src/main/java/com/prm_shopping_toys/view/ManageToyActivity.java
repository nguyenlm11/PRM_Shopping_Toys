package com.prm_shopping_toys.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.prm_shopping_toys.databinding.ActivityManageToyBinding;
import com.prm_shopping_toys.model.Toy;
import com.prm_shopping_toys.presenter.ToyPresenter;

import java.util.ArrayList;
import java.util.List;

public class ManageToyActivity extends AppCompatActivity implements ToyView {

    private static final int ADD_PRODUCT_REQUEST_CODE = 1;
    private static final int EDIT_PRODUCT_REQUEST_CODE = 2; // Request code for editing
    private ActivityManageToyBinding binding;
    private ToyAdapter toyAdapter;
    private ToyPresenter presenter;
    private List<Toy> toyList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize View Binding
        binding = ActivityManageToyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set up ListView and Adapter
        toyAdapter = new ToyAdapter(this, toyList, this::confirmDeleteToy, toy -> {
            // Handle edit button click
            Intent intent = new Intent(ManageToyActivity.this, EditProductActivity.class);
            intent.putExtra("toy_id", toy.getId());
            intent.putExtra("toy_name", toy.getName());
            intent.putExtra("toy_description", toy.getDescription());
            intent.putExtra("toy_price", toy.getPrice());
            intent.putExtra("toy_image", toy.getImage());
            intent.putExtra("toy_category", toy.getCategoryId());
            startActivityForResult(intent, EDIT_PRODUCT_REQUEST_CODE);  // Start EditProductActivity
        });
        binding.toyListView.setAdapter(toyAdapter);

        binding.addToyButton.setOnClickListener(v -> {
            Intent intent = new Intent(ManageToyActivity.this, AddProductActivity.class);
            startActivityForResult(intent, ADD_PRODUCT_REQUEST_CODE);
        });

        // Initialize presenter and load toy list
        presenter = new ToyPresenter(this);
        presenter.getToys();
    }

    // Confirm deletion with a dialog
    private void confirmDeleteToy(Toy toy) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Toy")
                .setMessage("Are you sure you want to delete this toy?")
                .setPositiveButton("Yes", (dialog, which) -> presenter.deleteToy(toy.getId())) // Delete toy
                .setNegativeButton("No", null)
                .show();
    }

    // Refresh the toy list after adding or editing a toy
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == ADD_PRODUCT_REQUEST_CODE || requestCode == EDIT_PRODUCT_REQUEST_CODE) && resultCode == RESULT_OK) {
            // Reload the toy list after add or edit
            presenter.getToys();
        }
    }

    @Override
    public void onToysLoaded(List<Toy> toys) {
        toyList.clear();
        toyList.addAll(toys);
        toyAdapter.notifyDataSetChanged();
    }

    @Override
    public void onToyDeleted(String response) {
        Toast.makeText(this, "Toy deleted successfully", Toast.LENGTH_SHORT).show();
        presenter.getToys();
    }

    @Override
    public void onError(String message) {
        Toast.makeText(this, "Error: " + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAddProductSuccess(String response) {
    }

    @Override
    public void onToyUpdated(String response) {
        // Optional: handle toy update success
    }
}

