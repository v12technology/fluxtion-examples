package com.fluxtion.example.cookbook.spring.service;

public interface BankingOperations {

    void openForBusiness();
    void closedForBusiness();
    void setDataStore(DataStore dataStore);
}
