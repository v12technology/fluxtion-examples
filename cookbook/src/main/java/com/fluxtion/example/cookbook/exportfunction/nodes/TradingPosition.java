package com.fluxtion.example.cookbook.exportfunction.nodes;

import com.fluxtion.runtime.annotations.ExportFunction;
import com.fluxtion.runtime.annotations.OnParentUpdate;
import com.fluxtion.runtime.callback.ExportFunctionNode;

import java.util.Date;

public class TradingPosition extends ExportFunctionNode {

    private final StockTracker stockTracker;
    private final SalesTracker salesTracker;
    private double tradePosition;

    public TradingPosition() {
        this(new StockTracker(), new SalesTracker());
    }

    public TradingPosition(StockTracker stockTracker, SalesTracker salesTracker) {
        this.stockTracker = stockTracker;
        this.salesTracker = salesTracker;
    }

    @ExportFunction
    public boolean addCash(String reference, Date date, double amount) {
        tradePosition += amount;
        System.out.println("TradingPosition - cash received:" + amount);
        System.out.println("TradingPosition - current:" + tradePosition);
        return true;
    }

    @ExportFunction
    public boolean payBill(String reference, Date date, double amount) {
        tradePosition -= amount;
        System.out.println("TradingPosition - pay bill:" + amount);
        System.out.println("TradingPosition - current:" + tradePosition);
        return true;
    }

    @OnParentUpdate
    public void stockChange(StockTracker stockTracker) {
        tradePosition -= stockTracker.getStockValueGbp();
        System.out.println("TradingPosition - stockTracker change current:" + tradePosition);
        setTriggered(true);
    }

    @OnParentUpdate
    public void salesChange(SalesTracker salesTracker) {
        tradePosition += salesTracker.getIncomeGbp();
        System.out.println("TradingPosition - salesTracker change current:" + tradePosition);
        setTriggered(true);
    }

    public double getTradePosition() {
        return tradePosition;
    }
}
