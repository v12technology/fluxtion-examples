package com.fluxtion.example.cookbook.dataingestion;

import com.fluxtion.compiler.EventProcessorConfig;
import com.fluxtion.compiler.FluxtionCompilerConfig;
import com.fluxtion.compiler.FluxtionGraphBuilder;
import com.fluxtion.compiler.builder.dataflow.DataFlow;
import com.fluxtion.example.cookbook.dataingestion.function.*;

/**
 * Builds the data ingestion processing graph, invoked by the Fluxtion maven plugin to generate the pipeline AOT as
 * part of the build.
 * <br>
 * <br>
 * This example using the Fluxtion {@link DataFlow} api to manage event subscription and notification
 * to user supplied functions.
 * <br>
 * <br>
 * The actual processing logic is encapsulated in user classes and functions. The goal is to have no Fluxtion api calls
 * in the business logic only pure vanilla java. The advantages of this approach:
 * <ul>
 *     <li>Business logic components are re-usable and testable</li>
 *     <li>Business code is not tied to a library api</li>
 *     <li>There is a clear separation between event notification and business logic</li>
 *     <li>The aot generated source file {@link com.fluxtion.example.cookbook.dataingestion.pipeline.DataIngestionPipeline} simplifies debugging</li>
 * </ul>
 *
 */
public class PipelineBuilder implements FluxtionGraphBuilder {

    @Override
    public void buildGraph(EventProcessorConfig eventProcessorConfig) {

        //flow: Csv String -> HouseInputRecord
        var csv2HouseRecordFlow = DataFlow
                .subscribe(String.class)
                .map(new CsvToHouseRecordSerializer()::marshall);

        //flow: HouseInputRecord -> x_formed(HouseInputRecord) -> validated(HouseInputRecord)
        var validTransformedFlow = csv2HouseRecordFlow
                .map(CsvToHouseRecordSerializer::getHouseRecord)
                .map(new HouseRecordTransformer()::transform)
                .map(new HouseRecordValidator()::validate);

        //outputs
        var csvWriter = new PostProcessCsvWriter();
        var binaryWriter = new PostProcessBinaryWriter();
        var stats = new ProcessingStats();
        var invalidLog = new InvalidLogWriter();

        //write validated output push to [stats, csv, binary]
        validTransformedFlow
                .map(HouseRecordValidator::getValidHouseRecord)
                .push(stats::validHouseRecord, csvWriter::validHouseRecord, binaryWriter::validHouseRecord);

        //invalid csv parsing output push to [invalid log, stats]
        csv2HouseRecordFlow
                .filter(CsvToHouseRecordSerializer::isBadCsvMessage)
                .push(invalidLog::badCsvRecord, stats::badCsvRecord);

        //invalid transform output push to [invalid log, stats]
        validTransformedFlow
                .filter(HouseRecordValidator::isInValidRecord)
                .push(invalidLog::invalidHouseRecord, stats::invalidHouseRecord);
    }

    @Override
    public void configureGeneration(FluxtionCompilerConfig compilerConfig) {
        compilerConfig.setClassName("DataIngestionPipeline");
        compilerConfig.setPackageName("com.fluxtion.example.cookbook.dataingestion.pipeline");
    }
}
