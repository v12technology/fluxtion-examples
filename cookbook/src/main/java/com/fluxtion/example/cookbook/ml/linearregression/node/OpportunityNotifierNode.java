package com.fluxtion.example.cookbook.ml.linearregression.node;

import com.fluxtion.example.cookbook.ml.linearregression.api.HouseSaleDetails;
import com.fluxtion.example.cookbook.ml.linearregression.api.HouseSalesMonitor;
import com.fluxtion.example.cookbook.ml.linearregression.api.OpportunityNotifier;
import com.fluxtion.example.cookbook.ml.linearregression.api.PotentialOpportunity;
import com.fluxtion.runtime.annotations.ExportService;
import com.fluxtion.runtime.annotations.OnTrigger;
import com.fluxtion.runtime.ml.PredictiveModel;
import lombok.Setter;

import java.util.function.Consumer;

public class OpportunityNotifierNode
        implements
        @ExportService HouseSalesMonitor,
        @ExportService OpportunityNotifier {
    private final PredictiveModel predictiveModel;
    @Setter
    private boolean enableNotifications = false;
    @Setter
    private double profitTrigger;
    @Setter
    private Consumer<PotentialOpportunity> notificationSink;

    public OpportunityNotifierNode(PredictiveModel predictiveModel) {
        this.predictiveModel = predictiveModel;
    }

    @OnTrigger
    public boolean predictionUpdated(){
        if(enableNotifications){
            System.out.println("new prediction:" + predictiveModel.predictedValue());
        }
        return false;
    }

    @Override
    public void houseSold(HouseSaleDetails soldHouse) {

    }

    @Override
    public void removeAllSales() {
    }
}
