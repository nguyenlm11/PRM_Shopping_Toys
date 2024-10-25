package com.prm_shopping_toys.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.prm_shopping_toys.databinding.ActivityManageBinding;

public class ManageActivity extends AppCompatActivity {

    private ActivityManageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize View Binding
        binding = ActivityManageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Manage Customers View click event
        binding.manageCustomerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageActivity.this, ManageCustomerActivity.class);
                startActivity(intent);
            }
        });

        // Manage Toys View click event
        binding.manageToyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageActivity.this, ManageToyActivity.class);
                startActivity(intent);
            }
        });

        // Manage Categories View click event
        binding.manageCategoryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageActivity.this, ManageCategoryActivity.class);
                startActivity(intent);
            }
        });

        // Logout View click event
        binding.logoutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }
}
