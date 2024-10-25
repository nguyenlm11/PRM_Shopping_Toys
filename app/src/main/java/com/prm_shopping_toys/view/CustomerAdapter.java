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
import androidx.core.content.ContextCompat;

import com.prm_shopping_toys.R;
import com.prm_shopping_toys.model.User;

import java.util.List;

public class CustomerAdapter extends ArrayAdapter<User> {
    private final List<User> customers;
    private final CustomerStatusChangeListener statusChangeListener;

    // Constructor
    public CustomerAdapter(@NonNull Context context, @NonNull List<User> customers, CustomerStatusChangeListener statusChangeListener) {
        super(context, 0, customers);
        this.customers = customers;
        this.statusChangeListener = statusChangeListener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_customer, parent, false);

            // Initialize the ViewHolder and set the views
            viewHolder = new ViewHolder();
            viewHolder.customerNumber = convertView.findViewById(R.id.customer_number);
            viewHolder.customerName = convertView.findViewById(R.id.customer_name);
            viewHolder.statusButton = convertView.findViewById(R.id.status_button);

            // Store the holder with the view
            convertView.setTag(viewHolder);
        } else {
            // Recycle the view to improve performance
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Get the data item for this position
        User customer = customers.get(position);

        // Populate the data into the template view using the data object
        viewHolder.customerNumber.setText(String.valueOf(position + 1));
        viewHolder.customerName.setText(customer.getUsername());

        // Update the button text and color based on the current status
        if ("active".equals(customer.getStatus())) {
            viewHolder.statusButton.setText("Activate");
            viewHolder.statusButton.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.greenColor));
        } else {
            viewHolder.statusButton.setText("Deactivate");
            viewHolder.statusButton.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.priceColor));
        }

        // Handle status button click
        viewHolder.statusButton.setOnClickListener(v -> {
            if (statusChangeListener != null) {
                statusChangeListener.onChangeStatus(customer);
            }
        });

        // Return the completed view to render on screen
        return convertView;
    }

    // ViewHolder to cache the views
    private static class ViewHolder {
        TextView customerNumber;
        TextView customerName;
        Button statusButton;
    }

    // Interface for changing customer status
    public interface CustomerStatusChangeListener {
        void onChangeStatus(User customer);
    }
}
