package com.prm_shopping_toys.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.appcompat.widget.SearchView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
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

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userPresenter = new UserPresenter(null, this);
        toyPresenter = new ToyPresenter(this);
        categoryPresenter = new CategoryPresenter(this);

        // Thiết lập RecyclerView với GridLayoutManager
        binding.productRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new ToyCustomerAdapter(this, toyList, categoryMap, toy -> addToCart(toy), toy -> showToyDetail(toy));
        binding.productRecyclerView.setAdapter(adapter);

        categoryPresenter.getCategories();

        setupBottomNavigation();

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchToys(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    loadAllToys();
                    binding.noResultsTextView.setVisibility(View.GONE);
                }
                return true;
            }
        });
    }

    private void loadAllToys() {
        binding.searchProgressBar.setVisibility(View.VISIBLE);
        binding.productRecyclerView.setVisibility(View.GONE);

        new Handler().postDelayed(() -> {
            toyPresenter.getToys();
            binding.searchProgressBar.setVisibility(View.GONE);
            binding.productRecyclerView.setVisibility(View.VISIBLE);
        }, 2000);
    }

    private void searchToys(String name) {
        binding.searchProgressBar.setVisibility(View.VISIBLE);
        binding.productRecyclerView.setVisibility(View.GONE);

        new Handler().postDelayed(() -> {
            toyPresenter.searchToysByName(name);
            binding.searchProgressBar.setVisibility(View.GONE);
            binding.productRecyclerView.setVisibility(View.VISIBLE);
        }, 2000);
    }

    private void setupBottomNavigation() {
        binding.bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.navigation_home) {
                    if (!HomeActivity.this.getClass().equals(HomeActivity.class)) {
                        Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                    return true;
                } else if (itemId == R.id.navigation_cart) {
                    if (!HomeActivity.this.getClass().equals(CartActivity.class)) {
                        Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                    return true;
                } else if (itemId == R.id.navigation_profile) {
                    if (!HomeActivity.this.getClass().equals(UserInfoActivity.class)) {
                        Intent intent = new Intent(HomeActivity.this, UserInfoActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                    return true;
                } else if (itemId == R.id.navigation_order) {
                    if (!HomeActivity.this.getClass().equals(OrderActivity.class)) {
                        Intent intent = new Intent(HomeActivity.this, OrderActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                    return true;
                } else if (itemId == R.id.navigation_logout) {
                    logoutUser();
                    return true;
                }
                return false;
            }
        });
    }

    private void logoutUser() {
        Toast.makeText(this, "Logout successfully", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
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
        binding.searchProgressBar.setVisibility(View.GONE);
        toyList.clear();
        toyList.addAll(toys);
        adapter.notifyDataSetChanged();

        if (toys.isEmpty()) {
            binding.noResultsTextView.setVisibility(View.VISIBLE);
        } else {
            binding.noResultsTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onError(String message) {
        binding.searchProgressBar.setVisibility(View.GONE);
        binding.noResultsTextView.setVisibility(View.VISIBLE);
        binding.noResultsTextView.setText("No results found.\nTry using more general keywords.");

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
}
