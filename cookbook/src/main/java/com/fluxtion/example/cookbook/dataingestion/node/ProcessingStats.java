package com.fluxtion.example.cookbook.dataingestion.node;

import com.fluxtion.example.cookbook.dataingestion.HouseInputRecord;

public class ProcessingStats {


    public void badCsvInput(CsvRecordValidator message){
        System.out.println("ProcessingStats::badCsvInput - " + message.getHouseInputRecord());
    }

    public void badHousingRecord(RecordValidator message){
        System.out.println("ProcessingStats::badHousingRecord - " + message.getRecord());
    }

    public void validHousingRecord(HouseInputRecord message){
        System.out.println("ProcessingStats::validHousingRecord - " + message);
    }
}
