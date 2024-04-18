package com.fluxtion.example.cookbook.dataingestion.function;

import com.fluxtion.example.cookbook.dataingestion.api.DataIngestConfig;
import com.fluxtion.example.cookbook.dataingestion.api.DataIngestComponent;
import com.fluxtion.example.cookbook.dataingestion.api.DataIngestLifecycle;
import com.fluxtion.example.cookbook.dataingestion.api.HouseRecord;
import com.fluxtion.runtime.annotations.ExportService;

public class HouseRecordCsvWriter
        implements
        DataIngestLifecycle,
        @ExportService(propagate = false) DataIngestComponent {

    public void validHouseRecord(HouseRecord message) {
    }

    @Override
    public boolean configUpdate(DataIngestConfig config) {
        return false;
    }

    @Override
    public void tearDown() {
    }
}
