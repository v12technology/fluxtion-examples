package com.fluxtion.example.cookbook.spring.node;

import com.fluxtion.example.cookbook.spring.node.BankTransactionStore;
import com.fluxtion.runtime.annotations.OnTrigger;
import lombok.Data;

@Data
public class TransactionPublisher {
    private BankTransactionStore transactionStore;

    @OnTrigger
    public boolean publishTransaction(){
        return true;
    }
}
