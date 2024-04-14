package com.fluxtion.example.cookbook.dataingestion.builder;

import com.fluxtion.compiler.EventProcessorConfig;
import com.fluxtion.compiler.FluxtionCompilerConfig;
import com.fluxtion.compiler.FluxtionGraphBuilder;
import com.fluxtion.compiler.builder.dataflow.DataFlow;
import com.fluxtion.example.cookbook.dataingestion.node.*;

public class DataIngestionBuilder implements FluxtionGraphBuilder {
    @Override
    public void buildGraph(EventProcessorConfig eventProcessorConfig) {
        //flows Csv String -> HouseInputRecord -> x_formed(HouseInputRecord)
        var csvFlow = DataFlow.subscribe(String.class).map(new CsvRecordValidator()::marshall);
        var validXformedFlow = csvFlow.map(CsvRecordValidator::getHouseInputRecord)
                .map(new RecordTransformer()::transform)
                .map(new RecordValidator()::validate);

        //outputs
        var csvWriter = new RecordCsvWriter();
        var binaryWriter = new RecordBinaryWriter();
        var stats = new ProcessingStats();
        var invalidLog = new InvalidLog();

        //write validated output
        validXformedFlow.map(RecordValidator::getRecord)
                .push(stats::validHousingRecord)
                .push(csvWriter::validHousingRecord)
                .push(binaryWriter::validHousingRecord);

        //invalid csv marshall
        csvFlow.filter(CsvRecordValidator::isInValidRecord)
                .push(invalidLog::badCsvInput)
                .push(stats::badCsvInput);

        //invalid transform
        validXformedFlow.filter(RecordValidator::isInValidRecord)
                .push(invalidLog::badHousingRecord)
                .push(stats::badHousingRecord);
    }

    @Override
    public void configureGeneration(FluxtionCompilerConfig compilerConfig) {
        compilerConfig.setClassName("DataIngestion");
        compilerConfig.setPackageName("com.fluxtion.example.cookbook.dataingestion.generated");
    }
}
