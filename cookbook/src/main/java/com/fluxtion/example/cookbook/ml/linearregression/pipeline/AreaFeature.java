package com.fluxtion.example.cookbook.ml.linearregression.pipeline;

import com.fluxtion.example.cookbook.ml.linearregression.api.HouseSalesDetailsPostProcess;
import com.fluxtion.runtime.annotations.ExportService;
import com.fluxtion.runtime.annotations.OnTrigger;
import com.fluxtion.runtime.dataflow.FlowSupplier;
import com.fluxtion.runtime.ml.AbstractFeature;
import com.fluxtion.runtime.ml.CalibrationProcessor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AreaFeature extends AbstractFeature implements @ExportService CalibrationProcessor {

    private final FlowSupplier<HouseSalesDetailsPostProcess> houseDetailSupplier;

    @OnTrigger
    public boolean processRecord() {
        value = houseDetailSupplier.get().area() * co_efficient * weight;
        return true;
    }
}
