package com.fluxtion.example.cookbook.ml.linearregression.wekamodel;

import com.fluxtion.extension.csvcompiler.annotations.ColumnMapping;
import com.fluxtion.extension.csvcompiler.annotations.CsvMarshaller;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@CsvMarshaller
@Accessors(fluent = true)
public class HousingData {
    private int Order;
    private int PID;
    @ColumnMapping(columnName = "MS SubClass")
    private String MS_SubClass;
}
