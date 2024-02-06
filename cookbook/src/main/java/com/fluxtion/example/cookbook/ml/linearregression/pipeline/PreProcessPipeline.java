package com.fluxtion.example.cookbook.ml.linearregression.pipeline;

import com.fluxtion.compiler.EventProcessorConfig;
import com.fluxtion.compiler.builder.dataflow.DataFlow;
import com.fluxtion.example.cookbook.ml.linearregression.Main;
import com.fluxtion.example.cookbook.ml.linearregression.api.HouseSaleDetails;
import com.fluxtion.example.cookbook.ml.linearregression.api.HouseSalesDetailsPostProcess;
import com.fluxtion.runtime.dataflow.FlowSupplier;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PreProcessPipeline {

    public static FlowSupplier<HouseSalesDetailsPostProcess> buildLogic(EventProcessorConfig cfg) {
        return DataFlow.subscribe(HouseSaleDetails.class)
                .peek(PreProcessPipeline::logIncoming)
                .map(HouseTransformer::asPostProcess)
                .filter(HouseFilters::bedroomWithinRange)
                .filter(HouseFilters::correctLocation)
                .peek(PreProcessPipeline::logValid)
                .flowSupplier();
    }

    public static void logIncoming(HouseSaleDetails houseSaleDetails){
        log.info("new sale:{}", houseSaleDetails);
    }

    public static void logValid(HouseSalesDetailsPostProcess houseSalesDetailsPostProcess){
        log.info("validated:{}", houseSalesDetailsPostProcess);
    }
}
