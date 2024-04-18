package com.fluxtion.example.cookbook.dataingestion;

import com.fluxtion.example.cookbook.dataingestion.pipeline.DataIngestionPipeline;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) throws IOException {
        var dataIngest = new DataIngestionPipeline();
        dataIngest.init();

        try (Stream<String> reader = Files.lines(Path.of("data/ml/linear_regression/AmesHousing.csv"))) {
            reader.forEach(dataIngest::onEvent);
        }

        dataIngest.tearDown();
    }

}
