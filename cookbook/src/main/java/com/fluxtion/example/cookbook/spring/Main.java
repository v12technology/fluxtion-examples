package com.fluxtion.example.cookbook.spring;

import com.fluxtion.example.cookbook.util.GenerationStrategy;

public class Main {

    public static void main(String[] args) {
        BankingApp bankingApp = new BankingApp(GenerationStrategy.USE_AOT);
        bankingApp.start();
        bankingApp.getBankAccount().credit(100, 250.12);

        //blacklist an account
        bankingApp.getCreditCheck().blackListAccount(100);
        bankingApp.getBankAccount().credit(100, 46.90);

        //remove account from blacklist
        bankingApp.getCreditCheck().whiteListAccount(100);
        bankingApp.getBankAccount().credit(100, 46.90);
    }
}
