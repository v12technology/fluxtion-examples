package com.fluxtion.example.cookbook.exportservice;

import com.fluxtion.example.cookbook.exportservice.node.BankAccountNode;
import com.fluxtion.example.cookbook.exportservice.service.BankAccount;
import com.fluxtion.example.cookbook.exportservice.service.SpendingMonitor;
import com.fluxtion.example.cookbook.exportservice.data.Transaction;
import com.fluxtion.example.cookbook.util.GenerationStrategy;
import com.fluxtion.runtime.annotations.NoPropagateFunction;
import lombok.SneakyThrows;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class Main {
    @SneakyThrows
    public static void main(String[] args) {
        BankingApp bankingApp = new BankingApp(GenerationStrategy.GENERATE_AOT);
        bankingApp.start();
        //
        BankAccount bankAccount = bankingApp.getBankAccount();
        bankAccount.debit(new Transaction("acc1", "acme goods", 220));
        bankAccount.debit(new Transaction("acc2", "j2go flights", 35));
        //
        SpendingMonitor spendingMonitor = bankingApp.getSpendingMonitor();
        spendingMonitor.assignToBucket("acc1", "2go flights", "travel");
        //
        bankingApp.stop();
    }
}
