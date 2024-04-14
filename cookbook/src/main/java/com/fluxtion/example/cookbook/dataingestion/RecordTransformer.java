package com.fluxtion.example.cookbook.dataingestion;

import lombok.Getter;

public class RecordTransformer {


    @Getter
    private boolean validRecord = false;
    @Getter
    private HouseInputRecord record;

    public RecordTransformer transform(HouseInputRecord record) {
        System.out.println("RecordTransformer::transform: " + record);
        validRecord = !record.getHouseId().equalsIgnoreCase("BAD");
        this.record = validRecord ? record : null;
        return this;
    }

}
