package com.fluxtion.example.cookbook.lottery;

import com.fluxtion.compiler.extern.spring.FluxtionSpring;
import com.fluxtion.example.cookbook.lottery.aot.LotteryProcessor;
import com.fluxtion.example.cookbook.lottery.api.LotteryMachine;
import com.fluxtion.example.cookbook.lottery.api.Ticket;
import com.fluxtion.example.cookbook.lottery.api.TicketStore;
import com.fluxtion.example.cookbook.lottery.auditor.FluxtionSlf4jAuditor;
import com.fluxtion.runtime.EventProcessor;
import com.fluxtion.runtime.audit.EventLogControlEvent;
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
    private static EventProcessor<?> lotteryEventProcessor;

    public static void main(String[] args) {
        start(System.out::println, System.out::println);
        //try and buy a ticket - store is closed
        ticketStore.buyTicket(new Ticket(12_65_56));

        //open store and buy ticket
        ticketStore.openStore();
        ticketStore.buyTicket(new Ticket(12_65_56));
        ticketStore.buyTicket(new Ticket(36_58_58));
        ticketStore.buyTicket(new Ticket(73_00_12));

        //bad numbers
        ticketStore.buyTicket(new Ticket(25));

        //close the store and run the lottery
        ticketStore.closeStore();

        //try and buy a ticket - store is closed
        ticketStore.buyTicket(new Ticket(12_65_56));

        //run the lottery
        lotteryMachine.selectWinningTicket();

        //teardown - should print stats
        lotteryEventProcessor.tearDown();
    }

    public static void start(Consumer<String> ticketReceiptHandler, Consumer<String> resultsPublisher){
        lotteryEventProcessor = new LotteryProcessor();
        lotteryEventProcessor.init();
        lotteryEventProcessor.setAuditLogProcessor(new FluxtionSlf4jAuditor());
        lotteryMachine = lotteryEventProcessor.getExportedService();
        ticketStore = lotteryEventProcessor.getExportedService();
        lotteryMachine.setResultPublisher(resultsPublisher);
        ticketStore.setTicketSalesPublisher(ticketReceiptHandler);
        lotteryEventProcessor.start();
    }

    public static void ticketReceipt(String receipt){
        log.info(receipt);
    }

    public static void lotteryResult(String receipt){
        log.info(receipt);
    }

}
