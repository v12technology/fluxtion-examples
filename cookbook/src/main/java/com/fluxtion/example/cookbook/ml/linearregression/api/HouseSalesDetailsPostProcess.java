package com.fluxtion.example.cookbook.ml.linearregression.api;

import com.fluxtion.extension.csvcompiler.annotations.CsvMarshaller;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@CsvMarshaller
@AllArgsConstructor
@NoArgsConstructor
public final class HouseSalesDetailsPostProcess {
    private double area;
    private double areaSquared;
    private int bedrooms;
    private int locationCategory;
    private double salePrice;
}
