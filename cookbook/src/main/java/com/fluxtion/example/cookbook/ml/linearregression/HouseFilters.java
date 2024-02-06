package com.fluxtion.example.cookbook.ml.linearregression;

public class HouseFilters {

    public static boolean bedroomWithinRange(HouseDetails houseDetails) {
        if (houseDetails.bedrooms() > 1 & houseDetails.bedrooms() < 8) {
            return true;
        }
        System.out.println("\tignoring:" + houseDetails);
        return false;
    }
}
