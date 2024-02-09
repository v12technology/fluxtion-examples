package com.fluxtion.example.cookbook.ml.linearregression.pipeline;

import com.fluxtion.compiler.EventProcessorConfig;
import com.fluxtion.compiler.builder.dataflow.DataFlow;
import com.fluxtion.example.cookbook.ml.linearregression.api.HouseSaleDetails;
import com.fluxtion.runtime.ml.PredictiveLinearRegressionModel;
import com.fluxtion.runtime.ml.PredictiveModel;
import com.fluxtion.runtime.ml.PropertyToFeature;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PreProcessPipeline {

    public static PredictiveModel buildScoringPipeline(EventProcessorConfig cfg){
        var houseDetailSupplier = DataFlow.subscribe(HouseSaleDetails.class)
                //Uncomment below to see more log output
                .peek(HousePipelineFunctions::logIncomingRecord)
                .filter(HousePipelineFunctions::bedroomWithinRangeFilter)
                .filter(HousePipelineFunctions::correctLocationFilter)
                //Uncomment below to see more log output
                .peek(HousePipelineFunctions::logValidRecord)
                .flowSupplier();
        return new PredictiveLinearRegressionModel(
                PropertyToFeature.build("offerPrice", houseDetailSupplier, HouseSaleDetails::getOfferPrice),
                PropertyToFeature.build("area", houseDetailSupplier, HouseSaleDetails::getArea),
                PropertyToFeature.build("areaSquared", houseDetailSupplier, HouseSaleDetails::getArea, HousePipelineFunctions::squared),
                new LocationCategoryFeature(houseDetailSupplier),
                PropertyToFeature.build("bedroom", houseDetailSupplier, HouseSaleDetails::getBedrooms)
        );
    }

}
