package com.fluxtion.example.cookbook.lottery.api;

import java.util.function.Consumer;

public interface GameResultStore {

    boolean isTicketSuccessful(Ticket ticket, Consumer<Boolean> responseReceiver);
    boolean publishReport(Consumer<String> reportReceiver);
}
