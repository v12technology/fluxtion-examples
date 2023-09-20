package com.fluxtion.example.cookbook.lottery.nodes;

import com.fluxtion.example.cookbook.lottery.Ticket;
import com.fluxtion.example.cookbook.lottery.TicketStore;
import com.fluxtion.runtime.annotations.ExportService;
import com.fluxtion.runtime.annotations.NoPropagateFunction;
import com.fluxtion.runtime.annotations.Start;

import java.util.Objects;
import java.util.function.Consumer;

public class TicketStoreNode implements @ExportService TicketStore {

    private boolean storeOpen;
    private Consumer<String> receiptReceiver;
    private Ticket ticket;

    @Override
    @NoPropagateFunction
    public void setTicketSalesPublisher(Consumer<String> receiptReceiver) {
        this.receiptReceiver = receiptReceiver;
    }

    @Start
    public void start(){
        Objects.requireNonNull(receiptReceiver, "must have a receipt publisher set");
        storeOpen = false;
    }

    @Override
    public boolean buyTicket(Ticket ticket) {
        if(storeOpen){
            this.ticket = ticket;
        }
        return storeOpen;
    }

    public Ticket lastTicketSold(){
        return ticket;
    }

    @Override
    @NoPropagateFunction
    public void openStore() {
        storeOpen = true;
    }

    @Override
    @NoPropagateFunction
    public void closeStore() {
        storeOpen = false;
    }
}
