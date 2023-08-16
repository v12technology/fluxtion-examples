package com.fluxtion.example.cookbook.spring;

import com.fluxtion.example.cookbook.spring.node.FileDataStore;
import com.fluxtion.example.cookbook.spring.service.Account;
import com.fluxtion.example.cookbook.spring.service.BankingOperations;
import com.fluxtion.example.cookbook.spring.service.CreditCheck;
import com.fluxtion.example.cookbook.util.GenerationStrategy;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Paths;

@Slf4j
public class Main {

    public static void main(String[] args) {
//        BankingApp bankingApp = new BankingApp(GenerationStrategy.GENERATE_AOT);
        BankingApp bankingApp = new BankingApp(GenerationStrategy.USE_AOT);
        //get services
        Account accountService = bankingApp.getBankAccount();
        BankingOperations bankControllerService = bankingApp.getBankingOperations();
        CreditCheck creditCheckService = bankingApp.getCreditCheck();
        //persistence
        FileDataStore fileDataStore = new FileDataStore(Paths.get("data/spring/bank"));
        bankControllerService.setDataStore(fileDataStore);
        //replay state and start
        fileDataStore.replay(bankingApp.getEventConsumer());
        bankingApp.start();
        //should reject unknown account
        accountService.deposit(999, 250.12);

        //get opening balance for acc 100
        accountService.publishBalance(100);

        //should reject bank closed
        accountService.openAccount(100);
        accountService.deposit(100, 250.12);

        //open bank and accept deposit
        bankControllerService.openForBusiness();
        accountService.deposit(100, 250.12);

        //blacklist an account, will reject transactions
        creditCheckService.blackListAccount(100);
        accountService.deposit(100, 46.90);

        //remove account from blacklist, now accepts transactions
        creditCheckService.whiteListAccount(100);
        accountService.deposit(100, 46.90);

        //close bank, reject all transactions
        bankControllerService.closedForBusiness();
        accountService.deposit(100, 13);
    }
}
