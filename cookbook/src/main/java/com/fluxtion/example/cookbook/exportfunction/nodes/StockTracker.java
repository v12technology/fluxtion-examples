package com.fluxtion.example.cookbook.exportfunction.nodes;

import com.fluxtion.example.cookbook.exportfunction.events.*;
import com.fluxtion.runtime.annotations.ExportFunction;
import com.fluxtion.runtime.annotations.Initialise;
import com.fluxtion.runtime.annotations.OnEventHandler;
import com.fluxtion.runtime.callback.ExportFunctionNode;

public class StockTracker extends ExportFunctionNode {

    private int totalQuantity;
    private double stockValueUsd;
    private double stockValueGbp;
    private double rate;
    @OnEventHandler(filterString = "GBPUSD")
    public boolean fxChange(FxRate fxRate){
        rate = fxRate.getRate();
        stockValueGbp = stockValueUsd * rate;
        System.out.println("StockTracker - fx update stockValueGbp:" + stockValueGbp);
        return true;
    }

    @ExportFunction("saleUpdate")
    public boolean updateStockLevels(String reference, int quantity, double amount){
        totalQuantity -= quantity;
        if(totalQuantity < 5){
            System.out.println("StockTracker Low stock level - " + totalQuantity);
        }
        return false;
    }

    @ExportFunction("foodStockUpdate")
    public boolean addStock(StockDelivery<Food> stockQuantity){
        if(stockQuantity.removingStock()){
            totalQuantity -= stockQuantity.amount();
            stockValueUsd -= stockQuantity.amount() * stockQuantity.costPerItem();
        }else {
            totalQuantity += stockQuantity.amount();
            stockValueUsd += stockQuantity.amount() * stockQuantity.costPerItem();
        }
        stockValueGbp = stockValueUsd * rate;
        System.out.println("StockTracker - add stock stockValueGbp:" + stockValueGbp);
        return true;
    }

    @ExportFunction("electronicStockUpdate")
    public boolean addStockElectronic(StockDelivery<Electronic> stockQuantity){
        //ignore
        return false;
    }

    @ExportFunction("furnitureStockUpdate")
    public boolean addStockFurniture(StockDelivery<Furniture> stockQuantity){
        //ignore
        return false;
    }

    @Initialise
    public void init(){
        totalQuantity = 0;
        stockValueUsd = 0;
        stockValueGbp = Double.NaN;
        rate = Double.NaN;
    }

    public double getStockValueGbp() {
        return stockValueGbp;
    }
}
