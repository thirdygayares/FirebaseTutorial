package com.example.firebasetutorial;

public class Product {
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


}
