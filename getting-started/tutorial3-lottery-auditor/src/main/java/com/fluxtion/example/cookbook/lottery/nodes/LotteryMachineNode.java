package com.fluxtion.example.cookbook.lottery.nodes;

import com.fluxtion.example.cookbook.lottery.api.LotteryMachine;
import com.fluxtion.example.cookbook.lottery.api.Ticket;
import com.fluxtion.runtime.annotations.ExportService;
import com.fluxtion.runtime.annotations.OnTrigger;
import com.fluxtion.runtime.annotations.Start;
import com.fluxtion.runtime.audit.EventLogNode;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class LotteryMachineNode extends EventLogNode implements @ExportService LotteryMachine {

    private final Supplier<Ticket> ticketSupplier;
    private final transient List<Ticket> ticketsBought = new ArrayList<>();
    private Consumer<String> resultPublisher;

    @Override
    public void setResultPublisher(Consumer<String> resultPublisher) {
        this.resultPublisher = resultPublisher;
    }

    @Start
    public void start() {
        Objects.requireNonNull(resultPublisher, "must set a results publisher before starting the lottery game");
    }

    @OnTrigger
    public boolean processNewTicketSale() {
        ticketsBought.add(ticketSupplier.get());
        return false;
    }

    @Override
    public void selectWinningTicket() {
        if (!ticketsBought.isEmpty()) {
            Collections.shuffle(ticketsBought);
            resultPublisher.accept("winning numbers:" + ticketsBought.get(0).number());
        }
        ticketsBought.clear();
    }
}
