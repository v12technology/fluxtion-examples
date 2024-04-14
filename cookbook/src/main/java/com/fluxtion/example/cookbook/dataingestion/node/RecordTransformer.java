package com.fluxtion.example.cookbook.dataingestion.node;

import com.fluxtion.example.cookbook.dataingestion.HouseInputRecord;
import lombok.Getter;

@Getter
public class RecordTransformer {

    private HouseInputRecord record;

    public HouseInputRecord transform(HouseInputRecord record) {
        System.out.println("RecordTransformer::transform: " + record);
        this.record = record;
        return this.record;
    }
}
