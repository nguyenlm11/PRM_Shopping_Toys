package com.prm_shopping_toys.model;

import java.util.Map;

public class UserResponse {
    private Map<String, String> user;
    private int order_count;
    private String error;

    // Getters
    public Map<String, String> getUser() {
        return user;
    }

    public int getOrderCount() {
        return order_count;
    }

    public String getError() {
        return error;
    }

    // Kiểm tra lỗi
    public boolean hasError() {
        return error != null;
    }
}
