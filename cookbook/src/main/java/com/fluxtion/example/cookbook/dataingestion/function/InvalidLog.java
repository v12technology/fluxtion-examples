package com.fluxtion.example.cookbook.dataingestion.function;

import com.fluxtion.example.cookbook.dataingestion.api.DataIngestConfig;
import com.fluxtion.example.cookbook.dataingestion.api.DataIngestConfigListener;
import com.fluxtion.runtime.annotations.ExportService;

public class InvalidLog implements @ExportService(propagate = false) DataIngestConfigListener {

    public void badCsvRecord(CsvHouseDataValidator message){
        System.out.println("InvalidLog::badCsvInput - " + message.getHouseData());
    }

    public void badHouseDataRecord(HouseDataRecordValidator message){
        System.out.println("InvalidLog::badHousingRecord - " + message.getRecord());
    }

    @Override
    public boolean configUpdate(DataIngestConfig config) {
        return false;
    }
}
