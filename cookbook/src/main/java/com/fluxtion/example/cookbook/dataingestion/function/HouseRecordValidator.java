package com.fluxtion.example.cookbook.dataingestion.function;

import com.fluxtion.example.cookbook.dataingestion.api.HouseRecord;
import lombok.Getter;

@Getter
public class HouseRecordValidator {

    private boolean inValidRecord = false;
    private HouseRecord validHouseRecord;
    private HouseRecord inValidHouseRecord;

    public HouseRecordValidator validate(HouseRecord record){
        inValidRecord = record.MS_Zoning().equalsIgnoreCase("FV");
        this.validHouseRecord = inValidRecord ? null : record;
        this.inValidHouseRecord = inValidRecord ? record : null;
        return this;
    }
}
