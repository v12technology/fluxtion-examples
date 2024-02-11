package com.fluxtion.example.cookbook.ml.linearregression.wekamodel;

import com.fluxtion.extension.csvcompiler.annotations.ColumnMapping;
import com.fluxtion.extension.csvcompiler.annotations.CsvMarshaller;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@CsvMarshaller(fluent = true)
@Accessors(fluent = true)
public class HousingData {
    //input only
    @ColumnMapping(outputField = false)
    private int Order;
    @ColumnMapping(outputField = false, columnName = "Lot Frontage", defaultValue = "-1")
    private int Lot_Frontage;
    @ColumnMapping(outputField = false, columnName = "MS Zoning")
    private String MS_Zoning;

    //input and output
    private int PID;
    @ColumnMapping(columnName = "MS SubClass")
    private int MS_SubClass;

    //derived
    @ColumnMapping(optionalField = true)
    private int Lot_Frontage_Squared;
    @ColumnMapping(optionalField = true)
    private int ms_zone_category;
}
