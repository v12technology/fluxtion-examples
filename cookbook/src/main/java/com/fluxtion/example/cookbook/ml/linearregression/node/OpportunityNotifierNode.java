package com.fluxtion.example.cookbook.ml.linearregression.node;

import com.fluxtion.example.cookbook.ml.linearregression.api.OpportunityNotifier;
import com.fluxtion.runtime.annotations.ExportService;
import com.fluxtion.runtime.annotations.OnTrigger;
import com.fluxtion.runtime.ml.PredictiveModel;

import java.util.function.Consumer;

public class OpportunityNotifierNode implements @ExportService OpportunityNotifier {
    private final PredictiveModel predictiveModel;
    private boolean publishFlag = false;

    public OpportunityNotifierNode(PredictiveModel predictiveModel) {
        this.predictiveModel = predictiveModel;
    }

    @OnTrigger
    public boolean predictionUpdated(){
        if(publishFlag){
            System.out.println("new prediction:" + predictiveModel.predictedValue());
        }
        return false;
    }

    @Override
    public void publishOn() {
        publishFlag = true;
    }

    @Override
    public void publishOff() {
        publishFlag = false;
    }

    @Override
    public void setNotificationSink(Consumer<Object> notifierSink) {

    }
}
