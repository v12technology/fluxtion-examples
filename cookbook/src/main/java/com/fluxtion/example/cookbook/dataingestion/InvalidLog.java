package com.fluxtion.example.cookbook.dataingestion;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InvalidLog {

    public void badCsvInput(CsvRecordValidator message){
        System.out.println("InvalidLog::badCsvInput: ");
    }

    public void badHousingRecord(RecordTransformer message){
        System.out.println("InvalidLog::badHousingRecord: ");
    }
}
