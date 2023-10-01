package com.fluxtion.example.cookbook.lottery.api;

import java.util.UUID;

public record Ticket(int number, UUID id) {

    public Ticket(int number){
        this(number, UUID.randomUUID());
    }
}
