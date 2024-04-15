package com.fluxtion.example.cookbook.dataingestion.node;

import com.fluxtion.example.cookbook.dataingestion.HouseData;
import lombok.Getter;

@Getter
public class CsvHouseDataValidator {

    private boolean validRecord = false;
    private HouseData houseData;

    public CsvHouseDataValidator marshall(String inputData) {
        validRecord = inputData != null && !inputData.isBlank();
        System.out.println("CsvRecordValidator::marshall inputData: " + inputData + ", validRecord: " + validRecord);
        houseData = validRecord ? new HouseData() : null;
        if(validRecord) {
            houseData.MS_Zoning(inputData);
        }
        return this;
    }

    public boolean isInValidRecord() {
        return !validRecord;
    }
}
