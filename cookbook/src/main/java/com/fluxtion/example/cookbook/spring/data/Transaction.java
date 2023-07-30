package com.fluxtion.example.cookbook.spring.data;

public record Transaction(int accountNumber, double amount, boolean debit) {
    public double signedAmount(){
        return debit ? -amount : amount;
    }
}
