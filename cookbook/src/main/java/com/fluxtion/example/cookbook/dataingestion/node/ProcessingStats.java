package com.fluxtion.example.cookbook.dataingestion.node;

import com.fluxtion.example.cookbook.dataingestion.HouseData;

public class ProcessingStats {

    public void badCsvRecord(CsvHouseDataValidator message){
        System.out.println("ProcessingStats::badCsvInput - " + message.getHouseData());
    }

    public void badHouseDataRecord(HouseDataRecordValidator message){
        System.out.println("ProcessingStats::badHousingRecord - " + message.getRecord());
    }

    public void validHousingRecord(HouseData message){
        System.out.println("ProcessingStats::validHousingRecord - " + message);
    }
}
