package com.prm_shopping_toys.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.prm_shopping_toys.R;
import com.prm_shopping_toys.model.User;

import java.util.List;

public class CustomerAdapter extends ArrayAdapter<User> {
    private final List<User> customers;
    private final CustomerStatusChangeListener statusChangeListener;

    public CustomerAdapter(@NonNull Context context, @NonNull List<User> customers, CustomerStatusChangeListener statusChangeListener) {
        super(context, 0, customers);
        this.customers = customers;
        this.statusChangeListener = statusChangeListener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_customer, parent, false);
        }

        User customer = customers.get(position);

        TextView customerName = convertView.findViewById(R.id.customer_name);
        Button statusButton = convertView.findViewById(R.id.status_button);

        customerName.setText(customer.getUsername());

        // Update the button text based on current status
        String statusText = customer.getStatus().equals("active") ? "Deactivate" : "Activate";
        statusButton.setText(statusText);

        // Handle status button click
        statusButton.setOnClickListener(v -> {
            if (statusChangeListener != null) {
                statusChangeListener.onChangeStatus(customer);
            }
        });

        return convertView;
    }

    // Interface for changing customer status
    public interface CustomerStatusChangeListener {
        void onChangeStatus(User customer);
    }
}
