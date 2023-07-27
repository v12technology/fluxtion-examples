package com.fluxtion.example.cookbook.spring.service;

public interface CreditCheck {

    void blackListAccount(int accountNumber);
    void whiteListAccount(int accountNumber);
}
