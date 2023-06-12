package com.fluxtion.example.cookbook.exportfunction.nodes;

import com.fluxtion.example.cookbook.exportfunction.nodes.TradingPosition;
import com.fluxtion.runtime.annotations.OnTrigger;

public class BankAlert {
    private final TradingPosition tradingPosition;

    public BankAlert(TradingPosition tradingPosition) {
        this.tradingPosition = tradingPosition;
    }

    public BankAlert() {
        this(new TradingPosition());
    }

    @OnTrigger
    public boolean checkTradePosition(){
        double tradePosition = tradingPosition.getTradePosition();
        System.out.println("BankAlert - check trade position:" + tradePosition);
        if(tradePosition < 0){
            System.out.println("!!ALERT!! - negative trade position " + tradePosition);
            System.out.println("----");
            return true;
        }
        System.out.println("----");
        return false;
    }
}
