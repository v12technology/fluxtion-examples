package com.fluxtion.example.cookbook.dataingestion;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.compiler.builder.dataflow.DataFlow;
import com.fluxtion.compiler.builder.dataflow.FlowBuilder;

public class Builder {

    public static void main(String[] args) {
        var dataIngestor =Fluxtion.interpret(c ->{
            var csvRecordValidator = new CsvRecordValidator();
            var x_Former = new RecordTransformer();
            //outputs
            var csvWriter = new RecordCsvWriter();
            var binaryWriter = new RecordBinaryWriter();
            var stats = new ProcessingStats();
            var invalidLog = new InvalidLog();

            //flows Csv String -> HouseInputRecord -> x_formed(HouseInputRecord)
            var recordFlow = DataFlow.subscribe(String.class);
            recordFlow
                    .map(csvRecordValidator::marshall)
                    .map(x_Former::transform)
                    .push(stats::validHousingRecord)
                    .push(csvWriter::validHousingRecord)
                    .push(binaryWriter::validHousingRecord);

            //invalid flow - csv
            recordFlow
                    .filter(csvRecordValidator::isInValidRecord)
                    .peek(i -> {
                        invalidLog.badCsvInput(csvRecordValidator);
                        stats.badCsvInput(csvRecordValidator);
                    });

            //invalid flow - x-former
            recordFlow
                    .filter(x_Former::isInValidRecord)
                    .peek(i -> {
                        invalidLog.badCsvInput(csvRecordValidator);
                        stats.badCsvInput(csvRecordValidator);
                    });
        });

        //
        dataIngestor.init();
        dataIngestor.onEvent("");
        dataIngestor.onEvent("");
        dataIngestor.onEvent("MyString");
    }
}
