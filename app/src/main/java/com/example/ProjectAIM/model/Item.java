package com.example.ProjectAIM.model;

// Represents a single item in the inventory list
public class Item {
    private final int id;           // unique ID for this item
    private final String name;      // item name
    private int quantity;           // how many are in stock
    private final String description; // optional notes or details

    // builds a new item object
    public Item(int id, String name, int quantity, String description) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.description = description;
    }

    // getters and setter
    public int getId() { return id; }
    public String getName() { return name; }
    public int getQuantity() { return quantity; }
    public String getDescription() { return description; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
