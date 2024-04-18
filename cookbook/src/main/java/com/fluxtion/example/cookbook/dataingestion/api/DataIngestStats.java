package com.fluxtion.example.cookbook.dataingestion.api;

import java.util.function.Consumer;

public interface DataIngestStats extends DataIngestComponent {

    void publishStats();

    void currentStats(Consumer<String> consumer);

    void clearStats();
}
