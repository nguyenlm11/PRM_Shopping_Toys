package com.prm_shopping_toys.view;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.prm_shopping_toys.databinding.ActivityManageCustomerBinding;
import com.prm_shopping_toys.model.User;
import com.prm_shopping_toys.presenter.CustomerPresenter;

import java.util.ArrayList;
import java.util.List;

public class ManageCustomerActivity extends AppCompatActivity implements CustomerView {

    private ActivityManageCustomerBinding binding;
    private CustomerAdapter customerAdapter;
    private CustomerPresenter presenter;
    private List<User> customerList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize View Binding
        binding = ActivityManageCustomerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set up ListView and Adapter
        customerAdapter = new CustomerAdapter(this, customerList, this::toggleCustomerStatus);
        binding.customerListView.setAdapter(customerAdapter);

        presenter = new CustomerPresenter(this);
        presenter.getCustomers();
    }

    // Toggle customer status between 'active' and 'inactive'
    private void toggleCustomerStatus(User customer) {
        String newStatus = customer.getStatus().equals("active") ? "inactive" : "active";
        presenter.updateCustomerStatus(customer.getId(), newStatus);
    }

    @Override
    public void onCustomersLoaded(List<User> customers) {
        customerList.clear();
        customerList.addAll(customers);
        customerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCustomerStatusUpdated(String response) {
        Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
        presenter.getCustomers();
    }

    @Override
    public void onError(String message) {
        Toast.makeText(this, "Error: " + message, Toast.LENGTH_SHORT).show();
    }
}
