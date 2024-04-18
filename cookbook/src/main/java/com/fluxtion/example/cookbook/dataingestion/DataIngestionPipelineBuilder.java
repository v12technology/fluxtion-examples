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

        //flows Csv String -> HouseInputRecord -> x_formed(HouseInputRecord) -> houseRecordValidator(validate)
        var csvFlow = DataFlow.subscribe(String.class).map(new CsvToHouseRecord()::marshall);
        //flows HouseInputRecord -> x_formed(HouseInputRecord) -> houseRecordValidator(validate)
        var validXformedFlow = csvFlow.map(CsvToHouseRecord::getHouseRecord)
                .map(new HouseRecordTransformer()::transform)
                .map(new HouseRecordValidator()::validate);

        //outputs
        var csvWriter = new HouseRecordCsvWriter();
        var binaryWriter = new HouseRecordBinaryWriter();
        var stats = new ProcessingStats();
        var invalidLog = new InvalidLog();

        //write validated output to [stats, csv, binary]
        validXformedFlow.map(HouseRecordValidator::getValidHouseRecord)
                .push(stats::validHouseRecord, csvWriter::validHouseRecord, binaryWriter::validHouseRecord);

        //invalid csv parsing output to [invalid log, stats]
        var invalidCsv = csvFlow.filter(CsvToHouseRecord::isInValidRecord);
        invalidCsv.push(invalidLog::badCsvRecord);
        invalidCsv.push(stats::badCsvRecord);

        //invalid transform output to [invalid log, stats]
        var invalidHouseRecord = validXformedFlow.filter(HouseRecordValidator::isInValidRecord);
        invalidHouseRecord.push(invalidLog::invalidHouseRecord);
        invalidHouseRecord.push(stats::invalidHouseRecord);
    }

    @Override
    public void configureGeneration(FluxtionCompilerConfig compilerConfig) {
        compilerConfig.setClassName("DataIngestionPipeline");
        compilerConfig.setPackageName("com.fluxtion.example.cookbook.dataingestion.generated");
    }
}
