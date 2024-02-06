package com.fluxtion.example.cookbook.ml.linearregression;

import com.fluxtion.runtime.annotations.ExportService;
import com.fluxtion.runtime.annotations.OnTrigger;
import com.fluxtion.runtime.ml.PredictiveModel;

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
}
