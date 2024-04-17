package com.fluxtion.example.cookbook.dataingestion.function;

import com.fluxtion.example.cookbook.dataingestion.api.DataIngestConfig;
import com.fluxtion.example.cookbook.dataingestion.api.DataIngestConfigListener;
import com.fluxtion.example.cookbook.dataingestion.api.HouseData;
import com.fluxtion.runtime.annotations.ExportService;

public class HouseDataRecordCsvWriter implements @ExportService(propagate = false) DataIngestConfigListener {

    public void validHouseDataRecord(HouseData message){
        System.out.println("RecordCsvWriter::validHousingRecord - " + message);
    }

    @Override
    public boolean configUpdate(DataIngestConfig config) {
        return false;
    }
}
