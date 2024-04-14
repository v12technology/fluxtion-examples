package com.fluxtion.example.cookbook.dataingestion;

import lombok.Getter;

public class CsvRecordValidator {

//    @Getter
    private boolean inValidRecord = false;

    public HouseInputRecord marshall(String inputData) {
        inValidRecord = inputData == null || inputData.isBlank();
        System.out.println("CsvRecordValidator::marshall inputData: " + inputData + ", inValidRecord: " + inValidRecord);
        return inValidRecord ? null: new HouseInputRecord();
    }

    public boolean isInValidRecord() {
        return inValidRecord;
    }
}
