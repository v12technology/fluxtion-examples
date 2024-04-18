package com.fluxtion.example.cookbook.dataingestion.function;

import com.fluxtion.example.cookbook.dataingestion.api.DataIngestLifecycle;
import com.fluxtion.example.cookbook.dataingestion.api.HouseRecord;

public class ProcessingStats implements DataIngestLifecycle {

    private int badCsvCount;
    private int goodRecordCount;
    private int badRecordCount;

    public void badCsvRecord(CsvToHouseRecord message){
        badCsvCount++;
    }

    public void invalidHouseRecord(HouseRecordValidator message){
        badRecordCount++;
    }

    public void validHouseRecord(HouseRecord message){
        goodRecordCount++;
    }

    @Override
    public void tearDown() {
        System.out.println("ProcessingStats::badCsvCount - " + badCsvCount);
        System.out.println("ProcessingStats::invalidHouseRecord - " + badRecordCount);
        System.out.println("ProcessingStats::validHouseRecord - " + goodRecordCount);
    }
}
