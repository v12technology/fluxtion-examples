package com.fluxtion.example.cookbook.ml.linearregression.node;

import com.fluxtion.example.cookbook.ml.linearregression.api.OpportunityNotifier;
import com.fluxtion.runtime.annotations.ExportService;
import com.fluxtion.runtime.annotations.OnTrigger;
import com.fluxtion.runtime.ml.PredictiveModel;
import lombok.Setter;

import java.util.function.Consumer;

public class OpportunityNotifierNode implements @ExportService OpportunityNotifier {
    private final PredictiveModel predictiveModel;
    @Setter
    private boolean enableNotifications = false;
    @Setter
    private double profitTrigger;
    @Setter
    private Consumer<Object> notificationSink;

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

}
