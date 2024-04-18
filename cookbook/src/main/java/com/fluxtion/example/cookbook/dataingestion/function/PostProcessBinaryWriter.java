package com.fluxtion.example.cookbook.dataingestion.function;

import com.fluxtion.compiler.replay.NullWriter;
import com.fluxtion.example.cookbook.dataingestion.api.DataIngestConfig;
import com.fluxtion.example.cookbook.dataingestion.api.DataIngestComponent;
import com.fluxtion.example.cookbook.dataingestion.api.DataIngestLifecycle;
import com.fluxtion.example.cookbook.dataingestion.api.HouseRecord;
import com.fluxtion.runtime.annotations.ExportService;
import lombok.SneakyThrows;

import java.io.DataOutputStream;
import java.io.OutputStream;

public class PostProcessBinaryWriter
        implements
        DataIngestLifecycle,
        @ExportService(propagate = false) DataIngestComponent {

    private DataOutputStream processedBinaryWriter;

    @Override
    public void init() {
        processedBinaryWriter = new DataOutputStream(OutputStream.nullOutputStream());
    }


    @SneakyThrows
    public void validHouseRecord(HouseRecord houseRecord) {
        processedBinaryWriter.writeInt(houseRecord.PID());
        processedBinaryWriter.writeInt(houseRecord.MS_SubClass());
        processedBinaryWriter.writeInt(houseRecord.Lot_Frontage_Squared());
        processedBinaryWriter.writeInt(houseRecord.ms_zone_category());
    }

    @Override
    public boolean configUpdate(DataIngestConfig config) {
        processedBinaryWriter = new DataOutputStream(
                config.getBinaryWriter() == null ? OutputStream.nullOutputStream() : config.getBinaryWriter());
        return false;
    }

    @Override
    @SneakyThrows
    public void tearDown() {
        processedBinaryWriter.flush();
    }
}
