package com.prm_shopping_toys.model;

public class Category {
    private int id;
    private String name;

    // Constructor, getter và setter
    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

