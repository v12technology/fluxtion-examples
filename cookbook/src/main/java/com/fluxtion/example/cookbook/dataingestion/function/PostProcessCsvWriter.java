package com.fluxtion.example.cookbook.dataingestion.function;

import com.fluxtion.compiler.replay.NullWriter;
import com.fluxtion.example.cookbook.dataingestion.api.DataIngestConfig;
import com.fluxtion.example.cookbook.dataingestion.api.DataIngestComponent;
import com.fluxtion.example.cookbook.dataingestion.api.DataIngestLifecycle;
import com.fluxtion.example.cookbook.dataingestion.api.HouseRecord;
import com.fluxtion.extension.csvcompiler.RowMarshaller;
import com.fluxtion.runtime.annotations.ExportService;
import lombok.SneakyThrows;

import java.io.Writer;

public class PostProcessCsvWriter
        implements
        DataIngestLifecycle,
        @ExportService(propagate = false) DataIngestComponent {

    private Writer processedCsvWriter;
    private RowMarshaller<HouseRecord> houseRecordRowMarshaller;

    @Override
    public void init() {
        processedCsvWriter = NullWriter.NULL_WRITER;
        houseRecordRowMarshaller = RowMarshaller.load(HouseRecord.class);
    }

    @SneakyThrows
    public void validHouseRecord(HouseRecord houseRecord) {
        houseRecordRowMarshaller.writeRow(houseRecord, processedCsvWriter);
    }

    @Override
    @SneakyThrows
    public boolean configUpdate(DataIngestConfig config) {
        processedCsvWriter = config.getCsvWriter() == null ? NullWriter.NULL_WRITER : config.getCsvWriter();
        houseRecordRowMarshaller.writeHeaders(processedCsvWriter);
        return false;
    }

    @Override
    @SneakyThrows
    public void tearDown() {
        processedCsvWriter.flush();
    }
}
