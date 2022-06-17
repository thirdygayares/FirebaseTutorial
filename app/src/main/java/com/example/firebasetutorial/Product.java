package com.example.firebasetutorial;

import com.google.firebase.firestore.Exclude;

public class Product {
    private  String id;
    private String name;
    private String price;
    private  int stocks;

    public Product(){
        //needed lang to
    }

    public Product(String name, String price, int stocks) {
        this.name = name;
        this.price = price;
        this.stocks = stocks;
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public int getStocks() {
        return stocks;
    }

    public void setStocks(int stocks) {
        this.stocks = stocks;
    }


}
