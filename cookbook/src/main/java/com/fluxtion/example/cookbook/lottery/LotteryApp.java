package com.fluxtion.example.cookbook.lottery;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.example.cookbook.lottery.nodes.LotteryGameNode;
import com.fluxtion.runtime.EventProcessor;

public class LotteryApp {

    public void start(){
        EventProcessor<?> lotteryGame = Fluxtion.interpret(new LotteryGameNode());
//        lotteryGame.getExportedService()
    }
}
