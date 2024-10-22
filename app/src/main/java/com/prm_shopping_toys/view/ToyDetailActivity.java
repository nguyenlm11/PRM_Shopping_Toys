package com.prm_shopping_toys.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.prm_shopping_toys.R;
import com.prm_shopping_toys.databinding.ActivityToyDetailBinding;
import com.prm_shopping_toys.presenter.UserPresenter;
import com.prm_shopping_toys.utils.ApiClient;
import com.prm_shopping_toys.api.CartAPI;

import java.text.DecimalFormat;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ToyDetailActivity extends AppCompatActivity {
    private ActivityToyDetailBinding binding;
    private UserPresenter userPresenter;
    private int quantity = 1; // Default quantity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize View Binding
        binding = ActivityToyDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize UserPresenter
        userPresenter = new UserPresenter(null, this);

        // Lấy dữ liệu truyền từ HomeActivity
        String imageUrl = getIntent().getStringExtra("toy_image");
        String name = getIntent().getStringExtra("toy_name");
        double price = getIntent().getDoubleExtra("toy_price", 0.0);
        String category = getIntent().getStringExtra("toy_category");
        String description = getIntent().getStringExtra("toy_description");
        int toyId = getIntent().getIntExtra("toy_id", -1);
        int userId = userPresenter.getCurrentUserId();

        // Kiểm tra nếu các giá trị null và xử lý
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.placeholder_image)
                    .into(binding.toyDetailImage);
        }

        DecimalFormat formatter = new DecimalFormat("#,###");
        String formattedPrice = formatter.format(price).replace(",", ".");
        binding.toyDetailName.setText(name != null ? name : "No Name Available");
        binding.toyDetailPrice.setText(String.format("%s VNĐ", formattedPrice));
        binding.toyDetailCategory.setText(category != null ? category : "Unknown");
        binding.toyDetailDescription.setText(description != null ? description : "No Description Available");

        // Set up quantity buttons
        binding.increaseQuantityButton.setOnClickListener(v -> {
            quantity++;
            updateQuantityDisplay();
        });

        binding.decreaseQuantityButton.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                updateQuantityDisplay();
            }
        });

        // Set up the add to cart button click listener
        binding.addToCartButton.setOnClickListener(v -> addToCart(userId, toyId, quantity));
    }

    private void updateQuantityDisplay() {
        binding.quantityTextView.setText(String.valueOf(quantity));
    }

    private void addToCart(int userId, int toyId, int quantity) {
        CartAPI apiService = ApiClient.getClient().create(CartAPI.class);
        Call<ResponseBody> call = apiService.addToCart(userId, toyId, quantity);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ToyDetailActivity.this, "Added to cart successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ToyDetailActivity.this, "Failed to add to cart", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ToyDetailActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Clean up binding
        binding = null;
    }
}
