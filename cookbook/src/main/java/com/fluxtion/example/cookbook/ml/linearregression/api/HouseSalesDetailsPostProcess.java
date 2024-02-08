package com.fluxtion.example.cookbook.ml.linearregression.api;

public record HouseSalesDetailsPostProcess(int locationCategory, HouseSaleDetails details) {



    public double area() {
        return details.area();
    }

    public int bedrooms() {
        return details.bedrooms();
    }

    public double offerPrice() {
        return details.offerPrice();
    }

    public String id() {
        return details.id();
    }
}
