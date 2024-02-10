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

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.UUID;

public class GenerateData {

    public static final String dataDir = "data/ml/linear_regression/";
    public static final Path dataDirPath = Paths.get(dataDir);
    private static Writer outputWriter;
    //CSV marshalling
    private static final RowMarshaller<HouseSaleDetails> marshallerInput = RowMarshaller.load(HouseSaleDetails.class);
    //CSV marshalling
    private static final RowMarshaller<HouseSalesDetailsPostProcess> marshallerOutput = RowMarshaller.load(HouseSalesDetailsPostProcess.class);
    private static EventProcessor<?> preProcessPipeline;
    private static final Random random = new Random(123);

    public static void main(String[] args) throws Exception {
        buildPreProcessPipeline();
        generatePreProcessInputCsv("inputHouseSales_1.csv", 100);
        generatePreProcessInputCsv("inputHouseSales_2.csv", 45);
        generatePostProcessCsvOutput("inputHouseSales_1.csv", "postProcessHouseSales_1.csv");
        generatePostProcessCsvOutput("inputHouseSales_2.csv", "postProcessHouseSales_2.csv");
    }

    @SneakyThrows
    private static void buildPreProcessPipeline() {
        preProcessPipeline = Fluxtion.interpret(c ->
                DataFlow.subscribeToNode(PreProcessPipeline.buildScoringPipeline(c))
                        .push(GenerateData::writePostProcessRow));
        preProcessPipeline.init();
        preProcessPipeline.getExportedService(CalibrationProcessor.class).resetToOne();
        Files.createDirectories(dataDirPath);
    }

    private static void generatePreProcessInputCsv(String file, int count) throws IOException {
        try (Writer writer = Files.newBufferedWriter(dataDirPath.resolve(file))) {
            marshallerInput.writeHeaders(writer);
            for (int i = 0; i < count; i++) {
                marshallerInput.writeRow(randomHouseForSaleAdvert(), writer);
            }
        }
    }

    private static void generatePostProcessCsvOutput(String inFile, String outFile) throws IOException {
        try (Writer writer = Files.newBufferedWriter(dataDirPath.resolve(outFile))) {
            outputWriter = writer;
            marshallerOutput.writeHeaders(writer);
            BufferedReader reader = Files.newBufferedReader(dataDirPath.resolve(inFile));
            marshallerInput.stream(reader).forEach(preProcessPipeline::onEvent);
            reader.close();
        }
    }

    @SneakyThrows
    public static void writePostProcessRow(PredictiveModel model) {
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
