package com.example.ezmeal.Model;

public class Item {
    private String itemName;
    private String brand;
    private int itemQuantity;

    public Item(String name, String brand) {
        //itemQuantity default is 1
        itemName = name;
        this.brand = brand;
        itemQuantity = 1;
    }

    Item(String name, String brand, int itemQuantity){
        this.itemName = name;
        this.brand = brand;
        this.itemQuantity = itemQuantity;
    }
    public String getName() {
        return itemName;
    }

    public void setItemName(String name) {
        this.itemName = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getQuantity(){return itemQuantity;}

    public void setItemQuantity(int itemQuantity){
        this.itemQuantity = itemQuantity;
    }


}
