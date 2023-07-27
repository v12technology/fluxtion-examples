package com.fluxtion.example.cookbook.spring.service;

public interface Account {
    void debit(int accountNumber, double debitAmount);

    void credit(int accountNumber, double creditAmount);
}
