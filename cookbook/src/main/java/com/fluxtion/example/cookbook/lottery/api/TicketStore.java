package com.fluxtion.example.cookbook.lottery.api;

import java.util.function.Consumer;

/**
 * Ticket store for lottery tickets. The store must be open before tickets can be bought.
 * If no ticketSalesPublisher is set the TicketStore should fail with an exception
 */
public interface TicketStore {
    boolean buyTicket(Ticket ticket);
    void openStore();
    void closeStore();
    void setTicketSalesPublisher(Consumer<String> ticketSalesPublisher);
}
