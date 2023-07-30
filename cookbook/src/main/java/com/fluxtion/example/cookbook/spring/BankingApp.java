package com.fluxtion.example.cookbook.spring;

import com.fluxtion.compiler.extern.spring.FluxtionSpring;
import com.fluxtion.example.cookbook.spring.generated.SpringBankEventProcessor;
import com.fluxtion.example.cookbook.spring.service.Account;
import com.fluxtion.example.cookbook.spring.service.BankingOperations;
import com.fluxtion.example.cookbook.spring.service.CreditCheck;
import com.fluxtion.example.cookbook.util.GenerationStrategy;
import com.fluxtion.runtime.EventProcessor;
import lombok.SneakyThrows;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.function.Consumer;

public class BankingApp {

    private final EventProcessor<?> eventProcessor;
    private final Account bankAccount;
    private final CreditCheck creditCheck;
    private final BankingOperations bankingOperations;
    private final Consumer eventConsumer;

    @SneakyThrows
    public BankingApp(GenerationStrategy generationStrategy) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("com/fluxtion/example/cookbook/spring/spring-account.xml");
        eventProcessor = switch (generationStrategy) {
            case USE_AOT -> new SpringBankEventProcessor();
            case INTERPRET -> FluxtionSpring.interpret(context);
            case COMPILE -> FluxtionSpring.compile(context);
            case GENERATE_AOT -> FluxtionSpring.compileAot(context, c -> {
                c.setPackageName("com.fluxtion.example.cookbook.spring.generated");
                c.setClassName("SpringBankEventProcessor");
            });
        };
        eventProcessor.init();
        bankAccount = eventProcessor.getExportedService();
        creditCheck = eventProcessor.getExportedService();
        bankingOperations = eventProcessor.getExportedService();
        eventConsumer = eventProcessor::onEvent;
    }

    public void start() {
        eventProcessor.start();
    }

    public Account getBankAccount() {
        return bankAccount;
    }

    public CreditCheck getCreditCheck() {
        return creditCheck;
    }

    public BankingOperations getBankingOperations() {
        return bankingOperations;
    }

    public <T> Consumer<T> getEventConsumer() {
        return eventConsumer;
    }
}
