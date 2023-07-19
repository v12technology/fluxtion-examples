package com.fluxtion.example.cookbook.exportservice.node;

import com.fluxtion.example.cookbook.exportservice.service.BankAccount;
import com.fluxtion.example.cookbook.exportservice.service.SpendingMonitor;
import com.fluxtion.example.cookbook.exportservice.service.StatementPublisher;
import com.fluxtion.runtime.annotations.ExportService;
import com.fluxtion.runtime.annotations.NoTriggerReference;
import com.fluxtion.runtime.annotations.builder.Inject;
import com.fluxtion.runtime.callback.ExportFunctionNode;
import com.fluxtion.runtime.output.SinkPublisher;

import java.util.function.Consumer;

public class StatementPublisherNode extends ExportFunctionNode implements @ExportService StatementPublisher {

    @NoTriggerReference
    private final SpendingMonitor spendingMonitor;
    @NoTriggerReference
    private final BankAccount bankAccount;

    @Inject(instanceName = "responseSink")
    public SinkPublisher<String> sinkPublisher;

    public StatementPublisherNode(SpendingMonitor spendingMonitor, BankAccount bankAccount) {
        this.spendingMonitor = spendingMonitor;
        this.bankAccount = bankAccount;
    }

    @Override
    public void publishStatement(Consumer<String> statementSink) {

    }
}
