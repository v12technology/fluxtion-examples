package com.fluxtion.example.cookbook.ml.linearregression.pipeline;

import com.fluxtion.example.cookbook.ml.linearregression.api.HouseSalesDetailsPostProcess;

public interface HouseFilters {

    static boolean bedroomWithinRange(HouseSalesDetailsPostProcess houseSalesDetailsPostProcess) {
        if (houseSalesDetailsPostProcess.bedrooms() > 1 & houseSalesDetailsPostProcess.bedrooms() < 8) {
            return true;
        }
        System.out.println("\tbedroom fail ignoring:" + houseSalesDetailsPostProcess);
        return false;
    }

    static boolean correctLocation(HouseSalesDetailsPostProcess houseSalesDetailsPostProcess) {
        if (houseSalesDetailsPostProcess.locationCategory() > 0) {
            return true;
        }
        System.out.println("\tlocation fail ignoring:" + houseSalesDetailsPostProcess);
        return false;
    }
}
