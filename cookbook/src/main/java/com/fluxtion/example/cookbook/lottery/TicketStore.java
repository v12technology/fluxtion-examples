package com.fluxtion.example.cookbook.lottery;

import java.util.function.Consumer;

public interface TicketStore {
    boolean buyTicket(Ticket ticket);
    void openStore();
    void closeStore();

    void setTicketSalesPublisher(Consumer<String> receiptReceiver);
}
