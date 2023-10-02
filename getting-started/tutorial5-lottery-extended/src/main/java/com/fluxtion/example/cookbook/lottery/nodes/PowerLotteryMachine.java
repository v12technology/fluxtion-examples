package com.fluxtion.example.cookbook.lottery.nodes;

import com.fluxtion.example.cookbook.lottery.api.Ticket;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Supplier;

@Slf4j
public class PowerLotteryMachine extends LotteryMachineNode{
    public PowerLotteryMachine(Supplier<Ticket> ticketSupplier) {
        super(ticketSupplier);
    }
}
