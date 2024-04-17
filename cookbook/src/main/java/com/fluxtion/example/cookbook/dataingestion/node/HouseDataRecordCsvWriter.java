package com.fluxtion.example.cookbook.dataingestion.node;

import com.fluxtion.example.cookbook.dataingestion.api.DataIngestConfig;
import com.fluxtion.example.cookbook.dataingestion.api.DataIngestConfigListener;
import com.fluxtion.example.cookbook.dataingestion.api.HouseData;
import com.fluxtion.runtime.annotations.ExportService;
import com.fluxtion.runtime.annotations.NoPropagateFunction;

public class HouseDataRecordCsvWriter implements @ExportService DataIngestConfigListener {

    public void validHouseDataRecord(HouseData message){
        System.out.println("RecordCsvWriter::validHousingRecord - " + message);
    }

    @Override
    @NoPropagateFunction
    public boolean configUpdate(DataIngestConfig config) {
        return false;
    }
}
