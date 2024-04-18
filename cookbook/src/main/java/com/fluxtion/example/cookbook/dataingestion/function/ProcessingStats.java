package com.fluxtion.example.cookbook.dataingestion.function;

import com.fluxtion.compiler.replay.NullWriter;
import com.fluxtion.example.cookbook.dataingestion.api.*;
import com.fluxtion.runtime.annotations.ExportService;
import lombok.SneakyThrows;

import java.io.Writer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;

public class ProcessingStats
        implements
        DataIngestLifecycle,
        @ExportService(propagate = false) DataIngestStats {

    private int badCsvCount;
    private int goodRecordCount;
    private int badRecordCount;
    private Writer statsConsumer;
    private LocalDateTime startTime;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void init() {
        clearStats();
    }

    @Override
    public boolean configUpdate(DataIngestConfig config) {
        statsConsumer = config.getStatsWriter() == null ? NullWriter.NULL_WRITER : config.getStatsWriter();
        return false;
    }

    @SneakyThrows
    @Override
    public void publishStats() {
        statsConsumer.append(statsMessage());
        statsConsumer.flush();
    }

    @Override
    public void currentStats(Consumer<String> consumer) {
        consumer.accept(statsMessage());
    }

    @Override
    public void clearStats() {
        startTime = LocalDateTime.now();
        badCsvCount = 0;
        goodRecordCount = 0;
        badRecordCount = 0;
    }

    public void badCsvRecord(CsvToHouseRecordSerializer message) {
        badCsvCount++;
    }

    public void invalidHouseRecord(HouseRecordValidator message) {
        badRecordCount++;
    }

    public void validHouseRecord(HouseRecord message) {
        goodRecordCount++;
    }

    @Override
    public void tearDown() {
        System.out.println(statsMessage());
        publishStats();
    }

    private String statsMessage() {
        return """
                ProcessingStats:
                    startTime          : %s
                    endTime            : %s
                    badCsvCount        : %19d
                    invalidHouseRecord : %19d
                    validHouseRecord   : %19d
                    inputMessageCount  : %19d
                --------------------------------------------
                """.formatted(
                startTime.format(formatter), LocalDateTime.now().format(formatter),
                badCsvCount, badRecordCount, goodRecordCount, badCsvCount + goodRecordCount + badRecordCount);
    }
}
