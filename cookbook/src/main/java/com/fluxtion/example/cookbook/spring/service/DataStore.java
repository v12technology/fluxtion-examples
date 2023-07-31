package com.fluxtion.example.cookbook.spring.service;


import com.fluxtion.example.cookbook.spring.data.Transaction;

public interface DataStore {
    void commitTransaction(Transaction transaction);
}
