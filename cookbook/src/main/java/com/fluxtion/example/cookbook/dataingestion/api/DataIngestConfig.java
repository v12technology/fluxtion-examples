package com.fluxtion.example.cookbook.dataingestion.api;

import lombok.Builder;
import lombok.Data;

import java.io.OutputStream;
import java.io.Writer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

@Data
@Builder
public final class DataIngestConfig {
    private final Predicate<HouseRecord> houseRecordValidator;
    private final UnaryOperator<HouseRecord> houseTransformer;
    private final OutputStream binaryWriter;
    private final Writer csvWriter;
    private final Writer statsWriter;
    private final Writer invalidLogWriter;

}
