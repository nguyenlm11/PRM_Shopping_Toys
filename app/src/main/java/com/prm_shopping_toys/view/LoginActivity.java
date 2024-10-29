package com.prm_shopping_toys.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.prm_shopping_toys.R;
import com.prm_shopping_toys.databinding.ActivityLoginBinding;
import com.prm_shopping_toys.presenter.UserPresenter;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements UserView {

    private ActivityLoginBinding binding;
    private UserPresenter presenter;
    private BiometricPrompt biometricPrompt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Khởi tạo View Binding
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        presenter = new UserPresenter(this, this);

        // Khởi tạo BiometricPrompt
        initBiometricPrompt();

        // Sự kiện click cho nút đăng nhập
        binding.loginButton.setOnClickListener(v -> {
            String username = binding.username.getText().toString();
            String password = binding.password.getText().toString();
            presenter.login(username, password);
        });

        // Sự kiện click cho nút đăng ký
        binding.signupLink.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });

        binding.fingerprintButton.setOnClickListener(v -> showBiometricPrompt());
    }

    @Override
    public void onLoginSuccess(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            String status = jsonObject.getString("status");

            if (status.equals("success")) {
                int userId = jsonObject.getInt("user_id");
                String username = jsonObject.getString("username");
                String password = binding.password.getText().toString(); // Lấy mật khẩu từ EditText

                // Lưu ID, tên người dùng và mật khẩu vào SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("user_id", userId);
                editor.putString("username", username);
                editor.putString("password", password); // Lưu mật khẩu
                editor.putString("role", jsonObject.getString("role")); // Lưu vai trò người dùng
                editor.apply();

                String role = jsonObject.getString("role");
                if (role.equals("admin")) {
                    Intent intent = new Intent(LoginActivity.this, ManageActivity.class);
                    startActivity(intent);
                    Toast.makeText(this, "Welcome, Admin!", Toast.LENGTH_SHORT).show();
                    finish();
                } else if (role.equals("customer")) {
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                    Toast.makeText(this, "Welcome, Customer!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            } else {
                String message = jsonObject.getString("message");
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error parsing feedback", Toast.LENGTH_SHORT).show();
        }
    }

    private void initBiometricPrompt() {
        biometricPrompt = new BiometricPrompt(this,
                ContextCompat.getMainExecutor(this),
                new BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationError(int errorCode, CharSequence errString) {
                        Toast.makeText(LoginActivity.this, "Authentication error: " + errString, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                        proceedToNextActivity();
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        Toast.makeText(LoginActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showBiometricPrompt() {
        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Authentication required")
                .setSubtitle("Log in using your fingerprint")
                .setNegativeButtonText("Cancel")
                .build();

        biometricPrompt.authenticate(promptInfo);
    }

    private void proceedToNextActivity() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String role = sharedPreferences.getString("role", "customer");

        // So sánh với dữ liệu đã lưu
        Intent intent;
        if ("admin".equals(role)) {
            intent = new Intent(LoginActivity.this, ManageActivity.class);
            Toast.makeText(this, "Welcome, Admin!", Toast.LENGTH_SHORT).show();
        } else {
            intent = new Intent(LoginActivity.this, HomeActivity.class);
            Toast.makeText(this, "Welcome, Customer!", Toast.LENGTH_SHORT).show();
        }
        startActivity(intent);
        finish();
    }

    @Override
    public void onSignupSuccess(String response) {
    }

    @Override
    public void onError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
