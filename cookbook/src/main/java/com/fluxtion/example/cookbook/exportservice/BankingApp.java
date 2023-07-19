package com.fluxtion.example.cookbook.exportservice;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.example.cookbook.exportservice.generated.BankEventProcessor;
import com.fluxtion.example.cookbook.exportservice.node.BankAccountNode;
import com.fluxtion.example.cookbook.exportservice.node.SpendingMonitorNode;
import com.fluxtion.example.cookbook.exportservice.node.StatementPublisherNode;
import com.fluxtion.example.cookbook.exportservice.node.TransactionStore;
import com.fluxtion.example.cookbook.exportservice.service.BankAccount;
import com.fluxtion.example.cookbook.exportservice.service.SpendingMonitor;
import com.fluxtion.example.cookbook.exportservice.service.StatementPublisher;
import com.fluxtion.example.cookbook.util.GenerationStrategy;
import com.fluxtion.runtime.EventProcessor;
import lombok.SneakyThrows;

import java.io.FileWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class BankingApp {

    private final EventProcessor<?> eventProcessor;
    private SpendingMonitor spendingMonitor;
    private BankAccount bankAccount;
    private StatementPublisher statementPublisher;

    @SneakyThrows
    public BankingApp(GenerationStrategy generationStrategy) {
        eventProcessor = switch (generationStrategy) {
            case USE_AOT -> new BankEventProcessor();
            case INTERPRET -> Fluxtion.interpret(buildObjectGraph());
            case COMPILE -> Fluxtion.compile(buildObjectGraph());
            case GENERATE_AOT -> Fluxtion.compileAot(
                    "com.fluxtion.example.cookbook.exportservice.generated",
                    "BankEventProcessor", buildObjectGraph());
        };
    }

    private static Object[] buildObjectGraph() {
        BankAccount bankAccount = new BankAccountNode();
        SpendingMonitor spendingMonitor = new SpendingMonitorNode();
        StatementPublisher statementPublisher = new StatementPublisherNode(spendingMonitor, bankAccount);
        return new Object[]{statementPublisher};
    }

    @SneakyThrows
    public void start() {
        Path rootStoreDir = Paths.get("src/main/resources/exportservice");
        Path transactionStore = rootStoreDir.resolve("transaction.jsonl");
        Path categoryStore = rootStoreDir.resolve("category.jsonl");
        Files.createDirectories(rootStoreDir);
        start(new FileWriter(transactionStore.toFile(), true), new FileWriter(categoryStore.toFile(), true));
    }

    public void start(Writer transactionWriter, Writer categoryWriter) {
        eventProcessor.injectNamedInstance(transactionWriter, Writer.class, TransactionStore.TRANSACTION_WRITER);
        eventProcessor.injectNamedInstance(categoryWriter, Writer.class, TransactionStore.CATEGORY_WRITER);
        eventProcessor.init();
        this.bankAccount = eventProcessor.asInterface();
        this.spendingMonitor = eventProcessor.asInterface();
        this.statementPublisher = eventProcessor.asInterface();
        eventProcessor.addSink("responseSink", (String s) -> System.out.println(s));
    }

    public void stop() {
        eventProcessor.tearDown();
    }

    public SpendingMonitor getSpendingMonitor() {
        return spendingMonitor;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public StatementPublisher getStatementPublisher() {
        return statementPublisher;
    }
}
