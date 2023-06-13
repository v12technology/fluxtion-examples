package com.fluxtion.example.cookbook.exportfunction;


import com.fluxtion.example.cookbook.exportfunction.data.Electronic;
import com.fluxtion.example.cookbook.exportfunction.data.Food;
import com.fluxtion.example.cookbook.exportfunction.data.Furniture;
import com.fluxtion.example.cookbook.exportfunction.data.StockDelivery;
import com.fluxtion.runtime.lifecycle.Lifecycle;

import java.util.Date;

public interface CashMonitor extends Lifecycle {
    void addCash(String paymentReference, Date dateReceived, double amount);

    void payBill(String billReference, Date datePaid, double amount);

    void electronicStockUpdate(StockDelivery<Electronic> arg0);

    void foodStockUpdate(StockDelivery<Food> arg0);

    void furnitureStockUpdate(StockDelivery<Furniture> arg0);

    void saleUpdate(String salesReference, int quantitySold, double totalPaymentReceived);

    void onEvent(Object event);
}
