package com.prm_shopping_toys.view;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.prm_shopping_toys.R;
import com.prm_shopping_toys.api.UserApi;
import com.prm_shopping_toys.model.UserResponse;
import com.prm_shopping_toys.utils.ApiClient;

import java.util.Calendar;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {

    private EditText edtEmail, edtPhone;
    private TextView edtBirthday;
    private Button btnSave;
    private String selectedBirthday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        edtEmail = findViewById(R.id.edtEmail);
        edtPhone = findViewById(R.id.edtPhone);
        edtBirthday = findViewById(R.id.edtBirthday);
        btnSave = findViewById(R.id.btnSave);

        loadUserData();

        // Thiết lập OnClickListener cho TextView để mở DatePicker
        edtBirthday.setOnClickListener(v -> showDatePickerDialog());

        btnSave.setOnClickListener(view -> updateUserProfile());
    }

    private void showDatePickerDialog() {
        // Lấy ngày hiện tại
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Hiển thị DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDay) -> {
            selectedBirthday = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay; // Tháng bắt đầu từ 0
            edtBirthday.setText(selectedBirthday);
        }, year, month, day);
        datePickerDialog.show();
    }

    private void loadUserData() {
        int userId = getCurrentUserId();
        UserApi userApi = ApiClient.getClient().create(UserApi.class);
        Call<UserResponse> call = userApi.getUserInfo(userId);

        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().hasError()) {
                    Map<String, String> user = response.body().getUser();
                    edtEmail.setText(user.get("email"));
                    edtPhone.setText(user.get("phone"));
                    edtBirthday.setText(user.get("birth_date"));
                } else {
                    Toast.makeText(EditProfileActivity.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(EditProfileActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUserProfile() {
        int userId = getCurrentUserId();
        String email = edtEmail.getText().toString();
        String phone = edtPhone.getText().toString();
        String birthday = selectedBirthday != null ? selectedBirthday : edtBirthday.getText().toString(); // Lấy ngày đã chọn

        UserApi userApi = ApiClient.getClient().create(UserApi.class);
        Call<ResponseBody> call = userApi.updateUserProfile(userId, email, phone, birthday);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EditProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    Intent intent = new Intent(EditProfileActivity.this, UserInfoActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(EditProfileActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(EditProfileActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int getCurrentUserId() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("user_id", -1);
    }
}
