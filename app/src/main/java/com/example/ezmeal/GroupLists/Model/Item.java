package com.example.ezmeal.GroupLists.Model;

public class Item {
    private String itemName;
    private String brand;
    private int itemQuantity;
    private String userName;

    public Item(String name, String brand, String user) {
        //itemQuantity default is 1
        itemName = name;
        this.brand = brand;
        itemQuantity = 1;
        userName = user;
    }

    Item(String name, String brand, int itemQuantity, String user){
        this.itemName = name;
        this.brand = brand;
        this.itemQuantity = itemQuantity;
        this.userName = user;
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

    public String getUser() {
        return userName;
    }

    public void setUser(String user) {
        this.userName = user;
    }


}
