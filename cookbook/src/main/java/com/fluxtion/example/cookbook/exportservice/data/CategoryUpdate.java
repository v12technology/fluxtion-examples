package com.fluxtion.example.cookbook.exportservice.data;

public record CategoryUpdate(String categoryName, String accountName, String accountToGroup, boolean delete) {
}
