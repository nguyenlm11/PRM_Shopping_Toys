package com.prm_shopping_toys.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.prm_shopping_toys.R;
import com.prm_shopping_toys.model.Cart;
import com.prm_shopping_toys.presenter.CartPresenter;

import java.text.DecimalFormat;
import java.util.List;

public class CartAdapter extends BaseAdapter {
    private Context context;
    private List<Cart> cartList;
    private CartPresenter cartPresenter;
    private int userId;
    private OnQuantityChangeListener quantityChangeListener;

    public CartAdapter(Context context, List<Cart> cartList, CartPresenter cartPresenter, int userId, OnQuantityChangeListener quantityChangeListener) {
        this.context = context;
        this.cartList = cartList;
        this.cartPresenter = cartPresenter;
        this.userId = userId;
        this.quantityChangeListener = quantityChangeListener;
    }

    @Override
    public int getCount() {
        return cartList.size();
    }

    @Override
    public Object getItem(int position) {
        return cartList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        }

        Cart cartItem = cartList.get(position);

        ImageView toyImage = convertView.findViewById(R.id.cart_toy_image);
        TextView toyName = convertView.findViewById(R.id.cart_toy_name);
        TextView toyPrice = convertView.findViewById(R.id.cart_toy_price);
        TextView toyQuantity = convertView.findViewById(R.id.cart_toy_quantity);
        Button removeButton = convertView.findViewById(R.id.remove_button);
        Button increaseButton = convertView.findViewById(R.id.increase_button);
        Button decreaseButton = convertView.findViewById(R.id.decrease_button);

        DecimalFormat formatter = new DecimalFormat("#,###");
        String formattedPrice = formatter.format(cartItem.getToy().getPrice()).replace(",", ".");
        Glide.with(context).load(cartItem.getToy().getImage()).into(toyImage);
        toyName.setText(cartItem.getToy().getName());
        toyPrice.setText(String.format("%s VNĐ", formattedPrice));
        toyQuantity.setText(Integer.toString(cartItem.getQuantity()));

        // Xử lý sự kiện nhấn nút xóa
        removeButton.setOnClickListener(v -> {
            int toyId = cartItem.getToyId();
            if (toyId != 0) {
                cartPresenter.removeFromCart(userId, toyId, new CartPresenter.CartCallback() {
                    @Override
                    public void onSuccess(List<Cart> cartItems) {
                        cartList.remove(position);
                        notifyDataSetChanged();
                        Toast.makeText(context, "Sản phẩm đã được xóa khỏi giỏ hàng!", Toast.LENGTH_SHORT).show();
                        if (quantityChangeListener != null) {
                            quantityChangeListener.onQuantityChanged();
                        }
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(context, "ID sản phẩm không hợp lệ!", Toast.LENGTH_SHORT).show();
            }
        });

        // Xử lý sự kiện nhấn nút tăng số lượng
        increaseButton.setOnClickListener(v -> {
            int newQuantity = cartItem.getQuantity() + 1;
            updateCartQuantity(cartItem.getToyId(), newQuantity);
            cartItem.setQuantity(newQuantity);
            toyQuantity.setText(String.valueOf(newQuantity));
            notifyDataSetChanged();
            if (quantityChangeListener != null) {
                quantityChangeListener.onQuantityChanged();
            }
        });

        // Xử lý sự kiện nhấn nút giảm số lượng
        decreaseButton.setOnClickListener(v -> {
            if (cartItem.getQuantity() > 1) {
                int newQuantity = cartItem.getQuantity() - 1;
                updateCartQuantity(cartItem.getToyId(), newQuantity);
                cartItem.setQuantity(newQuantity);
                toyQuantity.setText(String.valueOf(newQuantity));
                notifyDataSetChanged();
                if (quantityChangeListener != null) {
                    quantityChangeListener.onQuantityChanged();
                }
            } else {
                Toast.makeText(context, "Số lượng không thể nhỏ hơn 1!", Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }

    private void updateCartQuantity(int toyId, int newQuantity) {
        cartPresenter.updateCartQuantity(userId, toyId, newQuantity, new CartPresenter.CartCallback() {
            @Override
            public void onSuccess(List<Cart> cartItems) {
                // Không cần làm gì thêm ở đây
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Interface để thông báo khi số lượng thay đổi
    public interface OnQuantityChangeListener {
        void onQuantityChanged();
    }
}
