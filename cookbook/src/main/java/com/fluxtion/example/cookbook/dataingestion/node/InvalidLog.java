package com.fluxtion.example.cookbook.dataingestion.node;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InvalidLog {

    public void badCsvInput(CsvRecordValidator message){
        System.out.println("InvalidLog::badCsvInput - " + message.getHouseInputRecord());
    }

    public void badHousingRecord(RecordValidator message){
        System.out.println("InvalidLog::badHousingRecord - " + message.getRecord());
    }
}
