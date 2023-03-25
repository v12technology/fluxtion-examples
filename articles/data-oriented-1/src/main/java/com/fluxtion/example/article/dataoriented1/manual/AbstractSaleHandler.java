package com.fluxtion.example.article.dataoriented1.manual;

public class AbstractSaleHandler {
    protected double totalSales;
    protected double currentTaxRate = Double.NaN;
    protected double taxLiability = Double.NaN;

    protected void calculateTaxLiability() {
        taxLiability = totalSales * currentTaxRate;
    }

    public double taxLiability() {
        return taxLiability;
    }

}
