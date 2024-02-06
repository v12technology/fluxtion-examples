package com.fluxtion.example.cookbook.ml.linearregression.node;

import com.fluxtion.example.cookbook.ml.linearregression.api.HouseSaleDetails;
import com.fluxtion.example.cookbook.ml.linearregression.api.HouseSalesMonitor;
import com.fluxtion.example.cookbook.ml.linearregression.api.OpportunityNotifier;
import com.fluxtion.example.cookbook.ml.linearregression.api.PotentialOpportunity;
import com.fluxtion.runtime.annotations.ExportService;
import com.fluxtion.runtime.annotations.NoTriggerReference;
import com.fluxtion.runtime.annotations.OnEventHandler;
import com.fluxtion.runtime.annotations.OnTrigger;
import com.fluxtion.runtime.ml.Calibration;
import com.fluxtion.runtime.ml.CalibrationProcessor;
import com.fluxtion.runtime.ml.PredictiveModel;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

@Slf4j
public class OpportunityNotifierNode
        implements
        @ExportService CalibrationProcessor,
        @ExportService HouseSalesMonitor,
        @ExportService OpportunityNotifier {
    private final PredictiveModel predictiveModel;
    @NoTriggerReference
    private final LiveHouseSalesCache liveHouseSalesCache;
    @Setter
    private boolean enableNotifications = false;
    private boolean cacheNotifications = false;
    @Setter
    private double profitTrigger;
    @Setter
    private Consumer<Collection<PotentialOpportunity>> notificationSink = p ->{};
    private transient final List<PotentialOpportunity> cachedNotifications = new ArrayList<>();

    public OpportunityNotifierNode(PredictiveModel predictiveModel) {
        this(predictiveModel, null);
    }

    public OpportunityNotifierNode(PredictiveModel predictiveModel, LiveHouseSalesCache liveHouseSalesCache) {
        this.predictiveModel = predictiveModel;
        this.liveHouseSalesCache = liveHouseSalesCache;
    }

    @OnTrigger
    public boolean predictionUpdated() {
        if (enableNotifications) {
            PotentialOpportunity potentialOpportunity = new PotentialOpportunity(liveHouseSalesCache.getLatestSaleAdvert(), true);
            cachedNotifications.add(potentialOpportunity);
            if(!cacheNotifications){
                log.info("publish prediction:{} enableNotifications:{}", predictiveModel.predictedValue(), enableNotifications);
                notificationSink.accept(cachedNotifications);
                System.out.println("new prediction:" + predictiveModel.predictedValue());
            }
        } else {
            log.info("enableNotifications:{}", enableNotifications);
        }
        return false;
    }

    @Override
    public void houseSold(HouseSaleDetails soldHouse) {

    }

    @Override
    public void removeAllSales() {
    }

    @Override
    public boolean setCalibration(List<Calibration> list) {
        log.info("cacheNotifications");
        cachedNotifications.clear();
        cacheNotifications = true;
        return false;
    }


    @OnEventHandler
    public boolean recalibrationComplete(ReCalibrationCompleteEvent reCalibrationCompleteEvent){
        cacheNotifications = false;
        log.info("reCalibrationCompleteEvent, publishing:{}", cachedNotifications);
        notificationSink.accept(cachedNotifications);
        return false;
    }
}
