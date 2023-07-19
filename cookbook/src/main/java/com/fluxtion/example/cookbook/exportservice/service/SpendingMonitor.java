package com.fluxtion.example.cookbook.exportservice.service;

import com.fluxtion.example.cookbook.exportservice.data.Transaction;

public interface SpendingMonitor {

    void addBucket(String name);
    void assignToBucket(String userAccountName, String accountToGroup, String category);
    void resetBucket(String bucketName);
    void debit(Transaction debit);
}
