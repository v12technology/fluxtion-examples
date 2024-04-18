package com.fluxtion.example.cookbook.dataingestion.function;

import com.fluxtion.compiler.replay.NullWriter;
import com.fluxtion.example.cookbook.dataingestion.api.DataIngestConfig;
import com.fluxtion.example.cookbook.dataingestion.api.DataIngestComponent;
import com.fluxtion.example.cookbook.dataingestion.api.DataIngestLifecycle;
import com.fluxtion.runtime.annotations.ExportService;
import lombok.SneakyThrows;

import java.io.Writer;

public class InvalidLogWriter
        implements
        DataIngestLifecycle,
        @ExportService(propagate = false) DataIngestComponent {

    private Writer logWriter;

    @Override
    public void init() {
        logWriter = NullWriter.NULL_WRITER;
    }

    @Override
    public boolean configUpdate(DataIngestConfig config) {
        logWriter = config.getInvalidLogWriter() == null ? NullWriter.NULL_WRITER : config.getInvalidLogWriter();
        return false;
    }

    @SneakyThrows
    public void badCsvRecord(CsvToHouseRecordSerializer message) {
        String inputString = message.getInputString().substring(0, message.getInputString().length() - 1);
        logWriter.append("csv error " + message.getProcessingException().getMessage() + " msg[" + inputString + "]\n");
    }

    @SneakyThrows
    public void invalidHouseRecord(HouseRecordValidator message) {
        logWriter.append("validation error record[" + message.getInValidHouseRecord() + "]\n");
    }

    @Override
    @SneakyThrows
    public void tearDown() {
        logWriter.flush();
    }
}
