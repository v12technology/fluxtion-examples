package com.fluxtion.example.cookbook.spring.node;

import com.fluxtion.example.cookbook.spring.service.TransactionProcessor;
import com.fluxtion.runtime.annotations.OnTrigger;
import lombok.Data;

@Data
public class BankTransactionStore {
    private TransactionProcessor transactionSource;

    @OnTrigger
    public boolean updateAccounts() {
        System.out.println("updating account:" + transactionSource.currentTransactionRequest());
        transactionSource.clearTransaction();
        return false;
    }
}
