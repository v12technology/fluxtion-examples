package com.fluxtion.example.cookbook.dataingestion.api;

import com.fluxtion.runtime.annotations.Initialise;
import com.fluxtion.runtime.annotations.TearDown;

public interface DataIngestLifecycle {

    @Initialise
    default void init(){}

    @TearDown
    default void tearDown(){}

}
