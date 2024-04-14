package com.fluxtion.example.cookbook.dataingestion;



import com.fluxtion.example.cookbook.dataingestion.generated.DataIngestion;
import com.fluxtion.runtime.audit.EventLogControlEvent;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        var dataIngest = new DataIngestion();

        //Send some data
        dataIngest.init();
        dataIngest.setAuditLogLevel(EventLogControlEvent.LogLevel.DEBUG);

        dataIngest.onEvent("");
        System.out.println();

        dataIngest.onEvent("good");
        System.out.println();

        dataIngest.onEvent("BAD");
    }

}
