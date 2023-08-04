package com.fluxtion.example.cookbook.spring.service;

public interface Account {
    boolean debit(int accountNumber, double debitAmount);

    boolean deposit(int accountNumber, double creditAmount);

    void publishBalance(int accountNumber);

    void openAccount(int accountNumber);

    void closeAccount(int accountNumber);
}
