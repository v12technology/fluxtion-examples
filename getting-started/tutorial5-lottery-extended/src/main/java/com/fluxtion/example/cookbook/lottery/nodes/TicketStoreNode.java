package com.fluxtion.example.cookbook.lottery.nodes;

import com.fluxtion.example.cookbook.lottery.api.Ticket;
import com.fluxtion.example.cookbook.lottery.api.TicketStore;
import com.fluxtion.runtime.annotations.ExportService;
import com.fluxtion.runtime.annotations.NoPropagateFunction;
import com.fluxtion.runtime.annotations.Start;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Slf4j
public class TicketStoreNode implements
        Supplier<Ticket>,
        @ExportService TicketStore {

    private boolean storeOpen;
    private Consumer<String> ticketSalesPublisher;
    private Ticket ticket;

    @Override
    @NoPropagateFunction
    public void setTicketSalesPublisher(Consumer<String> ticketSalesPublisher) {
        this.ticketSalesPublisher = ticketSalesPublisher;
    }

    @Start
    public void start() {
        Objects.requireNonNull(ticketSalesPublisher, "must have a ticketSalesPublisher set");
        storeOpen = false;
    }

    @Override
    public boolean buyTicket(Ticket ticket) {
        if (ticket.number() < 9_99_99 | ticket.number() > 99_99_99) {
            ticketSalesPublisher.accept("invalid numbers " + ticket);
            this.ticket = null;
        } else if (storeOpen) {
            ticketSalesPublisher.accept("good luck with " + ticket);
            this.ticket = ticket;
        } else {
            ticketSalesPublisher.accept("store shut - no tickets can be bought");
            this.ticket = null;
        }
        return this.ticket != null;
    }

    @Override
    public Ticket get() {
        return ticket;
    }

    @Override
    @NoPropagateFunction
    public void openStore() {
        log.info("store opened");
        storeOpen = true;
    }

    @Override
    @NoPropagateFunction
    public void closeStore() {
        log.info("store closed");
        storeOpen = false;
    }
}
