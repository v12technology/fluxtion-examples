package com.fluxtion.example.cookbook.dataingestion.node;

import com.fluxtion.example.cookbook.dataingestion.HouseInputRecord;
import lombok.Getter;

public class RecordValidator {

    @Getter
    private boolean inValidRecord = false;
    @Getter
    private HouseInputRecord record;

    public RecordValidator validate(HouseInputRecord record){
        System.out.println("RecordValidator::validate: " + record);
        inValidRecord = record.getHouseId().equalsIgnoreCase("BAD");
        this.record = inValidRecord ? null : record;
        return this;
    }
}
