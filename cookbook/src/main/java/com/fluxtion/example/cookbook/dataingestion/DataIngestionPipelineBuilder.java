package com.fluxtion.example.cookbook.dataingestion;

import com.fluxtion.compiler.EventProcessorConfig;
import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.compiler.FluxtionCompilerConfig;
import com.fluxtion.compiler.FluxtionGraphBuilder;
import com.fluxtion.compiler.builder.dataflow.DataFlow;
import com.fluxtion.compiler.builder.dataflow.FlowBuilder;
import com.fluxtion.example.cookbook.dataingestion.api.HouseData;
import com.fluxtion.example.cookbook.dataingestion.function.*;

/**
 * Builds the data ingestion processing graph, invoked by the Fluxtion maven plugin to generate the pipeline AOT as
 * part of the build.
 *
 */
public class DataIngestionPipelineBuilder implements FluxtionGraphBuilder {

    @Override
    public void buildGraph(EventProcessorConfig eventProcessorConfig) {

        //flows Csv String -> HouseInputRecord -> x_formed(HouseInputRecord) -> houseRecordValidator(validate)
        var csvFlow = DataFlow.subscribe(String.class).map(new CsvHouseDataValidator()::marshall);
        var validXformedFlow = csvFlow.map(CsvHouseDataValidator::getHouseData)
                .map(new HouseDataRecordTransformer()::transform)
                .map(new HouseDataRecordValidator()::validate);

        //outputs
        var csvWriter = new HouseDataRecordCsvWriter();
        var binaryWriter = new HouseDataRecordBinaryWriter();
        var stats = new ProcessingStats();
        var invalidLog = new InvalidLog();

        //write validated output
        validXformedFlow.map(HouseDataRecordValidator::getRecord)
                .push(stats::validHousingRecord)
                .push(csvWriter::validHouseDataRecord)
                .push(binaryWriter::validHouseDataRecord);

        //invalid csv marshall
        csvFlow.filter(CsvHouseDataValidator::isInValidRecord)
                .push(invalidLog::badCsvRecord)
                .push(stats::badCsvRecord);

        //invalid transform
        validXformedFlow.filter(HouseDataRecordValidator::isInValidRecord)
                .push(invalidLog::badHouseDataRecord)
                .push(stats::badHouseDataRecord);
    }

    @Override
    public void configureGeneration(FluxtionCompilerConfig compilerConfig) {
        compilerConfig.setClassName("DataIngestionPipeline");
        compilerConfig.setPackageName("com.fluxtion.example.cookbook.dataingestion.generated");
    }

    //used for testing
    public static void main(String[] args) {
        Fluxtion.interpret(c -> {
            DataFlow.subscribe(String.class)
                    .map(new CsvHouseDataValidator()::csvToHouseData)
                    .map(new HouseDataRecordTransformer()::transform);
        });
    }
}
