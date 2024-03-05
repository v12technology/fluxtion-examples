package com.fluxtion.example.cookbook.lottery;

import com.fluxtion.compiler.extern.spring.FluxtionSpring;
import com.fluxtion.example.cookbook.lottery.aot.LotteryProcessor;
import com.fluxtion.example.cookbook.lottery.api.GameResultStore;
import com.fluxtion.example.cookbook.lottery.api.LotteryMachine;
import com.fluxtion.example.cookbook.lottery.api.Ticket;
import com.fluxtion.example.cookbook.lottery.api.TicketStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.function.Consumer;

/**
 * A simple lottery game to demonstrate event processing logic built with Fluxtion
 */
@Slf4j
public class LotteryApp {

    private static LotteryMachine lotteryMachine;
    private static TicketStore ticketStore;
    private static GameResultStore resultStore;

    public static void main(String[] args) {
        start(LotteryApp::ticketReceipt, LotteryApp::lotteryResult);

        lotteryMachine.newGame();
        ticketStore.openStore();

        //open store and buy ticket
        Ticket myGoldenTicket = new Ticket(12_65_56);
        ticketStore.buyTicket(myGoldenTicket);
        ticketStore.buyTicket(new Ticket(36_58_58));
        ticketStore.buyTicket(new Ticket(73_00_12));

        //run the lottery
        lotteryMachine.selectWinningTicket();

        //our new functionality
        resultStore.isTicketSuccessful(
                myGoldenTicket,
                b -> System.out.println( "\n" + myGoldenTicket + " is a " + (b ? "WINNER :)\n" : "LOSER :(\n")));
        resultStore.publishReport(System.out::println);
    }

    public static void start(Consumer<String> ticketReceiptHandler, Consumer<String> resultsPublisher){
        var lotteryEventProcessor = new LotteryProcessor();
        lotteryEventProcessor.init();

        //get service interfaces
        lotteryMachine = lotteryEventProcessor.getExportedService();
        ticketStore = lotteryEventProcessor.getExportedService();
        resultStore = lotteryEventProcessor.getExportedService();

        //register listeners via service interface
        lotteryMachine.setResultPublisher(resultsPublisher);
        ticketStore.setTicketSalesPublisher(ticketReceiptHandler);

        //start the processor
        lotteryEventProcessor.start();
    }

    public static void ticketReceipt(String receipt){
        System.out.println(receipt);
    }

    public static void lotteryResult(String receipt){
        System.out.println(receipt);
    }
}
