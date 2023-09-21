package com.fluxtion.example.cookbook.lottery.api;

import java.util.function.Consumer;

/**
 * Service that runs the lottery, picks a ticket and publishes the results to a supplied Consumer.
 *  * If no resultPublisher is set the LotteryGame should fail with an exception at startup
 */
public interface LotteryGame {

    void selectWinningTicket();

    void setResultPublisher(Consumer<String> resultPublisher);
}
