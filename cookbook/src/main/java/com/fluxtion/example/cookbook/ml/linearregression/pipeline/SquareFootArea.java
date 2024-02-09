package com.fluxtion.example.cookbook.ml.linearregression.pipeline;

import com.fluxtion.example.cookbook.ml.linearregression.api.HouseSaleDetails;
import com.fluxtion.runtime.dataflow.FlowSupplier;
import com.fluxtion.runtime.ml.FlowSuppliedFeature;

public class SquareFootArea extends FlowSuppliedFeature<HouseSaleDetails> {
    public SquareFootArea(FlowSupplier<HouseSaleDetails> houseDetailSupplier) {
        super(houseDetailSupplier);
    }

    @Override
    public double extractFeatureValue() {
        return Math.pow(data().getArea(), 2) ;
    }
}
