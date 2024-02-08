package com.fluxtion.example.cookbook.ml.linearregression.pipeline;

import com.fluxtion.example.cookbook.ml.linearregression.api.HouseSalesDetailsPostProcess;
import com.fluxtion.example.cookbook.ml.linearregression.api.HouseSaleDetails;

public interface HouseTransformer {

    static HouseSalesDetailsPostProcess asPostProcess(HouseSaleDetails details) {
        int locationCategory = switch (details.locationZip().charAt(0)) {
            case 'A', 'a' -> 1;
            case 'B', 'b' -> 2;
            case 'C', 'c' -> 3;
            default -> -1;
        };
        return new HouseSalesDetailsPostProcess(locationCategory, details);
    }
}
