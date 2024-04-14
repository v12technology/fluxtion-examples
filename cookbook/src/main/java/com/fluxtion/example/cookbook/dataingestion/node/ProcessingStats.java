package com.fluxtion.example.cookbook.dataingestion.node;

import com.fluxtion.example.cookbook.dataingestion.HouseInputRecord;

public class ProcessingStats {


    public void badCsvInput(CsvRecordValidator message){
        System.out.println("ProcessingStats::badCsvInput: ");
    }

    public void badHousingRecord(RecordValidator message){
        System.out.println("ProcessingStats::badHousingRecord: ");
    }

    public void validHousingRecord(HouseInputRecord message){
        System.out.println("ProcessingStats::validHousingRecord");
    }
}
