package com.fluxtion.example.cookbook.lottery.nodes;

import com.fluxtion.example.cookbook.lottery.api.GameResultStore;
import com.fluxtion.example.cookbook.lottery.api.LotteryMachine;
import com.fluxtion.example.cookbook.lottery.api.Ticket;
import com.fluxtion.runtime.annotations.ExportService;
import com.fluxtion.runtime.annotations.builder.AssignToField;

import java.util.function.Consumer;

public class GameReportNode implements
        @ExportService GameResultStore,
        @ExportService LotteryMachine {

    private final LotteryMachineNode lotteryMachine;
    private final PowerLotteryMachine powerLotteryMachine;
    private Consumer<String> resultPublisher;
    private int gameCount;

    public GameReportNode(
            @AssignToField("lotteryMachine") LotteryMachineNode lotteryMachine,
            @AssignToField("powerLotteryMachine") PowerLotteryMachine powerLotteryMachine) {
        this.lotteryMachine = lotteryMachine;
        this.powerLotteryMachine = powerLotteryMachine;
    }

    @Override
    public boolean isTicketSuccessful(Ticket ticket, Consumer<Boolean> responseReceiver) {
        return false;
    }

    @Override
    public boolean publishReport(Consumer<String> reportReceiver) {
        return false;
    }

    @Override
    public void selectWinningTicket() {
        // store results
    }

    @Override
    public void setResultPublisher(Consumer<String> resultPublisher) {
        //publish report
    }

    @Override
    public void newGame() {
        gameCount++;
    }
}
