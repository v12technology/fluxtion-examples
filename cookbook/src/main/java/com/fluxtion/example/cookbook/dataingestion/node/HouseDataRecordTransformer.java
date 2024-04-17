package com.fluxtion.example.cookbook.dataingestion.node;

import com.fluxtion.example.cookbook.dataingestion.api.DataIngestConfig;
import com.fluxtion.example.cookbook.dataingestion.api.DataIngestConfigListener;
import com.fluxtion.example.cookbook.dataingestion.api.HouseData;
import com.fluxtion.runtime.annotations.ExportService;
import com.fluxtion.runtime.annotations.NoPropagateFunction;
import lombok.Getter;

@Getter
public class HouseDataRecordTransformer implements @ExportService(propagate = false) DataIngestConfigListener {

    private HouseData record;

    public HouseData transform(HouseData record) {
        System.out.println("RecordTransformer::transform: " + record);
        this.record = record;
        return this.record;
    }

    @Override
//    @NoPropagateFunction
    public boolean configUpdate(DataIngestConfig config) {
        return false;
    }
}
