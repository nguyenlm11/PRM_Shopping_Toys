package com.prm_shopping_toys.model;

import com.google.gson.annotations.SerializedName;

public class Cart {
    private int id;

    @SerializedName("user_id")
    private int userId;

    @SerializedName("toy_id")
    private int toyId;

    private int quantity;
    private Toy toy;

    // Constructor
    public Cart(int id, int userId, int toyId, int quantity, Toy toy) {
        this.id = id;
        this.userId = userId;
        this.toyId = toyId;
        this.quantity = quantity;
        this.toy = toy;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public int getToyId() {
        return toyId;
    }

    public int getQuantity() {
        return quantity;
    }

    public Toy getToy() {
        return toy;
    }
}
