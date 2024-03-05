package com.fluxtion.example.cookbook.lottery.nodes;

import com.fluxtion.example.cookbook.lottery.api.LotteryMachine;
import com.fluxtion.example.cookbook.lottery.api.Ticket;
import com.fluxtion.runtime.annotations.ExportService;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.function.Supplier;

@Slf4j
public class PowerLotteryMachine extends LotteryMachineNode implements @ExportService LotteryMachine {

    public PowerLotteryMachine(Supplier<Ticket> ticketSupplier) {
        super(ticketSupplier);
    }

    @Override
    public void selectWinningTicket() {
        if (ticketsBought.isEmpty()) {
            resultPublisher.accept("no tickets bought - no winning ticket");
        } else {
            Collections.shuffle(ticketsBought);
            winningTicket = ticketsBought.get(1);
            resultPublisher.accept("POWER-LOTTERY winning numbers:" + winningTicket.number());
        }
        ticketsBought.clear();
    }
}
