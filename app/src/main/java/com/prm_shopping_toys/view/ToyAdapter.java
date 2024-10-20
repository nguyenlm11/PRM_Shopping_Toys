package com.prm_shopping_toys.view;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.prm_shopping_toys.R;
import com.prm_shopping_toys.model.Toy;

import java.util.List;

public class ToyAdapter extends BaseAdapter {

    private Context context;
    private List<Toy> toyList;
    private ToyDeleteListener deleteListener; // Listener for delete action
    private ToyEditListener editListener; // Listener for edit action

    public ToyAdapter(Context context, List<Toy> toyList, ToyDeleteListener deleteListener, ToyEditListener editListener) {
        this.context = context;
        this.toyList = toyList;
        this.deleteListener = deleteListener; // Initialize delete listener
        this.editListener = editListener; // Initialize edit listener
    }

    @Override
    public int getCount() {
        return toyList.size();
    }

    @Override
    public Object getItem(int position) {
        return toyList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_toy, parent, false);
        }

        // Get current toy from the list
        Toy toy = toyList.get(position);

        // Display toy name and price
        TextView toyNameTextView = convertView.findViewById(R.id.toy_name);
        TextView toyPriceTextView = convertView.findViewById(R.id.toy_price);
        toyNameTextView.setText(toy.getName());
        toyPriceTextView.setText(String.format("%.0f VNĐ", toy.getPrice()));

        // Delete button functionality
        Button deleteButton = convertView.findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onDeleteToy(toy);  // Notify the listener for deletion
            }
        });

        // Edit button functionality
        Button editButton = convertView.findViewById(R.id.edit_button);
        editButton.setOnClickListener(v -> {
            if (editListener != null) {
                editListener.onEditToy(toy);
            }
        });

        return convertView;
    }

    // Interface for delete action
    public interface ToyDeleteListener {
        void onDeleteToy(Toy toy);
    }

    // Interface for edit action
    public interface ToyEditListener {
        void onEditToy(Toy toy);
    }
}
