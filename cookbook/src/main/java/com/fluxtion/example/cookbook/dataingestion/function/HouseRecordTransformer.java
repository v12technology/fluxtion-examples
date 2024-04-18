package com.fluxtion.example.cookbook.dataingestion.function;

import com.fluxtion.example.cookbook.dataingestion.api.DataIngestConfig;
import com.fluxtion.example.cookbook.dataingestion.api.DataIngestComponent;
import com.fluxtion.example.cookbook.dataingestion.api.HouseRecord;
import com.fluxtion.runtime.annotations.ExportService;
import lombok.Getter;

import java.util.function.UnaryOperator;

@Getter
public class HouseRecordTransformer implements @ExportService(propagate = false) DataIngestComponent {

    private HouseRecord record;
    private UnaryOperator<HouseRecord> transformer = UnaryOperator.identity();

    public HouseRecord transform(HouseRecord houseRecord) {
        this.record = transformer.apply(houseRecord);
        return this.record;
    }

    @Override
    public boolean configUpdate(DataIngestConfig config) {
        transformer = config.getHouseTransformer() == null ? UnaryOperator.identity() : config.getHouseTransformer();
        return false;
    }
}
