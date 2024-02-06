package com.fluxtion.example.cookbook.ml.linearregression.node;

import com.fluxtion.example.cookbook.ml.linearregression.api.HouseSaleDetails;
import com.fluxtion.example.cookbook.ml.linearregression.api.HouseSalesMonitor;
import com.fluxtion.runtime.annotations.ExportService;
import com.fluxtion.runtime.annotations.NoPropagateFunction;
import com.fluxtion.runtime.annotations.builder.Inject;
import com.fluxtion.runtime.callback.EventDispatcher;
import com.fluxtion.runtime.ml.Calibration;
import com.fluxtion.runtime.ml.CalibrationProcessor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LiveHouseSalesCache
        implements
        @ExportService CalibrationProcessor,
        @ExportService HouseSalesMonitor {

    @Inject
    @Getter @Setter
    private EventDispatcher dispatcher;
    private transient final List<HouseSaleDetails> liveSalesCache = new ArrayList<>();

    @Override
    @NoPropagateFunction
    public void houseSold(HouseSaleDetails soldHouse){
        liveSalesCache.add(soldHouse);
    }

    @Override
    @NoPropagateFunction
    public void removeAllSales() {
        liveSalesCache.clear();
    }

    @Override
    @NoPropagateFunction
    public boolean setCalibration(List<Calibration> calibration) {
        dispatcher.processReentrantEvents(Collections.emptyList());
        return false;
    }
}
