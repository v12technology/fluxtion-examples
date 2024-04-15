package com.fluxtion.example.cookbook.dataingestion.node;

import com.fluxtion.example.cookbook.dataingestion.HouseData;
import lombok.Getter;

public class HouseDataRecordValidator {

    @Getter
    private boolean inValidRecord = false;
    @Getter
    private HouseData record;

    public HouseDataRecordValidator validate(HouseData record){
        System.out.println("RecordValidator::validate: " + record);
        inValidRecord = record.MS_Zoning().equalsIgnoreCase("BAD");
        this.record = inValidRecord ? null : record;
        return this;
    }
}
