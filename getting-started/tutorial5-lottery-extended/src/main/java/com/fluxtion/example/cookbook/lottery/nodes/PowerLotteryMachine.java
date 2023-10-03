package com.fluxtion.example.cookbook.lottery.nodes;

import com.fluxtion.example.cookbook.lottery.api.Ticket;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.function.Supplier;

@Slf4j
public class PowerLotteryMachine extends LotteryMachineNode {

    public PowerLotteryMachine(Supplier<Ticket> ticketSupplier) {
        super(ticketSupplier);
    }

    @Override
    public void selectWinningTicket() {
        if (ticketsBought.isEmpty()) {
            resultPublisher.accept("no tickets bought - no winning ticket");
        } else {
            Collections.shuffle(ticketsBought);
            resultPublisher.accept("winning numbers:" + ticketsBought.get(0));
        }
        ticketsBought.clear();
    }
}
