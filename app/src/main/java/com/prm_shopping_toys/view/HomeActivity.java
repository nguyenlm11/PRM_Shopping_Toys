package com.prm_shopping_toys.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.prm_shopping_toys.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize View Binding
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Add your content logic for the home screen here
    }
}
