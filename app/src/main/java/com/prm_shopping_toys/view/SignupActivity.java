package com.prm_shopping_toys.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.prm_shopping_toys.databinding.ActivitySignupBinding;
import com.prm_shopping_toys.presenter.UserPresenter;

public class SignupActivity extends AppCompatActivity implements UserView {

    private ActivitySignupBinding binding;
    private UserPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize View Binding
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        presenter = new UserPresenter(this, this);

        binding.signupButton.setOnClickListener(v -> {
            String username = binding.username.getText().toString();
            String password = binding.password.getText().toString();
            String confirmPassword = binding.confirmPassword.getText().toString();

            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }
            presenter.signup(username, password);
        });
    }

    @Override
    public void onSignupSuccess(String response) {
        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
        startActivity(intent);
        Toast.makeText(this, "Signup successful!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoginSuccess(String response) {
    }

    @Override
    public void onError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
