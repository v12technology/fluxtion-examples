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

public class BankingApp {

    private final EventProcessor<?> eventProcessor;
    private Account bankAccount;
    private CreditCheck creditCheck;
    private BankingOperations bankingOperations;

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
    }

    public void start() {
        eventProcessor.init();
        bankAccount = eventProcessor.asInterface();
        creditCheck = eventProcessor.asInterface();
        bankingOperations = eventProcessor.asInterface();
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
}
