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

import java.util.List;

public class CartAdapter extends BaseAdapter {
    private Context context;
    private List<Cart> cartList;
    private CartPresenter cartPresenter;
    private int userId;

    public CartAdapter(Context context, List<Cart> cartList, CartPresenter cartPresenter, int userId) {
        this.context = context;
        this.cartList = cartList;
        this.cartPresenter = cartPresenter;
        this.userId = userId;
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
            convertView = LayoutInflater.from(context).inflate(com.prm_shopping_toys.R.layout.item_cart, parent, false);
        }

        Cart cartItem = cartList.get(position);

        ImageView toyImage = convertView.findViewById(R.id.cart_toy_image);
        TextView toyName = convertView.findViewById(R.id.cart_toy_name);
        TextView toyPrice = convertView.findViewById(R.id.cart_toy_price);
        TextView toyQuantity = convertView.findViewById(R.id.cart_toy_quantity);
        Button removeButton = convertView.findViewById(R.id.remove_button);

        Glide.with(context).load(cartItem.getToy().getImage()).into(toyImage);
        toyName.setText(cartItem.getToy().getName());
        toyPrice.setText(String.format("%.0f VNĐ", cartItem.getToy().getPrice()));
        toyQuantity.setText("Quantity: " + cartItem.getQuantity());

        // Xử lý sự kiện nhấn nút xóa
        removeButton.setOnClickListener(v -> {
            int toyId = cartItem.getToyId();
            if (toyId != 0) { // Kiểm tra nếu toyId không phải là 0
                cartPresenter.removeFromCart(userId, toyId, new CartPresenter.CartCallback() {
                    @Override
                    public void onSuccess(List<Cart> cartItems) {
                        cartList.remove(position);
                        notifyDataSetChanged();
                        Toast.makeText(context, "Toy removed from cart!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(context, "Invalid Toy ID!", Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }
}
