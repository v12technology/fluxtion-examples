package com.fluxtion.example.cookbook.exportservice.service;

import com.fluxtion.example.cookbook.exportservice.data.Transaction;

public interface BankAccount {

    void deposit(Transaction deposit);

    void debit(Transaction debit);

    void setSpendingLimit(double amount);
}
