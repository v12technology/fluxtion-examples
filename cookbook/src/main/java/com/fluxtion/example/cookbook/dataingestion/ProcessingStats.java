package com.fluxtion.example.cookbook.dataingestion;

public class ProcessingStats {


    public void badCsvInput(CsvRecordValidator message){
        System.out.println("ProcessingStats::badCsvInput: ");
    }

    public void badHousingRecord(RecordTransformer message){
        System.out.println("ProcessingStats::badHousingRecord: ");
    }

    public void validHousingRecord(HouseInputRecord message){
        System.out.println("ProcessingStats::validHousingRecord");
    }
}
