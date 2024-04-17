package com.fluxtion.example.cookbook.dataingestion.function;

import com.fluxtion.example.cookbook.dataingestion.api.HouseData;
import lombok.Getter;

@Getter
public class HouseDataRecordValidator {

    private boolean inValidRecord = false;
    private HouseData record;

    public HouseDataRecordValidator validate(HouseData record){
        System.out.println("RecordValidator::validate: " + record);
        inValidRecord = record.MS_Zoning().equalsIgnoreCase("BAD");
        this.record = inValidRecord ? null : record;
        return this;
    }
}
