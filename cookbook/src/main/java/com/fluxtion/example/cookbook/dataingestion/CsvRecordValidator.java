package com.fluxtion.example.cookbook.dataingestion;

import lombok.Getter;

public class CsvRecordValidator {

    @Getter
    private boolean validRecord = false;
    @Getter
    private HouseInputRecord houseInputRecord;

    public CsvRecordValidator marshall(String inputData) {
        validRecord = inputData != null && !inputData.isBlank();
        System.out.println("CsvRecordValidator::marshall inputData: " + inputData + ", validRecord: " + validRecord);
        houseInputRecord = validRecord ? new HouseInputRecord() : null;
        if(validRecord) {
            houseInputRecord.setHouseId(inputData);
        }
        return this;
    }

//    public boolean isInValidRecord() {
//        return inValidRecord;
//    }
}
