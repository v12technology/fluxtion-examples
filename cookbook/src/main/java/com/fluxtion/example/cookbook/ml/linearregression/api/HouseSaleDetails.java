package com.fluxtion.example.cookbook.ml.linearregression.api;

import com.fluxtion.extension.csvcompiler.annotations.CsvMarshaller;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@CsvMarshaller
public final class HouseSaleDetails {
    private String locationZip;
    private double area;
    private int bedrooms;
    private double offerPrice;
    private double soldPrice;
    private String id;

    public HouseSaleDetails(String locationZip, double area, int bedrooms, double offerPrice, String id) {
        this.locationZip = locationZip;
        this.area = area;
        this.bedrooms = bedrooms;
        this.offerPrice = offerPrice;
        this.id = id;
    }
}
