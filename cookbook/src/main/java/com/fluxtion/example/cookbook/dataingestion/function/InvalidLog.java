package com.fluxtion.example.cookbook.dataingestion.function;

import com.fluxtion.example.cookbook.dataingestion.api.DataIngestConfig;
import com.fluxtion.example.cookbook.dataingestion.api.DataIngestComponent;
import com.fluxtion.example.cookbook.dataingestion.api.DataIngestLifecycle;
import com.fluxtion.runtime.annotations.ExportService;

public class InvalidLog
        implements
        DataIngestLifecycle,
        @ExportService(propagate = false) DataIngestComponent {

    public void badCsvRecord(CsvToHouseRecord message) {
        System.out.println("InvalidLog::badCsvRecord - " + message.getProcessingException() + " msg[" + message.getInputString() + "]");
    }

    public void invalidHouseRecord(HouseRecordValidator message) {
        System.out.println("InvalidLog::invalidHouseRecord - " + message.getInValidHouseRecord());
    }

    @Override
    public boolean configUpdate(DataIngestConfig config) {
        return false;
    }

    @Override
    public void tearDown() {

    }
}