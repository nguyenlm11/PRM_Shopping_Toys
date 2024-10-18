package com.prm_shopping_toys.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.prm_shopping_toys.databinding.ActivityLoginBinding;
import com.prm_shopping_toys.presenter.UserPresenter;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements UserView {

    private ActivityLoginBinding binding;
    private UserPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize View Binding
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        presenter = new UserPresenter(this, this);

        // Login button click listener
        binding.loginButton.setOnClickListener(v -> {
            String username = binding.username.getText().toString();
            String password = binding.password.getText().toString();
            presenter.login(username, password);
        });

        // Signup button click listener
        binding.signupLink.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onLoginSuccess(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            String role = jsonObject.getString("role");

            if (role.equals("admin")) {
                Intent intent = new Intent(LoginActivity.this, ManageActivity.class);
                startActivity(intent);
                Toast.makeText(this, "Welcome, Admin!", Toast.LENGTH_SHORT).show();
            } else if (role.equals("customer")) {
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
                Toast.makeText(this, "Welcome, Customer!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error parsing response", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSignupSuccess(String response) {
    }

    @Override
    public void onError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
