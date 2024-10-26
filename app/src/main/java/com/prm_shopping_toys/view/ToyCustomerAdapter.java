package com.prm_shopping_toys.view;

import android.content.Context;
import android.content.Intent;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.prm_shopping_toys.R;
import com.prm_shopping_toys.model.Toy;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import okhttp3.Response;

public class ToyCustomerAdapter extends RecyclerView.Adapter<ToyCustomerAdapter.ToyViewHolder> {

    private Context context;
    private List<Toy> toyList;
    private Map<Integer, String> categoryMap;
    private AddToCartListener addToCartListener;
    private ToyClickListener toyClickListener;

    public ToyCustomerAdapter(Context context, List<Toy> toyList, Map<Integer, String> categoryMap,
                              AddToCartListener addToCartListener, ToyClickListener toyClickListener) {
        this.context = context;
        this.toyList = toyList;
        this.categoryMap = categoryMap;
        this.addToCartListener = addToCartListener;
        this.toyClickListener = toyClickListener;
    }

    @NonNull
    @Override
    public ToyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_toy_customer, parent, false);
        return new ToyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ToyViewHolder holder, int position) {
        Toy toy = toyList.get(position);

        // Thiết lập thông tin cho toy
        DecimalFormat formatter = new DecimalFormat("#,###");
        String formattedPrice = formatter.format(toy.getPrice()).replace(",", ".");
        holder.toyNameTextView.setText(toy.getName());
        holder.toyPriceTextView.setText(String.format("%s VNĐ", formattedPrice));

        // Lấy tên danh mục từ categoryMap
        String categoryName = categoryMap.get(toy.getCategoryId());
        holder.toyCategoryTextView.setText(categoryName);

        // Load hình ảnh toy bằng Glide
        Glide.with(context)
                .load(toy.getImage())
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.placeholder_image)
                .into(holder.toyImageView);

        // Xử lý sự kiện khi nhấn Add to Cart
        holder.addToCartButton.setOnClickListener(v -> {
            if (addToCartListener != null) {
                addToCartListener.onAddToCart(toy);
            }
        });

        // Xử lý sự kiện khi nhấn vào toy
        holder.itemView.setOnClickListener(v -> {
            if (toyClickListener != null) {
                toyClickListener.onToyClick(toy);
            }
        });
    }

    @Override
    public int getItemCount() {
        return toyList.size();
    }

    public static class ToyViewHolder extends RecyclerView.ViewHolder {
        ImageView toyImageView;
        TextView toyNameTextView;
        TextView toyPriceTextView;
        TextView toyCategoryTextView;
        Button addToCartButton;

        public ToyViewHolder(@NonNull View itemView) {
            super(itemView);
            toyImageView = itemView.findViewById(R.id.toy_image);
            toyNameTextView = itemView.findViewById(R.id.toy_name);
            toyPriceTextView = itemView.findViewById(R.id.toy_price);
            toyCategoryTextView = itemView.findViewById(R.id.toy_category);
            addToCartButton = itemView.findViewById(R.id.add_to_cart_button);
        }
    }

    // Listener interface cho Add to Cart
    public interface AddToCartListener {
        void onAddToCart(Toy toy);
    }

    // Listener interface cho click vào toy
    public interface ToyClickListener {
        void onToyClick(Toy toy);
    }
}
