package com.fluxtion.example.cookbook.spring.node;

import com.fluxtion.example.cookbook.spring.data.Transaction;
import com.fluxtion.example.cookbook.spring.service.Account;
import com.fluxtion.example.cookbook.spring.service.TransactionProcessor;
import com.fluxtion.runtime.annotations.*;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@ToString
@Slf4j
public class AccountNode implements @ExportService Account, TransactionProcessor {

    private transient Transaction currentTransaction;
    @NoTriggerReference
    private ResponsePublisher responsePublisher;
    private transient final Map<Integer, Double> account2Balance = new HashMap<>();
    private boolean replay;

    @Override
    public boolean debit(int accountNumber, double debitAmount) {
        currentTransaction = new Transaction(accountNumber, debitAmount, true);
        log.info("------------------------------------------------------");
        log.info("debit request:{}", currentTransaction);
        return processRequest();
    }

    @Override
    public boolean deposit(int accountNumber, double creditAmount) {
        log.info("------------------------------------------------------");
        currentTransaction = new Transaction(accountNumber, creditAmount, false);
        log.info("deposit request:{}", currentTransaction);
        return processRequest();
    }

    @Override
    @NoPropagateFunction
    public void publishBalance(int accountNumber) {
        log.info("------------------------------------------------------");
        responsePublisher.publishBalance(accountNumber, account2Balance.getOrDefault(accountNumber, Double.NaN));
    }

    @Override
    @NoPropagateFunction
    public void openAccount(int accountNumber) {
        log.info("------------------------------------------------------");
        log.info("opened account:{}", accountNumber);
        account2Balance.putIfAbsent(accountNumber, 0d);
    }

    @Override
    @NoPropagateFunction
    public void closeAccount(int accountNumber) {
        log.info("------------------------------------------------------");
        log.info("closed account:{}", accountNumber);
        account2Balance.remove(accountNumber);
    }

    @OnEventHandler(propagate = false)
    public boolean replayTransaction(Transaction transactionToAdd) {
        replay = true;
        int accountNumber = transactionToAdd.accountNumber();
        account2Balance.putIfAbsent(accountNumber, 0d);
        double balance = account2Balance.get(accountNumber) + transactionToAdd.signedAmount();
        account2Balance.put(accountNumber, balance);
        return false;
    }

    private boolean processRequest() {
        int accountNumber = currentTransaction.accountNumber();
        if (!account2Balance.containsKey(accountNumber)) {
            log.info("reject unknown account:{}", accountNumber);
            responsePublisher.rejectTransaction(currentTransaction);
            return false;
        }
        account2Balance.computeIfPresent(accountNumber, (i, d) -> d + currentTransaction.signedAmount());
        return true;
    }

    @AfterTrigger
    public void afterEventRequest() {
        if (!replay) {
            log.info("request complete");
            log.info("------------------------------------------------------\n");
        }
        replay = false;
    }

    @Override
    public Transaction currentTransactionRequest() {
        return currentTransaction;
    }

    public void rollbackTransaction() {
        int accountNumber = currentTransaction.accountNumber();
        account2Balance.computeIfPresent(accountNumber, (i, d) -> d - currentTransaction.signedAmount());
        currentTransaction = null;
    }

    public void commitTransaction() {
        int accountNumber = currentTransaction.accountNumber();
        log.info("updated balance:{} account:{}", account2Balance.get(accountNumber), accountNumber);
        currentTransaction = null;
    }

    public ResponsePublisher getResponsePublisher() {
        return responsePublisher;
    }

    public void setResponsePublisher(ResponsePublisher responsePublisher) {
        this.responsePublisher = responsePublisher;
    }
}
