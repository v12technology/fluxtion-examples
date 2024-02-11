package com.fluxtion.example.cookbook.ml.linearregression.wekamodel;

import com.fluxtion.extension.csvcompiler.RowMarshaller;
import lombok.SneakyThrows;

import java.nio.file.Path;

public class MainTest {

    @SneakyThrows
    public static void main(String[] args) {
        RowMarshaller.transform(
                HousingData.class,
                Path.of("data/ml/linear_regression/AmesHousing.csv"),
                Path.of("data/ml/linear_regression/PostProcess.csv"),
                s -> s.filter(h -> h.Lot_Frontage() > 0)
                        .map(MainTest::squareLotFrontage)
                        .map(MainTest::ms_zone_to_category)
                        .filter(h -> h.ms_zone_category() > 0));
    }

    public static HousingData squareLotFrontage(HousingData housingData) {
        int lotFrontage = housingData.Lot_Frontage();
        housingData.Lot_Frontage_Squared(lotFrontage * lotFrontage);
        return housingData;
    }

    public static HousingData ms_zone_to_category(HousingData housingData) {
        switch (housingData.MS_Zoning()) {
            case "A" -> housingData.ms_zone_category(1);
            case "FV" -> housingData.ms_zone_category(2);
            case "RL" -> housingData.ms_zone_category(3);
            case "RM" -> housingData.ms_zone_category(4);
            default -> housingData.ms_zone_category(-1);
        }
        return housingData;
    }
}
