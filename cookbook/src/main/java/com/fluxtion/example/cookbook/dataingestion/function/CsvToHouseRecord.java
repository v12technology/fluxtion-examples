package com.fluxtion.example.cookbook.dataingestion.function;

import com.fluxtion.example.cookbook.dataingestion.api.DataIngestLifecycle;
import com.fluxtion.example.cookbook.dataingestion.api.HouseRecord;
import com.fluxtion.extension.csvcompiler.CsvProcessingException;
import com.fluxtion.extension.csvcompiler.RowMarshaller;
import com.fluxtion.extension.csvcompiler.SingleRowMarshaller;
import com.fluxtion.extension.csvcompiler.ValidationLogger;
import lombok.Getter;


@Getter
public class CsvToHouseRecord implements DataIngestLifecycle {

    private SingleRowMarshaller<HouseRecord> houseDataCsvMarshaller;
    private boolean validRecord = false;
    private HouseRecord houseRecord;
    private CsvProcessingException processingException;
    private String inputString;

    @Override
    public void init() {
        houseDataCsvMarshaller = RowMarshaller.load(HouseRecord.class)
                .setValidationLogger(new ValidationLogger() {
                    @Override
                    public void logFatal(CsvProcessingException e) {
                        validRecord = false;
                        processingException = e;
                    }

                    @Override
                    public void logWarning(CsvProcessingException e) {
                        validRecord = false;
                        processingException = e;
                    }
                })
                .parser();
    }

    public CsvToHouseRecord marshall(String inputData) {
        validRecord = true;
        this.inputString = inputData + "\n";
        houseRecord = houseDataCsvMarshaller.parse(inputString);
        return this;
    }

    public boolean isInValidRecord() {
        return !validRecord;
    }
}
