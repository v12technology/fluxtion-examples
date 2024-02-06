package com.fluxtion.example.cookbook.ml.linearregression;

import com.fluxtion.compiler.EventProcessorConfig;
import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.compiler.builder.dataflow.DataFlow;
import com.fluxtion.example.cookbook.ml.linearregression.api.HouseSalesDetailsPostProcess;
import com.fluxtion.example.cookbook.ml.linearregression.api.HouseSaleDetails;
import com.fluxtion.example.cookbook.ml.linearregression.api.HouseSalesMonitor;
import com.fluxtion.example.cookbook.ml.linearregression.node.LiveHouseSalesCache;
import com.fluxtion.example.cookbook.ml.linearregression.node.OpportunityNotifierNode;
import com.fluxtion.example.cookbook.ml.linearregression.pipeline.HouseTransformer;
import com.fluxtion.example.cookbook.ml.linearregression.api.OpportunityNotifier;
import com.fluxtion.example.cookbook.ml.linearregression.generated.OpportunityMlProcessor;
import com.fluxtion.example.cookbook.ml.linearregression.pipeline.AreaFeature;
import com.fluxtion.example.cookbook.ml.linearregression.pipeline.HouseFilters;
import com.fluxtion.runtime.EventProcessor;
import com.fluxtion.runtime.ml.Calibration;
import com.fluxtion.runtime.ml.CalibrationProcessor;
import com.fluxtion.runtime.ml.PredictiveLinearRegressionModel;

import java.util.Collections;

public class Main {

    private static CalibrationProcessor calibrationProcessor;
    private static OpportunityNotifier notifier;
    private static HouseSalesMonitor houseSalesMonitor;
    private static EventProcessor<?> opportunityIdentifier;

    public static void main(String[] args) {
        buildProcessingLogic(false);
        exportAppServices();

        setCalibration(4, 3.6);

        //online processing
        registerHouseForSale(new HouseSaleDetails("A12",12.0, 3));
        registerHouseForSale(new HouseSaleDetails("A12",25, 6));
        registerHouseForSale(new HouseSaleDetails("A12",250, 13));
        //turn publication off
        notifier.setEnableNotifications(true);
        registerHouseForSale(new HouseSaleDetails( "A1", 12.0, 3));
        registerHouseForSale(new HouseSaleDetails( "B2", 25, 6));
        registerHouseForSale(new HouseSaleDetails( "UU1", 25, 6));
        registerHouseForSale(new HouseSaleDetails( "A12", 250, 13));
        registerHouseForSale(new HouseSaleDetails( "A12", 6, 1));
        //update calibration
        setCalibration(2, 10);
        registerHouseForSale(new HouseSaleDetails("A12",12.0, 3));
        registerHouseForSale(new HouseSaleDetails("A12",25, 6));
    }

    public static void buildProcessingLogic(boolean interpreted){
        opportunityIdentifier = interpreted ? Fluxtion.interpret(Main::buildLogic) : new OpportunityMlProcessor();
        opportunityIdentifier.init();
    }

    public static void buildLogic(EventProcessorConfig cfg) {
        var preProcessHouseDetails = DataFlow.subscribe(HouseSaleDetails.class)
                .map(HouseTransformer::asPostProcess)
                .filter(HouseFilters::bedroomWithinRange)
                .filter(HouseFilters::correctLocation)
                .peek(Main::logValid)
                .flowSupplier();
        var predictor = new PredictiveLinearRegressionModel(new AreaFeature(preProcessHouseDetails));
        var opportunityNotifier = new OpportunityNotifierNode(predictor);
        cfg.addNode(opportunityNotifier, new LiveHouseSalesCache());
    }

    public static void exportAppServices() {
        calibrationProcessor = opportunityIdentifier.getExportedService();
        notifier = opportunityIdentifier.getExportedService();
        houseSalesMonitor = opportunityIdentifier.getExportedService();
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

    private static void registerHouseForSale(HouseSaleDetails houseDetailsPostProcess) {
        opportunityIdentifier.onEvent(houseDetailsPostProcess);
    }

    public static void logValid(HouseSalesDetailsPostProcess houseSalesDetailsPostProcess){
        System.out.println("\tvalidated:" + houseSalesDetailsPostProcess);
    }
}
