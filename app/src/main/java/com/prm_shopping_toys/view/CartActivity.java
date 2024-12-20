package com.prm_shopping_toys.view;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.prm_shopping_toys.R;
import com.prm_shopping_toys.databinding.ActivityCartBinding;
import com.prm_shopping_toys.model.Cart;
import com.prm_shopping_toys.presenter.CartPresenter;
import com.prm_shopping_toys.presenter.OrderPresenter;

import java.io.File;
import java.text.DecimalFormat;
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

        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        cartPresenter = new CartPresenter(this);
        orderPresenter = new OrderPresenter(this);

        int userId = getCurrentUserId();
        cartAdapter = new CartAdapter(this, cartList, cartPresenter, userId, this::updateTotal);
        binding.cartListView.setAdapter(cartAdapter);
        loadCartItems();

        binding.checkoutButton.setOnClickListener(v -> {
            if (!cartList.isEmpty()) {
                checkout();
            } else {
                Toast.makeText(this, "Giỏ hàng của bạn trống!", Toast.LENGTH_SHORT).show();
            }
        });
        setupBottomNavigation();
        binding.bottomNavigation.setSelectedItemId(R.id.navigation_cart);
    }

    private void setupBottomNavigation() {
        binding.bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.navigation_home) {
                    if (!CartActivity.this.getClass().equals(HomeActivity.class)) {
                        Intent intent = new Intent(CartActivity.this, HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    }
                    return true;
                } else if (itemId == R.id.navigation_cart) {
                    if (!CartActivity.this.getClass().equals(CartActivity.class)) {
                        Intent intent = new Intent(CartActivity.this, CartActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                    return true;
                } else if (itemId == R.id.navigation_profile) {
                    if (!CartActivity.this.getClass().equals(UserInfoActivity.class)) {
                        Intent intent = new Intent(CartActivity.this, UserInfoActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                    return true;
                }
                return false;
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
                    updateTotal();
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

    private void updateTotal() {
        int totalToys = 0;
        double totalPrice = 0.0;

        for (Cart item : cartList) {
            totalToys += item.getQuantity();
            totalPrice += item.getQuantity() * item.getToy().getPrice();
        }

        DecimalFormat formatter = new DecimalFormat("#,###");
        String formattedPrice = formatter.format(totalPrice).replace(",", ".");

        binding.totalToyCount.setText(totalToys + "");
        binding.totalPriceAmount.setText(formattedPrice + " VNĐ");
    }

    private void checkout() {
        String userName = getCurrentUserName();
        double totalPrice = calculateTotalPrice(cartList);
        String filePath = orderPresenter.createOrderBill(userName, totalPrice, cartList);
        if (filePath != null) {
            cartPresenter.clearCart(getCurrentUserId(), new CartPresenter.CartCallback() {
                @Override
                public void onSuccess(List<Cart> cartItems) {
                    cartList.clear();
                    cartAdapter.notifyDataSetChanged();
                    updateTotal();
                    openPDF(new File(filePath));
                }

                @Override
                public void onFailure(String errorMessage) {
                    Toast.makeText(CartActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Lỗi khi tạo hóa đơn PDF", Toast.LENGTH_SHORT).show();
        }
    }

    private void openPDF(File file) {
        Uri fileUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".fileprovider", file);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(fileUri, "application/pdf");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            startActivity(Intent.createChooser(intent, "Open Bill PDF"));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No application available to open PDF", Toast.LENGTH_SHORT).show();
        }
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
