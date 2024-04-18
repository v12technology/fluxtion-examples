package com.fluxtion.example.cookbook.dataingestion.function;

import com.fluxtion.example.cookbook.dataingestion.api.DataIngestComponent;
import com.fluxtion.example.cookbook.dataingestion.api.DataIngestConfig;
import com.fluxtion.example.cookbook.dataingestion.api.DataIngestLifecycle;
import com.fluxtion.example.cookbook.dataingestion.api.HouseRecord;
import com.fluxtion.runtime.annotations.ExportService;
import lombok.Getter;

import java.util.function.Predicate;

@Getter
public class HouseRecordValidator
        implements
        DataIngestLifecycle,
        @ExportService(propagate = false) DataIngestComponent {

    private boolean inValidRecord = false;
    private HouseRecord validHouseRecord;
    private HouseRecord inValidHouseRecord;
    private Predicate<HouseRecord> validator;

    @Override
    public void init() {
        validator = h -> true;
    }

    @Override
    public boolean configUpdate(DataIngestConfig config) {
        validator = config.getHouseRecordValidator() == null ? h -> true : config.getHouseRecordValidator();
        return false;
    }

    public HouseRecordValidator validate(HouseRecord record) {
        inValidRecord = record.MS_Zoning().equalsIgnoreCase("FV");
        this.validHouseRecord = inValidRecord ? null : record;
        this.inValidHouseRecord = inValidRecord ? record : null;
        return this;
    }
}
