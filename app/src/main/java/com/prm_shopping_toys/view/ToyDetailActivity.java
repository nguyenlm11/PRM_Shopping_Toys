package com.prm_shopping_toys.view;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.prm_shopping_toys.R;

public class ToyDetailActivity extends AppCompatActivity {
    private ImageView toyImageView;
    private TextView toyNameTextView;
    private TextView toyPriceTextView;
    private TextView toyCategoryTextView;
    private TextView toyDescriptionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toy_detail);

        // Khởi tạo các view
        toyImageView = findViewById(R.id.toy_detail_image);
        toyNameTextView = findViewById(R.id.toy_detail_name);
        toyPriceTextView = findViewById(R.id.toy_detail_price);
        toyCategoryTextView = findViewById(R.id.toy_detail_category);
        toyDescriptionTextView = findViewById(R.id.toy_detail_description);

        // Lấy dữ liệu truyền từ HomeActivity
        String imageUrl = getIntent().getStringExtra("toy_image");
        String name = getIntent().getStringExtra("toy_name");
        double price = getIntent().getDoubleExtra("toy_price", 0.0);
        String category = getIntent().getStringExtra("toy_category");
        String description = getIntent().getStringExtra("toy_description");

        // Kiểm tra nếu các giá trị null và xử lý
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.placeholder_image)
                    .into(toyImageView);
        }

        toyNameTextView.setText(name != null ? name : "No Name Available");
        toyPriceTextView.setText(String.format("%.0f VNĐ", price));
        toyCategoryTextView.setText("Category: " + (category != null ? category : "Unknown"));
        toyDescriptionTextView.setText(description != null ? description : "No Description Available");
    }
}
