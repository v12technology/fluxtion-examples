package com.fluxtion.example.cookbook.ml.linearregression.wekamodel;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.compiler.builder.dataflow.DataFlow;
import com.fluxtion.example.cookbook.ml.linearregression.api.HouseSaleDetails;
import com.fluxtion.example.cookbook.ml.linearregression.api.HouseSalesDetailsPostProcess;
import com.fluxtion.example.cookbook.ml.linearregression.pipeline.LocationCategoryFeature;
import com.fluxtion.example.cookbook.ml.linearregression.pipeline.PreProcessPipeline;
import com.fluxtion.extension.csvcompiler.RowMarshaller;
import com.fluxtion.runtime.EventProcessor;
import com.fluxtion.runtime.ml.CalibrationProcessor;
import com.fluxtion.runtime.ml.PredictiveModel;
import lombok.SneakyThrows;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.UUID;

public class GenerateData {

    private static FileWriter outputWriter;
    //CSV marshalling
    private static final RowMarshaller<HouseSaleDetails> marshallerInput = RowMarshaller.load(HouseSaleDetails.class);
    //CSV marshalling
    private static final RowMarshaller<HouseSalesDetailsPostProcess> marshallerOutput = RowMarshaller.load(HouseSalesDetailsPostProcess.class);
    private static EventProcessor<?> preProcessPipeline;
    private static final String resourcesDir = "src/main/resources/data/ml/linear_regression/";
    private static final Random random = new Random(123);

    public static void main(String[] args) throws Exception {
        preProcessPipeline = Fluxtion.interpret(c -> DataFlow.subscribeToNode(PreProcessPipeline.buildScoringPipeline(c))
                        .push(GenerateData::writeRow));
        preProcessPipeline.init();
        preProcessPipeline.getExportedService(CalibrationProcessor.class).resetToOne();
        generateInputCsv("input1.csv", 100);
        generateInputCsv("input2.csv", 45);
        generateOutput("input1.csv", "output1.csv");
        generateOutput("input2.csv", "output2.csv");
    }

    private static void generateInputCsv(String file, int count) throws IOException {
        try (FileWriter writer = new FileWriter(resourcesDir + file)) {
            marshallerInput.writeHeaders(writer);
            for (int i = 0; i < count; i++) {
                marshallerInput.writeRow(randomHouseForSaleAdvert(), writer);
            }
        }
    }

    private static void generateOutput(String inFile, String outFile) throws IOException {
        try (FileWriter sw = new FileWriter(resourcesDir + outFile)) {
            outputWriter = sw;
            marshallerOutput.writeHeaders(sw);
            marshallerInput.stream(new FileReader(resourcesDir + inFile)).forEach(preProcessPipeline::onEvent);
        }
    }

    @SneakyThrows
    public static void writeRow(PredictiveModel model) {
        HouseSalesDetailsPostProcess postProcess = new HouseSalesDetailsPostProcess();
        model.features().forEach(f -> {
            switch (f) {
                case LocationCategoryFeature feature -> postProcess.setLocationCategory((int) feature.value());
                default -> {
                    switch (f.getName()) {
                        case "offerPrice" -> postProcess.setSalePrice(f.value());
                        case "area" -> postProcess.setArea(f.value());
                        case "bedroom" -> postProcess.setBedrooms((int) f.value());
                        case "areaSquared" -> postProcess.setAreaSquared(f.value());
                        default -> {
                        }
                    }
                }
            }
        });
        marshallerOutput.writeRow(postProcess, outputWriter);
    }

    private static HouseSaleDetails randomHouseForSaleAdvert() {
        return new HouseSaleDetails(((char) (random.nextInt(65, 75))) + "" + random.nextInt(200),
                random.nextInt(50, 800),
                random.nextInt(1, 7),
                random.nextInt(35_000, 3_500_000),
                random.nextInt(35_000, 3_500_000),
                UUID.randomUUID().toString());
    }
}
