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
import com.prm_shopping_toys.model.Category;

import java.util.List;

public class CategoryAdapter extends ArrayAdapter<Category> {

    private final List<Category> categories;
    private final CategoryDeleteListener deleteListener;

    public CategoryAdapter(@NonNull Context context, @NonNull List<Category> categories, CategoryDeleteListener deleteListener) {
        super(context, 0, categories);
        this.categories = categories;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_category, parent, false);
        }

        Category category = categories.get(position);

        TextView categoryName = convertView.findViewById(R.id.category_name);
        Button deleteButton = convertView.findViewById(R.id.delete_category_button);

        categoryName.setText(category.getName());

        // Handle delete button click
        deleteButton.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onDeleteCategory(category);
            }
        });

        return convertView;
    }

    // Interface for delete category action
    public interface CategoryDeleteListener {
        void onDeleteCategory(Category category);
    }
}
