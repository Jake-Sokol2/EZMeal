package com.example.ezmeal.Model;

import java.util.ArrayList;
import java.util.List;

public class GroceryListModel {
    private List<List<String>> shoppingList;


    public GroceryListModel(){
        shoppingList = new ArrayList<List<String>>();
    }

    public void addItem(String itemName, String itemBrand){
        List<String> tmp = new ArrayList<String>();
        tmp.add(itemName);
        tmp.add(itemBrand);
        tmp.add("1");

        shoppingList.add(tmp);

    }

    public void addItem(String itemName, String itemBrand, int itemQuantity){
        List<String> tmp = new ArrayList<String>();
        tmp.add(itemName);
        tmp.add(itemBrand);
        tmp.add(String.valueOf(itemQuantity));

        shoppingList.add(tmp);
    }

    public int listLength()
    {
        return shoppingList.size();
    }

    public void dumpList()
    {
        shoppingList.clear();
    }

    public List<List<String>> getGroceryList() {
        return shoppingList;
    }

    public void restoreGroceryList(List<List<String>> theList) {
        shoppingList = theList;
    }

}
