package com.fluxtion.example.cookbook.exportservice.data;

public record Transaction(String accountName, String destinationAccount, double amount) {
}
