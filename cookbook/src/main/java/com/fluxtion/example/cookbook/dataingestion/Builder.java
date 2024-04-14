package com.fluxtion.example.cookbook.dataingestion;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.compiler.builder.dataflow.DataFlow;
import com.fluxtion.runtime.audit.EventLogControlEvent;

import java.io.IOException;

public class Builder {

    public static void main(String[] args) throws IOException {

        var dataIngest = Fluxtion.interpret(c -> {
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
                    .peek(r -> {
                        if (!csvRecordValidator.isValidRecord()) {
                            System.out.println("PUSH bad csv record");
                            invalidLog.badCsvInput(csvRecordValidator);
                            stats.badCsvInput(csvRecordValidator);
                        }
                    })
                    .map(CsvRecordValidator::getHouseInputRecord)
                    .map(x_Former::transform)
                    .peek(r -> {
                        if (!x_Former.isValidRecord()) {
                            System.out.println("PUSH bad x-former record");
                            invalidLog.badHousingRecord(x_Former);
                            stats.badHousingRecord(x_Former);
                        }
                    })
                    .map(RecordTransformer::getRecord)
                    .push(stats::validHousingRecord)
                    .push(csvWriter::validHousingRecord)
                    .push(binaryWriter::validHousingRecord);

            DataFlow.subscribeToNode(csvRecordValidator);

            c.addEventAudit(EventLogControlEvent.LogLevel.INFO);
        });

        //Send some data
        dataIngest.init();
        dataIngest.setAuditLogLevel(EventLogControlEvent.LogLevel.DEBUG);

        dataIngest.onEvent("");
        System.out.println();

        dataIngest.onEvent("good");
        System.out.println();

        dataIngest.onEvent("BAD");
    }
}
