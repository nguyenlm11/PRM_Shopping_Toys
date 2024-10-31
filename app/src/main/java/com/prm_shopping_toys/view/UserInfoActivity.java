package com.prm_shopping_toys.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.prm_shopping_toys.api.UserApi;
import com.prm_shopping_toys.model.UserResponse;
import com.prm_shopping_toys.utils.ApiClient;
import com.prm_shopping_toys.R;
import com.prm_shopping_toys.databinding.ActivityProfileBinding; // Import View Binding

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserInfoActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Khởi tạo View Binding
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserInfoActivity.this, EditProfileActivity.class);
                intent.putExtra("user_id", getCurrentUserId());
                startActivityForResult(intent, 1);
            }
        });

        binding.changePasswordLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserInfoActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        binding.navigationLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

        setupBottomNavigation();
        binding.bottomNavigation.setSelectedItemId(R.id.navigation_profile);

        int userId = getCurrentUserId();

        if (userId != -1) {
            getUserInfo(userId);
        } else {
            Toast.makeText(this, "Invalid User ID", Toast.LENGTH_SHORT).show();
        }
    }

    private void getUserInfo(int userId) {
        UserApi userService = ApiClient.getClient().create(UserApi.class);
        Call<UserResponse> call = userService.getUserInfo(userId);

        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserResponse userResponse = response.body();
                    if (userResponse.hasError()) {
                        Toast.makeText(UserInfoActivity.this, userResponse.getError(), Toast.LENGTH_SHORT).show();
                    } else {
                        binding.txtUsername.setText(userResponse.getUser().get("username"));
                        String email = userResponse.getUser().get("email");
                        binding.txtEmail.setText(email != null ? email : "No data");
                        String phone = userResponse.getUser().get("phone");
                        binding.txtPhone.setText(phone != null ? phone : "No data");
                        String birthday = userResponse.getUser().get("birth_date");
                        binding.txtBirthday.setText(birthday != null ? birthday : "No data");
                        binding.txtOrderCount.setText(String.valueOf(userResponse.getOrderCount()));
                    }
                } else {
                    Toast.makeText(UserInfoActivity.this, "Failed to retrieve user info", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(UserInfoActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupBottomNavigation() {
        binding.bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.navigation_home) {
                    if (!UserInfoActivity.this.getClass().equals(HomeActivity.class)) {
                        Intent intent = new Intent(UserInfoActivity.this, HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    }
                    return true;
                } else if (itemId == R.id.navigation_cart) {
                    if (!UserInfoActivity.this.getClass().equals(CartActivity.class)) {
                        Intent intent = new Intent(UserInfoActivity.this, CartActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    }
                    return true;
                } else if (itemId == R.id.navigation_profile) {
                    if (!UserInfoActivity.this.getClass().equals(UserInfoActivity.class)) {
                        Intent intent = new Intent(UserInfoActivity.this, UserInfoActivity.class);
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

    private void logoutUser() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        Intent intent = new Intent(UserInfoActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private int getCurrentUserId() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("user_id", -1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            getUserInfo(getCurrentUserId());
        }
    }
}
