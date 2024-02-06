package com.fluxtion.example.cookbook.ml.linearregression.pipeline;

import com.fluxtion.example.cookbook.ml.linearregression.api.HouseSalesDetailsPostProcess;

public class HouseFilters {

    public static boolean bedroomWithinRange(HouseSalesDetailsPostProcess houseSalesDetailsPostProcess) {
        if (houseSalesDetailsPostProcess.bedrooms() > 1 & houseSalesDetailsPostProcess.bedrooms() < 8) {
            return true;
        }
        System.out.println("\tignoring:" + houseSalesDetailsPostProcess);
        return false;
    }
}
