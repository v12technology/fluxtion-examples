package com.fluxtion.example.cookbook.exportfunction.nodes;

import com.fluxtion.example.cookbook.exportfunction.events.FxRate;
import com.fluxtion.runtime.annotations.ExportFunction;
import com.fluxtion.runtime.annotations.Initialise;
import com.fluxtion.runtime.annotations.OnEventHandler;
import com.fluxtion.runtime.callback.ExportFunctionNode;

public class SalesTracker extends ExportFunctionNode {
    private double incomeUsd;
    private double incomeGbp;
    private double rate;

    @OnEventHandler(filterString = "GBPUSD")
    public boolean fxChange(FxRate fxRate){
        rate = fxRate.getRate();
        incomeGbp = incomeUsd * rate;
        System.out.println("SalesTracker - fx update incomeGbp:" + incomeGbp);
        return !Double.isNaN(incomeGbp);
    }
    @ExportFunction("saleUpdate")
    public boolean updateSalesIncome(String reference, int quantity, double amount){
        incomeUsd += quantity * amount;
        incomeGbp = incomeUsd * rate;
        System.out.println("SalesTracker - sales income  incomeGbp:" + incomeGbp);
        return !Double.isNaN(incomeGbp);
    }

    @Initialise
    public void init(){
        incomeUsd = 0;
        incomeGbp = Double.NaN;
        rate = Double.NaN;
    }

    public double getIncomeGbp() {
        return incomeGbp;
    }
}
