package com.fluxtion.example.cookbook.ml.linearregression.pipeline;

import com.fluxtion.example.cookbook.ml.linearregression.api.HouseSaleDetails;
import com.fluxtion.runtime.dataflow.FlowSupplier;
import com.fluxtion.runtime.ml.FlowSuppliedFeature;

public class LocationCategoryFeature extends FlowSuppliedFeature<HouseSaleDetails> {
    public LocationCategoryFeature(FlowSupplier<HouseSaleDetails> houseDetailSupplier) {
        super(houseDetailSupplier);
    }

    @Override
    public double extractFeatureValue() {
        return switch (data().getLocationZip().charAt(0)) {
            case 'A', 'a' -> 1;
            case 'B', 'b' -> 2;
            case 'C', 'c' -> 3;
            default -> -1;
        };
    }

}

