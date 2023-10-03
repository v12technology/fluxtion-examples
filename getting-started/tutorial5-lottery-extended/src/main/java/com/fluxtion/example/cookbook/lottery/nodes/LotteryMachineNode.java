package com.fluxtion.example.cookbook.lottery.nodes;

import com.fluxtion.example.cookbook.lottery.api.LotteryMachine;
import com.fluxtion.example.cookbook.lottery.api.Ticket;
import com.fluxtion.runtime.annotations.ExportService;
import com.fluxtion.runtime.annotations.OnTrigger;
import com.fluxtion.runtime.annotations.Start;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Slf4j
@RequiredArgsConstructor
public class LotteryMachineNode implements @ExportService LotteryMachine {

    protected final Supplier<Ticket> ticketSupplier;
    protected @Getter final transient List<Ticket> ticketsBought = new ArrayList<>();
    protected Consumer<String> resultPublisher;
    protected @Getter Ticket winningTicket;

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
        if (ticketsBought.isEmpty()) {
            resultPublisher.accept("no tickets bought - no winning ticket");
        } else {
            Collections.shuffle(ticketsBought);
            winningTicket = ticketsBought.get(0);
            resultPublisher.accept("winning numbers:" + winningTicket.number());
        }
    }

    public void newGame(){
        ticketsBought.clear();
    }
}
