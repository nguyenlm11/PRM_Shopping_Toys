package com.prm_shopping_toys.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.prm_shopping_toys.R;
import com.prm_shopping_toys.databinding.ActivityHomeBinding;
import com.prm_shopping_toys.model.Category;
import com.prm_shopping_toys.model.Toy;
import com.prm_shopping_toys.presenter.CartPresenter;
import com.prm_shopping_toys.presenter.CategoryPresenter;
import com.prm_shopping_toys.presenter.ToyPresenter;
import com.prm_shopping_toys.presenter.UserPresenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity implements ToyView, CategoryView {

    private ActivityHomeBinding binding;
    private ToyPresenter toyPresenter;
    private CategoryPresenter categoryPresenter;
    private UserPresenter userPresenter;
    private ToyCustomerAdapter adapter;
    private List<Toy> toyList = new ArrayList<>();
    private Map<Integer, String> categoryMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Khởi tạo View Binding
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Thiết lập Toolbar
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("Welcome to Shopping Toys");

        // Khởi tạo các presenter
        userPresenter = new UserPresenter(null, this);
        toyPresenter = new ToyPresenter(this);
        categoryPresenter = new CategoryPresenter(this);

        // Thiết lập RecyclerView với GridLayoutManager
        binding.productRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new ToyCustomerAdapter(this, toyList, categoryMap, toy -> addToCart(toy), toy -> showToyDetail(toy));
        binding.productRecyclerView.setAdapter(adapter);

        // Tải danh mục
        categoryPresenter.getCategories();
    }

    private void addToCart(Toy toy) {
        int userId = userPresenter.getCurrentUserId();
        if (userId != -1) {
            CartPresenter cartPresenter = new CartPresenter(this);
            cartPresenter.addToCart(userId, toy.getId());
        } else {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
        }
    }

    private void showToyDetail(Toy toy) {
        Intent intent = new Intent(HomeActivity.this, ToyDetailActivity.class);
        intent.putExtra("toy_id", toy.getId());
        intent.putExtra("toy_image", toy.getImage());
        intent.putExtra("toy_name", toy.getName());
        intent.putExtra("toy_price", toy.getPrice());
        String categoryName = categoryMap.getOrDefault(toy.getCategoryId(), "No Category");
        intent.putExtra("toy_category", categoryName);
        intent.putExtra("toy_description", toy.getDescription());
        startActivity(intent);
    }

    @Override
    public void onCategoriesLoaded(List<Category> categories) {
        for (Category category : categories) {
            categoryMap.put(category.getId(), category.getName());
        }
        toyPresenter.getToys();
    }

    @Override
    public void onToysLoaded(List<Toy> toys) {
        toyList.clear();
        toyList.addAll(toys);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onError(String message) {
        Toast.makeText(this, "Error: " + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAddProductSuccess(String response) {
    }

    @Override
    public void onToyDeleted(String response) {
    }

    @Override
    public void onToyUpdated(String response) {
    }

    @Override
    public void onCategoryAddedSuccess(String response) {
    }

    @Override
    public void onCategoryDeleted(String response) {
    }

    // Thêm phần tạo menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu); // Tạo menu từ resource file
        return true;
    }

    // Xử lý sự kiện khi nhấn vào các mục trong menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.view_cart) {
            // Xử lý sự kiện nhấn vào "View Cart"
            Intent intent = new Intent(HomeActivity.this, CartActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.logout) {
            // Xử lý sự kiện nhấn vào "Logout"
            Toast.makeText(this, "Logout successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
