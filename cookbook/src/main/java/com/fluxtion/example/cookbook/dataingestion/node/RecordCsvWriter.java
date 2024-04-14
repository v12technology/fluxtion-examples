package com.fluxtion.example.cookbook.dataingestion.node;

import com.fluxtion.example.cookbook.dataingestion.HouseInputRecord;

public class RecordCsvWriter {

    public void validHousingRecord(HouseInputRecord message){
        System.out.println("RecordCsvWriter::validHousingRecord - " + message);
    }
}
