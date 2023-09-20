package com.fluxtion.example.cookbook.lottery.nodes;

import com.fluxtion.example.cookbook.lottery.LotteryGame;
import com.fluxtion.runtime.annotations.ExportService;
import com.fluxtion.runtime.annotations.OnTrigger;

import java.util.function.Consumer;

public class LotteryGameNode implements  @ExportService LotteryGame {

    private final TicketStoreNode ticketStoreNode;
    private Consumer<String> resultPublisher;

    public LotteryGameNode(){
        this(new TicketStoreNode());
    }

    public LotteryGameNode(TicketStoreNode ticketStoreNode) {
        this.ticketStoreNode = ticketStoreNode;
    }

    @Override
    public void setResultPublisher(Consumer<String> resultPublisher) {
        this.resultPublisher = resultPublisher;
    }

    @OnTrigger
    public boolean newTicketSale(){
        return false;
    }

    @Override
    public void pickTicket() {

    }
}
