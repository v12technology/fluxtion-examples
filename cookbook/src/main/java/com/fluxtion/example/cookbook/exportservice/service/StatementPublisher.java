package com.fluxtion.example.cookbook.exportservice.service;

import java.util.function.Consumer;

public interface StatementPublisher {

    void publishStatement(Consumer<String> statementSink);
}
