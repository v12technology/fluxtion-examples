package com.fluxtion.example.cookbook.ml.linearregression;

import com.fluxtion.compiler.EventProcessorConfig;
import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.compiler.builder.dataflow.DataFlow;
import com.fluxtion.example.cookbook.ml.linearregression.generated.OpportunityMlProcessor;
import com.fluxtion.runtime.EventProcessor;
import com.fluxtion.runtime.ml.Calibration;
import com.fluxtion.runtime.ml.CalibrationProcessor;
import com.fluxtion.runtime.ml.PredictiveLinearRegressionModel;

import java.util.Collections;

public class Main {

    private static CalibrationProcessor calibrationProcessor;
    private static OpportunityNotifier notifier;
    private static EventProcessor<?> opportunityIdentifier;

    public static void main(String[] args) {
        buildProcessor(false);
        buildApp();
        setCalibration(4, 3.6);
        //online processing
        runPredictions(new HouseDetails(12.0, 3));
        runPredictions(new HouseDetails(25, 6));
        runPredictions(new HouseDetails(250, 13));
        //turn publication off
        notifier.publishOn();
        runPredictions(new HouseDetails(12.0, 3));
        runPredictions(new HouseDetails(25, 6));
        runPredictions(new HouseDetails(250, 13));
        runPredictions(new HouseDetails(6, 1));
        //update calibration
        setCalibration(2, 10);
        runPredictions(new HouseDetails(12.0, 3));
        runPredictions(new HouseDetails(25, 6));
    }

    public static void buildProcessor(boolean interpreted){
        opportunityIdentifier = interpreted ? Fluxtion.interpret(Main::buildLogic) : new OpportunityMlProcessor();
    }

    public static void buildLogic(EventProcessorConfig cfg) {
        var preProcessHouseDetails = DataFlow.subscribe(HouseDetails.class)
                .filter(HouseFilters::bedroomWithinRange)
//                .console()
//                .peek(Main::logValid)
                .flowSupplier();
        var predictor = new PredictiveLinearRegressionModel(new AreaFeature(preProcessHouseDetails));
        var opportunityNotifier = new OpportunityNotifierNode(predictor);
        cfg.addNode(opportunityNotifier);
    }

    public static void buildApp() {
        opportunityIdentifier.init();
        calibrationProcessor = opportunityIdentifier.getExportedService();
        notifier = opportunityIdentifier.getExportedService();
    }

    public static void setCalibration(double weight, double co_efficient) {
        calibrationProcessor.setCalibration(
                Collections.singletonList(
                        Calibration.builder()
                                .featureClass(AreaFeature.class)
                                .weight(weight).co_efficient(co_efficient)
                                .featureVersion(0)
                                .build()));
    }

    private static void runPredictions(HouseDetails houseDetails) {
        opportunityIdentifier.onEvent(houseDetails);
    }

    public static void logValid(HouseDetails houseDetails){
//        System.out.println("\tvalidated:" + houseDetails);
    }
}
