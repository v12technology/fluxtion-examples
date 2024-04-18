package com.fluxtion.example.cookbook.dataingestion;

import com.fluxtion.example.cookbook.dataingestion.api.DataIngestComponent;
import com.fluxtion.example.cookbook.dataingestion.api.DataIngestConfig;
import com.fluxtion.example.cookbook.dataingestion.api.HouseRecord;
import com.fluxtion.example.cookbook.dataingestion.pipeline.DataIngestionPipeline;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * Executes a {@link DataIngestionPipeline} with data from kaggle's AmesHousing.csv data file. The pipeline behaviour
 * <ul>
 *     <li>Subscribes to String events</li>
 *     <li>Tries to marshal the String from csv into a {@link HouseRecord} </li>
 *     <li>Transforms the {@link HouseRecord} by applying a user transform function</li>
 *     <li>Validates the transformed {@link HouseRecord} is valid with a user supplied {@link java.util.function.Predicate}</li>
 *     <li>Writes the valid {@link HouseRecord} to a user supplied {@link java.io.Writer} as CSV</li>
 *     <li>Writes the valid {@link HouseRecord} to a user supplied {@link java.io.OutputStream} in a binary format</li>
 *     <li>Processing stats are updated with each valid transformed {@link HouseRecord}</li>
 * </ul>
 *
 * Any processing errors are recorded as:
 * <ul>
 *     <li>An entry in the invalid log that writes to a user supplied {@link java.io.Writer}</li>
 *     <li>Processing stats are updated with each csv error</li>
 *     <li>Processing stats are updated with each {@link HouseRecord} validation failure</li>
 * </ul>
 *
 * Dynamic configuration is supplied in an instance of {@link DataIngestConfig} for:
 * <ul>
 *     <li>{@link HouseRecord} validation {@link java.util.function.Predicate}</li>
 *     <li>{@link HouseRecord} validation transformer as {@link java.util.function.UnaryOperator}</li>
 *     <li>Post process Csv output - {@link java.io.Writer}</li>
 *     <li>Post process binary output - {@link java.io.OutputStream}</li>
 *     <li>Statistics output - {@link java.io.Writer}</li>
 *     <li>Invalid log output - {@link java.io.Writer}</li>
 * </ul>
 *
 *
 */
public class Main {

    public static void main(String[] args) throws IOException {
        //set up pipeline
        var dataIngest = new DataIngestionPipeline();
        //lifecycle call to init pipeline, user components that implement DataIngestLifecycle receive init callback
        dataIngest.init();

        //get the exported DataIngestComponent service, used to set configuration as an api call
        DataIngestComponent dataIngestComponent = dataIngest.getExportedService();

        //set up a config for pipeline - can be changed dynamically during the run
        Path dataPath = Path.of("data/dataingest/");
        Path dataOutPath = Path.of("data/dataingest/output/");
        Files.createDirectories(dataOutPath);
        DataIngestConfig dataIngestConfig = DataIngestConfig.builder()
                .houseRecordValidator(houseRecord -> houseRecord.MS_Zoning().equalsIgnoreCase("FV"))
                .houseTransformer(Main::tansformInputHouseRecord)
                .csvWriter(Files.newBufferedWriter(dataOutPath.resolve("postProcessHouse.csv")))
                .binaryWriter(new BufferedOutputStream(Files.newOutputStream(dataOutPath.resolve("postProcessHouse.binary"))))
                .statsWriter(Files.newBufferedWriter(dataOutPath.resolve("processStats.rpt")))
                .invalidLogWriter(Files.newBufferedWriter(dataOutPath.resolve("processingErrors.log")))
                .build();

        //update the config for the pipeline
        dataIngestComponent.configUpdate(dataIngestConfig);

        //send some data as individual events
        try (Stream<String> reader = Files.lines(dataPath.resolve("input/AmesHousing.csv"))) {
            reader.forEach(dataIngest::onEvent);
        }

        //lifecycle call to close pipeline, user components that implement DataIngestLifecycle are receive tearDown callback
        dataIngest.tearDown();
    }

    //User supplied function to transform a HouseRecord for post process output
    public static HouseRecord tansformInputHouseRecord(HouseRecord houseRecord) {
        int lotFrontage = houseRecord.Lot_Frontage();
        houseRecord.Lot_Frontage_Squared(lotFrontage * lotFrontage);

        switch (houseRecord.MS_Zoning()) {
            case "A" -> houseRecord.ms_zone_category(1);
            case "FV" -> houseRecord.ms_zone_category(2);
            case "RL" -> houseRecord.ms_zone_category(3);
            case "RM" -> houseRecord.ms_zone_category(4);
            default -> houseRecord.ms_zone_category(-1);
        }
        return houseRecord;
    }

}
