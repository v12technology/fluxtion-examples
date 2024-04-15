package com.fluxtion.example.cookbook.dataingestion.node;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InvalidLog {

    public void badCsvRecord(CsvHouseDataValidator message){
        System.out.println("InvalidLog::badCsvInput - " + message.getHouseData());
    }

    public void badHouseDataRecord(HouseDataRecordValidator message){
        System.out.println("InvalidLog::badHousingRecord - " + message.getRecord());
    }
}
