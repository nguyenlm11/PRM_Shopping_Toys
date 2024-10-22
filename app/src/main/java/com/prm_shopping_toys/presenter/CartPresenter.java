package com.prm_shopping_toys.presenter;

import android.content.Context;
import android.widget.Toast;

import com.prm_shopping_toys.api.CartAPI;
import com.prm_shopping_toys.model.Cart;
import com.prm_shopping_toys.utils.ApiClient;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartPresenter {
    private Context context;
    private CartAPI cartAPI;

    public CartPresenter(Context context) {
        this.context = context;
        this.cartAPI = ApiClient.getClient().create(CartAPI.class);
    }

    public void addToCart(int userId, int toyId) {
        cartAPI.addToCart(userId, toyId, 1).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Đã thêm sản phẩm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Thêm vào giỏ hàng thất bại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getCartItems(int userId, CartCallback callback) {
        cartAPI.getCartItems(userId).enqueue(new Callback<List<Cart>>() {
            @Override
            public void onResponse(Call<List<Cart>> call, Response<List<Cart>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Lỗi khi lấy giỏ hàng!");
                }
            }

            @Override
            public void onFailure(Call<List<Cart>> call, Throwable t) {
                callback.onFailure("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    public void removeFromCart(int userId, int toyId, CartCallback callback) {
        cartAPI.removeFromCart(userId, toyId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Sản phẩm đã được xóa khỏi giỏ hàng!", Toast.LENGTH_SHORT).show();
                    callback.onSuccess(null);
                } else {
                    callback.onFailure("Xóa sản phẩm thất bại!");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onFailure("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    public void clearCart(int userId, CartCallback callback) {
        cartAPI.clearCart(userId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(null);
                    Toast.makeText(context, "Giỏ hàng đã được xóa!", Toast.LENGTH_SHORT).show();
                } else {
                    callback.onFailure("Xóa giỏ hàng thất bại!");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onFailure("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    public interface CartCallback {
        void onSuccess(List<Cart> cartItems);

        void onFailure(String errorMessage);
    }
}
