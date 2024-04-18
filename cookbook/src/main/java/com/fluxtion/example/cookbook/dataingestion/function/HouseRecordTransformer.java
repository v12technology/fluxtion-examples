package com.fluxtion.example.cookbook.dataingestion.function;

import com.fluxtion.example.cookbook.dataingestion.api.DataIngestConfig;
import com.fluxtion.example.cookbook.dataingestion.api.DataIngestComponent;
import com.fluxtion.example.cookbook.dataingestion.api.HouseRecord;
import com.fluxtion.runtime.annotations.ExportService;
import lombok.Getter;

@Getter
public class HouseRecordTransformer implements @ExportService(propagate = false) DataIngestComponent {

    private HouseRecord record;

    public HouseRecord transform(HouseRecord record) {
        this.record = record;
        return this.record;
    }

    @Override
    public boolean configUpdate(DataIngestConfig config) {
        return false;
    }
}
