package com.prm_shopping_toys.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.prm_shopping_toys.R;
import com.prm_shopping_toys.api.UserApi;
import com.prm_shopping_toys.model.User;
import com.prm_shopping_toys.utils.ApiClient;
import com.prm_shopping_toys.databinding.ActivityChangePasswordBinding;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {

    private ActivityChangePasswordBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangePasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndChangePassword();
            }
        });

        binding.goBack.setOnClickListener(v -> {
            Intent intent = new Intent(ChangePasswordActivity.this, UserInfoActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });
    }

    private void validateAndChangePassword() {
        String currentPassword = binding.etCurrentPassword.getText().toString().trim();
        String newPassword = binding.etNewPassword.getText().toString().trim();
        String confirmPassword = binding.etConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(currentPassword) || TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        // Giả sử bạn đã có một đối tượng User với thông tin đã được lưu trong SharedPreferences
        User currentUser = getCurrentUser();

        if (currentUser == null) {
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!passwordMatches(currentUser.getPassword(), currentPassword)) {
            Toast.makeText(this, "Current password is incorrect", Toast.LENGTH_SHORT).show();
            return;
        }

        if (newPassword.equals(currentPassword)) {
            Toast.makeText(this, "New password cannot be the same as the current password", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        changePassword(currentPassword, newPassword);
    }

    private void changePassword(String currentPassword, String newPassword) {
        UserApi userService = ApiClient.getClient().create(UserApi.class);
        int userId = getCurrentUserId();

        Call<ResponseBody> call = userService.changePassword(userId, currentPassword, newPassword);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ChangePasswordActivity.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                    logoutAndRedirectToLogin(); // Logout user after password change
                } else {
                    Toast.makeText(ChangePasswordActivity.this, "Failed to change password", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ChangePasswordActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void logoutAndRedirectToLogin() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private int getCurrentUserId() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("user_id", -1);
    }

    // Giả sử bạn đã lưu thông tin người dùng vào SharedPreferences
    private User getCurrentUser() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        int userId = sharedPreferences.getInt("user_id", -1);
        String password = sharedPreferences.getString("password", null);
        return new User(userId, password);
    }

    private boolean passwordMatches(String storedPassword, String currentPassword) {
        return currentPassword.equals(storedPassword);
    }
}
