package com.fluxtion.example.cookbook.lottery;

import java.util.function.Consumer;

public interface LotteryGame {

    void pickTicket();
    void setResultPublisher(Consumer<String> resultPublisher);
}
