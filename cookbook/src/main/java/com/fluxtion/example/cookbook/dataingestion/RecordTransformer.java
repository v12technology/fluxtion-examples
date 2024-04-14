package com.fluxtion.example.cookbook.dataingestion;

import lombok.Getter;

public class RecordTransformer {


    @Getter
    private boolean inValidRecord = false;

    public HouseInputRecord transform(HouseInputRecord record) {
        System.out.println("RecordTransformer::transform: " + record);
        return record;
    }

}
