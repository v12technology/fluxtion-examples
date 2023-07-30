package com.fluxtion.example.cookbook.spring.node;

import com.fluxtion.example.cookbook.spring.data.Transaction;
import com.fluxtion.example.cookbook.spring.service.DataStore;

import java.util.ArrayList;
import java.util.List;

public class InMemoryDataStore implements DataStore {
    private final List<Transaction> transactionList = new ArrayList<>();
    @Override
    public void commitTransaction(Transaction transaction) {
        transactionList.add(transaction);
    }

    public List<Transaction> getTransactionList() {
        return transactionList;
    }
}
