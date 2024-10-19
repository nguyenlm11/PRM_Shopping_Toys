package com.prm_shopping_toys.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.prm_shopping_toys.databinding.ActivityCartBinding;
import com.prm_shopping_toys.model.Cart;
import com.prm_shopping_toys.presenter.CartPresenter;
import com.prm_shopping_toys.presenter.OrderPresenter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    private ActivityCartBinding binding;
    private CartPresenter cartPresenter;
    private OrderPresenter orderPresenter;
    private CartAdapter cartAdapter;
    private List<Cart> cartList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Kiểm tra quyền ghi file
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        cartPresenter = new CartPresenter(this);
        orderPresenter = new OrderPresenter(this);

        int userId = getCurrentUserId();
        cartAdapter = new CartAdapter(this, cartList, cartPresenter, userId);
        binding.cartListView.setAdapter(cartAdapter);

        loadCartItems();

        binding.checkoutButton.setOnClickListener(v -> {
            if (!cartList.isEmpty()) {
                checkout();
            } else {
                Toast.makeText(this, "Giỏ hàng của bạn trống!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadCartItems() {
        int userId = getCurrentUserId();
        if (userId != -1) {
            cartPresenter.getCartItems(userId, new CartPresenter.CartCallback() {
                @Override
                public void onSuccess(List<Cart> cartItems) {
                    cartList.clear();
                    cartList.addAll(cartItems);
                    cartAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(String errorMessage) {
                    Toast.makeText(CartActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkout() {
        String userName = getCurrentUserName();
        double totalPrice = calculateTotalPrice(cartList);

        // Truyền danh sách Cart đến OrderPresenter
        String filePath = orderPresenter.createOrderBill(userName, totalPrice, cartList);
        if (filePath != null) {
            openPDF(filePath);
        } else {
            Toast.makeText(this, "Lỗi khi tạo hóa đơn PDF", Toast.LENGTH_SHORT).show();
        }
    }

    private void openPDF(String filePath) {
        File file = new File(filePath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(Intent.createChooser(intent, "Open Bill PDF"));
    }

    private double calculateTotalPrice(List<Cart> cartList) {
        double total = 0;
        for (Cart item : cartList) {
            total += item.getQuantity() * item.getToy().getPrice();
        }
        return total;
    }

    private int getCurrentUserId() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("user_id", -1);
    }

    private String getCurrentUserName() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("username", "Unknown");
    }
}
