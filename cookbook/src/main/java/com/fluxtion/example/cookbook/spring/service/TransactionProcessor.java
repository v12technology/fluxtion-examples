package com.fluxtion.example.cookbook.spring.service;

import com.fluxtion.example.cookbook.spring.data.Transaction;

public interface TransactionProcessor {
    Transaction currentTransactionRequest();
    void rollbackTransaction();
    void commitTransaction();
}
