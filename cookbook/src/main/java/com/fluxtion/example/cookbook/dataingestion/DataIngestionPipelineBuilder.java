package com.fluxtion.example.cookbook.dataingestion;

import com.fluxtion.compiler.EventProcessorConfig;
import com.fluxtion.compiler.FluxtionCompilerConfig;
import com.fluxtion.compiler.FluxtionGraphBuilder;
import com.fluxtion.compiler.builder.dataflow.DataFlow;
import com.fluxtion.example.cookbook.dataingestion.function.*;

/**
 * Builds the data ingestion processing graph, invoked by the Fluxtion maven plugin to generate the pipeline AOT as
 * part of the build.
 */
public class DataIngestionPipelineBuilder implements FluxtionGraphBuilder {

    @Override
    public void buildGraph(EventProcessorConfig eventProcessorConfig) {

        //flow: Csv String -> HouseInputRecord
        var csv2HouseRecordFlow = DataFlow
                .subscribe(String.class)
                .map(new CsvToHouseRecord()::marshall);

        //flow: HouseInputRecord -> x_formed(HouseInputRecord) -> validated(HouseInputRecord)
        var validTransformedFlow = csv2HouseRecordFlow
                .map(CsvToHouseRecord::getHouseRecord)
                .map(new HouseRecordTransformer()::transform)
                .map(new HouseRecordValidator()::validate);

        //outputs
        var csvWriter = new HouseRecordCsvWriter();
        var binaryWriter = new HouseRecordBinaryWriter();
        var stats = new ProcessingStats();
        var invalidLog = new InvalidLog();

        //write validated output push to [stats, csv, binary]
        validTransformedFlow
                .map(HouseRecordValidator::getValidHouseRecord)
                .push(stats::validHouseRecord, csvWriter::validHouseRecord, binaryWriter::validHouseRecord);

        //invalid csv parsing output push to [invalid log, stats]
        csv2HouseRecordFlow
                .filter(CsvToHouseRecord::isBadCsvMessage)
                .push(invalidLog::badCsvRecord, stats::badCsvRecord);

        //invalid transform output push to [invalid log, stats]
        validTransformedFlow
                .filter(HouseRecordValidator::isInValidRecord)
                .push(invalidLog::invalidHouseRecord, stats::invalidHouseRecord);
    }

    @Override
    public void configureGeneration(FluxtionCompilerConfig compilerConfig) {
        compilerConfig.setClassName("DataIngestionPipeline");
        compilerConfig.setPackageName("com.fluxtion.example.cookbook.dataingestion.generated");
    }
}
