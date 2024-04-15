package com.fluxtion.example.cookbook.dataingestion.node;

import com.fluxtion.example.cookbook.dataingestion.HouseData;
import lombok.Getter;

@Getter
public class HouseDataRecordTransformer {

    private HouseData record;

    public HouseData transform(HouseData record) {
        System.out.println("RecordTransformer::transform: " + record);
        this.record = record;
        return this.record;
    }
}
