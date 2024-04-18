package com.fluxtion.example.cookbook.dataingestion.api;

public interface DataIngestComponent {

    default boolean configUpdate(DataIngestConfig config){
        return false;
    }
}
