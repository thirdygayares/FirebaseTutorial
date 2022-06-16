package com.example.firebasetutorial;

import com.google.firebase.firestore.Exclude;

public class Product {
    private  String id;
    private String name;
    private String price;
    private  String stocks;

    public Product(){
        //needed lang to
    }

    public Product(String name, String price, String stocks) {
        this.name = name;
        this.price = price;
        this.stocks = stocks;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getStocks() {
        return stocks;
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
